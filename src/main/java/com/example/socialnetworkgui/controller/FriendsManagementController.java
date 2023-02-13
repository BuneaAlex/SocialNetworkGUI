package com.example.socialnetworkgui.controller;
import com.example.socialnetworkgui.HelloApplication;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.utils.SimpleAlertBuilder;
import com.example.socialnetworkgui.utils.events.UserChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FriendsManagementController extends AbstractController implements Observer<UserChangeEvent>, Initializable {
    @FXML
    private TableColumn<User,String> firstNameColumn;
    @FXML
    private TableColumn<User,String> lastNameColumn;
    @FXML
    private TableView<User> friendsTableView;

    @FXML
    private ObservableList<User> model = FXCollections.observableArrayList();
    @FXML
    public AnchorPane anchorPane;

    private final User user;

    public FriendsManagementController(User user) {
        this.user = user;
        service.addObserver(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //friendsTableView.getSelectionModel().setCellSelectionEnabled(true); -> o singura celula
        /*
        friendsTableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        */

        firstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastName()));
        friendsTableView.setItems(model);
        initModel();
    }

    private void initModel() {

        List<User> friends = service.userFriends(user.getId());
        model.setAll(friends);
    }

    public void removeFriend(ActionEvent actionEvent) {
        int index = friendsTableView.getSelectionModel().getSelectedIndex();
        if(index!=-1)
        {
            User selectedUser = friendsTableView.getSelectionModel().getSelectedItem();
            service.removeFriend(user.getId(),selectedUser.getId());
            SimpleAlertBuilder simpleAlertBuilder = new SimpleAlertBuilder(Alert.AlertType.INFORMATION,"Message",
                    "Friend removed successfully!");
            simpleAlertBuilder.show();
        }

    }

    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initModel();
    }


    public void sendMessage(ActionEvent actionEvent) throws IOException {

        int index = friendsTableView.getSelectionModel().getSelectedIndex();
        if(index!=-1) {
            User selectedUser = friendsTableView.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("messagesPage-view2.fxml"));
            fxmlLoader.setControllerFactory(controllerClass -> new MessagesPageMainController(user,selectedUser));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load(),600,400);
            String userName = selectedUser.getFirstName() + " " +selectedUser.getLastName();
            stage.setTitle(userName);
            stage.setScene(scene);
            stage.show();
        }
    }
}
