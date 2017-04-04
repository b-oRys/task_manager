package controller;


import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import model.Task;

import java.util.Date;

public class EditingTaskSceneController {


    private Main mainApp;

    private Task selectedTask;

    public Task getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }

    public EditingTaskSceneController(){

    }

    @FXML
    private void initialize (){

        displayInfoInFields(selectedTask);

    }

    private void displayInfoInFields(Task task) {

    }

    public void setMainApp (Main mainApp) {
        this.mainApp = mainApp;
    }



}
