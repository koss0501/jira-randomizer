package com.apptimized.tools.randomizer.Randomizer.UIInterface.Controllers;

import com.apptimized.tools.randomizer.JiraEntities.Issue;
import com.apptimized.tools.randomizer.JiraEntities.User;
import com.apptimized.tools.randomizer.Utils.JsonActions;
import com.apptimized.tools.randomizer.RestClient.RestActions;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import static com.apptimized.tools.randomizer.Utils.JsonActions.*;
import static com.apptimized.tools.randomizer.Randomizer.UIInterface.Actions.UIActions.*;
import static com.apptimized.tools.randomizer.Utils.ActionUtils.findQA;
import static com.apptimized.tools.randomizer.Utils.ConstStorage.ORDER_PACKAGING_TRANS_ID;
import static com.apptimized.tools.randomizer.Utils.ConstStorage.PACKAGERS_USERS_GROUP;

public class MainController implements Initializable {
    private ObservableList<Issue> issuesData = FXCollections.observableArrayList();
    private ObservableList<User> usersData = FXCollections.observableArrayList();
    private ArrayList<User> usersList = new ArrayList<>();
    private ArrayList<Issue> issuesList = new ArrayList<>();

    @FXML
    private TextField jqlTextInput;

    @FXML
    private ToggleGroup radioGroup1;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private Label parentIssueIdLabel;

    @FXML
    private TextField appNameField, appVersionField, appVendorField, amountField, summaryField, parentIssueIdField;

    @FXML
    private RadioButton subtasksRadio, pairRadio;

    @FXML
    private Button shuffleButton, createIssuesButton, pushToJiraButton, loadQueryResultButton;

    @FXML
    private TableView<Issue> issuesTable;

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> emailAddressColumn;

    @FXML
    private TableColumn<Issue, String> keyColumn;

    @FXML
    private TableColumn<Issue, String> summaryColumn;

    @FXML
    private TableColumn<Issue, String> assigneeColumn;

    @FXML
    private TableColumn<Issue, String> packagerColumn;

    @FXML
    private TableColumn<Issue, String> qaColumn;

    @FXML
    private void initialize(ArrayList<Issue> issuesList) {
        initData(issuesList);
        keyColumn.setCellValueFactory(new PropertyValueFactory<Issue, String>("key"));
        summaryColumn.setCellValueFactory(new PropertyValueFactory<Issue, String>("summary"));
        assigneeColumn.setCellValueFactory(new PropertyValueFactory<Issue, String>("assignee"));
        packagerColumn.setCellValueFactory(new PropertyValueFactory<Issue, String>("packager"));
        qaColumn.setCellValueFactory(new PropertyValueFactory<Issue, String>("qa_engineer"));
        issuesTable.setItems(issuesData);
    }

    private void initData(ArrayList<Issue> issuesList) {
        issuesData.clear();
        issuesData.addAll(issuesList);
    }

    private void initializeUserstable(ArrayList<User> usersList) {
        initUsersData(usersList);
        nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        emailAddressColumn.setCellValueFactory(new PropertyValueFactory<User, String>("emailAddress"));
        usersTable.setItems(usersData);
    }

    private void initUsersData(ArrayList<User> usersList) {
        usersData.clear();
        usersData.addAll(usersList);
    }

    @FXML
    protected void getQueryResultsInTable(ActionEvent event) {
        new Thread(() -> {
            getQueryResultsInTableHandler();
        }).start();
    }

    private void getQueryResultsInTableHandler() {
        try {
            loadQueryResultButton.setDisable(true);
            String query = jqlTextInput.getText();
            if(query.equals("")) {
                showMessage(Alert.AlertType.INFORMATION, "Information",
                        "Oooops...",
                        "Query is empty. Please write query for search");
                return;
            }
            String jsonArray = RestActions.getQueryResults(query);
            if (JsonActions.getNodeFromJson(jsonArray, "warningMessages") != null) {
                showMessage(Alert.AlertType.WARNING, "Attention!",
                        "Oooops... Please, fix your query (JQL)",
                        JsonActions.getNodeFromJson(jsonArray, "warningMessages"));
                return;
            }
            if (JsonActions.getNodeFromJson(jsonArray, "errorMessages") != null) {
                showMessage(Alert.AlertType.WARNING, "Attention!",
                        "Oooops... Please, fix your query (JQL)",
                        JsonActions.getNodeFromJson(jsonArray, "errorMessages"));
                return;
            }
            if (jsonArray.contains("\"issues\":[]")) {
                showMessage(Alert.AlertType.INFORMATION, "Information",
                        "Oooops...",
                        "Query hasn't returned any results");
                return;
            }
            issuesList =  parseIssues(jsonArray);
            if (issuesList.size() > 0) {
                shuffleButton.setDisable(false);
                pushToJiraButton.setDisable(true);
            }
            initialize(issuesList);
        } catch(Exception ex) {
            showExceptionAlert(ex);
        } finally {
            loadQueryResultButton.setDisable(false);
        }
    }

