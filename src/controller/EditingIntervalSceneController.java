package controller;

import javafx.beans.property.SimpleStringProperty;
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
//        taskTable.getSelectionModel().selectedItemProperty().addListener(
//                (observable, oldValue, newValue) -> showTaskDetails(newValue));

    }

    public void setMainApp (Main mainApp) {
        this.mainApp = mainApp;
        taskTable.setItems(mainApp.getTaskData());
    }

}
