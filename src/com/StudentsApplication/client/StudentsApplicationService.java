package com.StudentsApplication.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("StudentsApplicationService")
public interface StudentsApplicationService extends RemoteService {
    // Sample interface method of remote interface
    Student[] getStudents();

    /**
     * Utility/Convenience class.
     * Use StudentsApplicationService.App.getInstance() to access static instance of StudentsApplicationServiceAsync
     */
    public static class App {
        private static StudentsApplicationServiceAsync ourInstance = GWT.create(StudentsApplicationService.class);

        public static synchronized StudentsApplicationServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
