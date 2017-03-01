package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Task;

import java.util.Date;

public class TaskOverviewController {

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task,SimpleStringProperty> dateColumn;

    @FXML
    private TableColumn<Task,SimpleStringProperty> titleColumn;

    @FXML
    public Label titleInfoLabel;

    @FXML
    public Label timeInfoLabel;

    @FXML
    public Label startInfoLabel;

    @FXML
    public Label endInfoLabel;

    @FXML
    public Label intervalInfoLabel;

    @FXML
    public Label statusInfoLabel;

    @FXML
    private CheckBox activateCheckBox;

    private Main mainApp;

    public TaskOverviewController (){

    }

    @FXML
    private void initialiize (){
        dateColumn.setCellValueFactory(new PropertyValueFactory<Task, SimpleStringProperty>("time"));
//        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nextTimeAfter(new Date(0)).toString()));

        //titleColumn.setCellValueFactory();
        titleColumn.setCellValueFactory(new PropertyValueFactory<Task, SimpleStringProperty>("title"));

//        taskTable.setItems(mainApp.getTaskData());

        showTaskDetails(null);

        taskTable.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue)-> showTaskDetails(newValue));

    }

    public void setMainApp (Main mainApp) {
        this.mainApp = mainApp;
        taskTable.setItems(mainApp.getTaskData());

    }

    public void addTask(){
        mainApp.getTaskData().add(new Task("runing", new Date(567896)));
    }

    private void showTaskDetails(Task task){
        if (task != null){

            titleInfoLabel.setText(task.getTitle());
            statusInfoLabel.setText(task.isActive() ? "Активна" : "Неактивна");

            if (task.isRepeated()){
                startInfoLabel.setText(task.getStartTime().toString());
                endInfoLabel.setText(task.getEndTime().toString());
                intervalInfoLabel.setText(String.valueOf(task.getRepeatInterval()));
            }else {
                timeInfoLabel.setText(task.getTime().toString());
            }

        }else{
            titleInfoLabel.setText("");
            statusInfoLabel.setText("");
            startInfoLabel.setText("");
            endInfoLabel.setText("");
            intervalInfoLabel.setText("");
            timeInfoLabel.setText("");
        }

    }


}
