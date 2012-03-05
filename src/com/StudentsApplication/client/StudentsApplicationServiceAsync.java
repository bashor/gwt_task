package com.StudentsApplication.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StudentsApplicationServiceAsync {
    // Sample interface method of remote interface
    void getStudents(AsyncCallback<Student[]> async);
}