    @FXML
    protected void makeShuffle(ActionEvent event) {
        try {
            ArrayList<User> usersList =  parseUsers(RestActions.getUsersFromGroup(PACKAGERS_USERS_GROUP));
            Collections.shuffle(usersList);
            Collections.shuffle(issuesList);
            for (int i = 0; i < usersList.size(); i++) {
                if(usersList.get(i).getQaEngineerName() == null) {
                    usersList = findQA(usersList, i);
                }
            }
            if(usersList.size() != issuesList.size()) {
                showMessage(Alert.AlertType.WARNING, "Attention!",
                        "Oooops... ",
                        "Issues count should be equal to users count.\n" +
                                "As for now issues count '" + issuesList.size() + "' doesn't equal users count '" + usersList.size() + "'");
                return;
            }
            for (int i = 0; i < issuesList.size(); i++) {
                issuesList.get(i).setAssignee(usersList.get(i).getName());
                issuesList.get(i).setPackager(usersList.get(i).getName());
                issuesList.get(i).setQa_engineer(usersList.get(i).getQaEngineerName());
            }
            pushToJiraButton.setDisable(false);
            initialize(issuesList);
        } catch(Exception ex) {
            showExceptionAlert(ex);
        }
    }

    @FXML
    protected void pushToJira(ActionEvent event) {
        new Thread(() -> {
            pushToJiraHandler();
        }).start();
    }

    @FXML
    private void pushToJiraHandler() {
        try {
            loadQueryResultButton.setDisable(true);
            pushToJiraButton.setDisable(true);
            shuffleButton.setDisable(true);
            for (int i = 0; i < issuesList.size(); i++) {
                RestActions.setAssignee(issuesList.get(i).getAssignee(), issuesList.get(i).getKey());
                RestActions.setPackager(issuesList.get(i).getPackager(), issuesList.get(i).getKey());
                RestActions.setQAEngineer(issuesList.get(i).getQa_engineer(), issuesList.get(i).getKey());
            }

            initialize(issuesList);
        } catch(Exception ex) {
            showExceptionAlert(ex);
        } finally {
            loadQueryResultButton.setDisable(false);
            shuffleButton.setDisable(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        radioGroup1.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (pairRadio.isSelected()) {
                    parentIssueIdLabel.setDisable(true);
                    parentIssueIdField.setDisable(true);
                }
                if (subtasksRadio.isSelected()) {
                    parentIssueIdLabel.setDisable(false);
                    parentIssueIdField.setDisable(false);
                }
            }
        });
        try {
            if(usersList.size() == 0) {
                usersList =  parseUsers(RestActions.getUsersFromGroup(PACKAGERS_USERS_GROUP));
            }
            initializeUserstable(usersList);
        } catch(Exception ex) {
            showExceptionAlert(ex);
        }
    }

    @FXML
    protected void createIssues(ActionEvent event) {
        new Thread(() -> {
            createIssuesHandler();
        }).start();
    }

    private void createIssuesHandler() {
        try {
            createIssuesButton.setDisable(true);
            String result;
            int amount = Integer.parseInt(amountField.getText().equals("") ? "0" : amountField.getText());
            String appName = appNameField.getText();
            String appVersion = appVersionField.getText();
            String appVendor = appVendorField.getText();
            String summary = summaryField.getText();
            String parentID = parentIssueIdField.getText();
            if (summary.equals("")) {
                summary = (!appVendor.equals("") ? appVendor + "_" : appVendor) + appName + "_" + appVersion;
            }
            outputTextArea.appendText("----------------------\n");
            if(subtasksRadio.isSelected()) {
                if(parentID.equals("")) {
                    parentID = RestActions.createIssue(summary, appName, appVersion, appVendor);
                    parentID = JsonActions.getNodeFromJson(parentID, "key").replace("\"", "");
                }
                outputTextArea.appendText("Created root issue: https://sdctest.revacom.com/browse/" + parentID + "\n\n");
                for (int i = 0; i < amount; i++) {
                    result = RestActions.createSubtask(parentID, "Package of " + summary, appName, appVersion, appVendor);
                    result = JsonActions.getNodeFromJson(result, "key").replace("\"", "");
                    RestActions.makeTransitionById(result, ORDER_PACKAGING_TRANS_ID);
                    outputTextArea.appendText("Created subtask # " + (i+1) + ": https://sdctest.revacom.com/browse/"
                            + result + "\n\n");
                }
            }
            if(pairRadio.isSelected()) {
                for (int i = 0; i < amount; i++) {
                    parentID = RestActions.createIssue(summary, appName, appVersion, appVendor);
                    outputTextArea.appendText("Created root issue: https://sdctest.revacom.com/browse/" + parentID + "\n\n");
                    result = RestActions.createSubtask(parentID, "Package of " + summary, appName, appVersion, appVendor);
                    result = JsonActions.getNodeFromJson(result, "key").replace("\"", "");
                    RestActions.makeTransitionById(result, ORDER_PACKAGING_TRANS_ID);
                    outputTextArea.appendText("Created subtask # " + (i+1) + ": https://sdctest.revacom.com/browse/" + result + "\n\n");
                }
            }
            outputTextArea.appendText("-----------------");
        } catch(Exception ex) {
            showExceptionAlert(ex);
        } finally {
            createIssuesButton.setDisable(false);
        }
    }

}
