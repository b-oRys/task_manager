package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Task;

import java.util.Date;

/**
 * Created by trm_cp on 2/3/17.
 */
public class EditingIntervalSceneController {


    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task,String> dateColumn;
    @FXML
    private TableColumn<Task,String> titleColumn;

    private Main mainApp;

    public EditingIntervalSceneController (){

    }

    @FXML
    private void initialize (){
        dateColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        cellData.getValue().nextTimeAfter(new Date(0)).toString() + "\n"
                )
        );


        titleColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        cellData.getValue().getTitle()
                )
        );
//
//        showTaskDetails(null);
//
        taskTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTaskDetails(newValue));

    }

    public void showTaskDetails(Task task){}

    public void setMainApp (Main mainApp) {
        this.mainApp = mainApp;
        ObservableList<Task> taskData = FXCollections.observableArrayList();
        Task task0 = new Task("house work", new Date(44444));
        task0.setActive(true);
        Task task1 = new Task("program", new Date(55555));
        task1.setActive(true);
        Task task2 = new Task("walk", new Date(234653));
        task2.setActive(true);

        taskData.add(task0);
        taskData.add(task1);
        taskData.add(task2);


        taskTable.setItems(taskData);
//        taskTable.setItems(mainApp.getTaskData());
    }

}
