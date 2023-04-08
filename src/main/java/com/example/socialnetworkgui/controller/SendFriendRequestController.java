package com.example.socialnetworkgui.controller;


import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.utils.SimpleAlertBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class SendFriendRequestController extends AbstractController implements Initializable {
    @FXML
    private Button sendRequestButton;
    @FXML
    private TextField userNameTextField;

    @FXML
    private TableColumn<User,String> firstNameColumn;
    @FXML
    private TableColumn<User,String> lastNameColumn;
    @FXML
    private TableView<User> usersTableView;
    @FXML
    private ObservableList<User> userObservableList = FXCollections.observableArrayList();

    private User user;

    public SendFriendRequestController(User user)
    {
        this.user = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastName()));
        usersTableView.setItems(userObservableList);
    }

    public void findUsersByName(ActionEvent actionEvent) {
        String name = userNameTextField.getText();
        if(!Objects.equals(name, ""))
        {
            List<User> users = service.findUserByName(name,user.getId());
            userObservableList.setAll(users);
        }
        else
        {
            SimpleAlertBuilder simpleAlertBuilder = new SimpleAlertBuilder(Alert.AlertType.WARNING,"Message",
                    "Write something in the text field first!");
            simpleAlertBuilder.show();
        }
    }

    public void sendRequestToUser(ActionEvent actionEvent) {
        int index = usersTableView.getSelectionModel().getSelectedIndex();
        if(index!=-1)
        {
            User userSelected = usersTableView.getSelectionModel().getSelectedItem();
            List<User> userFriends = service.userFriends(user.getId());

            if(!userFriends.contains(userSelected))//verify if they are friends
            {
                if(!service.alreadySentFriendRequest(user.getId(),userSelected.getId()))//verify if already sent request
                {
                    service.sendFriendRequest(user.getId(), userSelected.getId());
                    SimpleAlertBuilder simpleAlertBuilder = new SimpleAlertBuilder(Alert.AlertType.INFORMATION,"Message",
                            "Request sent successfully!");
                    simpleAlertBuilder.show();
                }
                else
                {
                    SimpleAlertBuilder simpleAlertBuilder = new SimpleAlertBuilder(Alert.AlertType.INFORMATION,"Message",
                            "Already sent friend request or you have a request from the user!");
                    simpleAlertBuilder.show();
                }
            }
            else
            {
                SimpleAlertBuilder simpleAlertBuilder = new SimpleAlertBuilder(Alert.AlertType.INFORMATION,"Message",
                        "Already friends!");
                simpleAlertBuilder.show();
            }

        }
        else
        {
            SimpleAlertBuilder simpleAlertBuilder = new SimpleAlertBuilder(Alert.AlertType.INFORMATION,"Message",
                    "Select a user!");
            simpleAlertBuilder.show();
        }
    }

    public void cancelRequestToUser(ActionEvent actionEvent) {
        int index = usersTableView.getSelectionModel().getSelectedIndex();

        if(index != 1)
        {
            User userSelected = usersTableView.getSelectionModel().getSelectedItem();
            if(service.cancelFriendRequest(user.getId(),userSelected.getId()))
            {
                SimpleAlertBuilder simpleAlertBuilder = new SimpleAlertBuilder(Alert.AlertType.INFORMATION,"Message",
                        "The request has been canceled.");
                simpleAlertBuilder.show();
            }
            else
            {
                SimpleAlertBuilder simpleAlertBuilder = new SimpleAlertBuilder(Alert.AlertType.INFORMATION,"Message",
                        "There is no pending friend request to this person!");
                simpleAlertBuilder.show();
            }
        }
    }

}
