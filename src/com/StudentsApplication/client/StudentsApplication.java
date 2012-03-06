package com.StudentsApplication.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudentsApplication implements EntryPoint {

    public static final int REFRESH_INTERVAL = 5000; //ms
    public static final int FIRST_ELEMENT_INDEX = 2;

    private List<Student> students = new ArrayList<Student>();

    final ToggleButton findButton = new ToggleButton("Show filter", "Hide filter");
    final MultiWordSuggestOracle firstNameOracle = new MultiWordSuggestOracle();
    final SuggestBox firstNameFilter = new SuggestBox(firstNameOracle);
    final ListBox sureNameFilter = new ListBox();

    final ListBox groupFilter = new ListBox();

    private FlexTable table = new FlexTable();

    private StudentsApplicationServiceAsync stockPriceSvc = GWT.create(StudentsApplicationService.class);

    public void onModuleLoad() {
        findButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                table.getRowFormatter().setVisible(1, findButton.isDown());
                updateRowsVisible();
            }
        });

        firstNameFilter.setStyleName("filterElement");
        sureNameFilter.setStyleName("filterElement");
        sureNameFilter.addItem("All");
        groupFilter.addItem("All");

        ChangeHandler FilterFieldChangedHandler = new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                updateRowsVisible();
            }
        };

        firstNameFilter.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                updateRowsVisible();
            }
        });
        sureNameFilter.addChangeHandler(FilterFieldChangedHandler);
        groupFilter.addChangeHandler(FilterFieldChangedHandler);

        RootPanel.get().add(findButton);

        table.setText(0, 0, "Name");
        table.setText(0, 1, "Sure Name");
        table.setText(0, 2, "Avg. Ball");
        table.setText(0, 3, "Group #");
        table.setText(0, 4, "");
        table.getCellFormatter().setStyleName(0, 0, "listNameCollumn");
        table.getCellFormatter().setStyleName(0, 1, "listNameCollumn");
        table.getCellFormatter().addStyleName(0, 2, "listNumericColumnHeader");
        table.getCellFormatter().addStyleName(0, 3, "listNumericColumnHeader");
        table.getCellFormatter().addStyleName(0, 4, "listRemoveColumn");

        table.setWidget(1, 0, firstNameFilter);
        table.setWidget(1, 1, sureNameFilter);
        table.setWidget(1, 3, groupFilter);
        //table.setWidget(1, 4, hideFilterButton);
        table.getCellFormatter().setStyleName(1, 0, "listNameCollumn");
        table.getCellFormatter().setStyleName(1, 1, "listNameCollumn");
        table.getCellFormatter().addStyleName(1, 2, "listNumericColumn");
        table.getCellFormatter().addStyleName(1, 3, "listNumericColumn");
        table.getCellFormatter().addStyleName(1, 4, "listRemoveColumn");

        table.getRowFormatter().setVisible(1, findButton.isDown());

        table.getRowFormatter().addStyleName(0, "listHeader");
        table.addStyleName("list");

        RootPanel.get().add(table);

//        Timer refreshTimer = new Timer() {
//            @Override
//            public void run() {
//                refreshList();
//            }
//        };
//        refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
        refreshList();
    }

    private void updateRowsVisible() {
        for (int i = 0; i < students.size(); i++) {
            Student st = students.get(i);

            boolean v = true;

            if (findButton.isDown()) {
                if (!firstNameFilter.getText().isEmpty())
                    v = st.getFirstName().contains(firstNameFilter.getText());

                if (sureNameFilter.getSelectedIndex() != 0)
                    v = v && st.getSureName().equals(sureNameFilter.getItemText(sureNameFilter.getSelectedIndex()));

                if (groupFilter.getSelectedIndex() != 0)
                    v = v && groupFilter.getItemText(groupFilter.getSelectedIndex()).equals(Integer.toString(st.getGroupNumber()));
            }
            table.getRowFormatter().setVisible(i + FIRST_ELEMENT_INDEX, v);
        }
    }

    private void refreshList() {
        RootPanel.get("LoadIndicator").setVisible(true);

        if (stockPriceSvc == null) {
            stockPriceSvc = GWT.create(StudentsApplicationService.class);
        }

        // Make the call to the stock price service.
        stockPriceSvc.getStudents(new AsyncCallback<Student[]>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
                RootPanel.get("LoadIndicator").setVisible(false);
            }

            public void onSuccess(Student[] result) {
                Set<String> firstNames = new HashSet<String>();
                Set<String> sureNames = new HashSet<String>();
                Set<String> groups = new HashSet<String>();
                for (Student s : result) {
                    addLine(s);

                    //TODO: fix this, it's don't work when continues update
                    if (!firstNames.contains(s.getFirstName())) {
                        firstNames.add(s.getFirstName());
                        firstNameOracle.add(s.getFirstName());
                    }
                    if (!sureNames.contains(s.getSureName())) {
                        sureNames.add(s.getSureName());
                        sureNameFilter.addItem(s.getSureName());
                    }
                    String groupNumberAsString = Integer.toString(s.getGroupNumber());
                    if (!groups.contains(groupNumberAsString)) {
                        groups.add(groupNumberAsString);
                        groupFilter.addItem(groupNumberAsString);
                    }
                }

                RootPanel.get("LoadIndicator").setVisible(false);
            }
        });
    }

    private class RowRemover implements ClickHandler {
        final Student student;

        public RowRemover(Student student) {
            this.student = student;
        }

        @Override
        public void onClick(ClickEvent event) {
            int removedIndex = students.indexOf(student);
            students.remove(removedIndex);
            table.removeRow(removedIndex + FIRST_ELEMENT_INDEX);
        }
    }

    private void addLine(Student student) {
        int row = students.indexOf(student);

        if (row == -1) {
            row = students.size();
            students.add(student);
        }

        row += FIRST_ELEMENT_INDEX;

        table.setText(row, 0, student.getFirstName());
        table.setText(row, 1, student.getSureName());
        table.setText(row, 2, NumberFormat.getFormat("#,##0.00").format(student.getAvgBall()));
        table.setText(row, 3, Integer.toString(student.getGroupNumber()));

        Button removeButton = new Button("x");
        removeButton.addClickHandler(new RowRemover(student));
        table.setWidget(row, 4, removeButton);

        table.getCellFormatter().setStyleName(row, 0, "listNameCollumn");
        table.getCellFormatter().setStyleName(row, 1, "listNameCollumn");
        table.getCellFormatter().addStyleName(row, 2, "listNumericColumn");
        table.getCellFormatter().addStyleName(row, 3, "listNumericColumn");
        table.getCellFormatter().addStyleName(row, 4, "listRemoveColumn");
    }
}
