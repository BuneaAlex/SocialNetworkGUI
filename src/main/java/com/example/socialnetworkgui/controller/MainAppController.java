package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.HelloApplication;

import com.example.socialnetworkgui.domain.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

import java.util.Optional;


public class MainAppController extends AbstractController {

    @FXML
    private BorderPane borderPane;
    @FXML
    private MenuBar menuBar;

    private User user;

    public MainAppController(User user) {
        this.user = user;
    }

    @FXML
    public void initialize() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friendsManagement-view.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new FriendsManagementController(user));
        Pane pane = fxmlLoader.load();
        //BorderPane.setAlignment(pane, Pos.CENTER);
        //BorderPane.setMargin(pane, new Insets(12,12,12,12));
        borderPane.setCenter(pane);
    }

    @FXML
    public void signOut(ActionEvent actionEvent) {

        closeWindow(actionEvent);

    }

    @FXML
    public void deleteUser(ActionEvent actionEvent) {

        Stage stage = (Stage) menuBar.getScene().getWindow();
        User user = (User)stage.getUserData();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Message");
        alert.setHeaderText("Are you sure you want to delete your account?");


        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent())
        {
            if(result.get() == ButtonType.OK)
            {
                service.delete(user.getId());
                stage.close();
            }

        }

    }

    @FXML
    public void updateUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("updateAccount-view.fxml"));
        Stage stage = (Stage) menuBar.getScene().getWindow();
        User user = (User)stage.getUserData();
        Pane pane = fxmlLoader.load();
        borderPane.setCenter(pane);
        borderPane.setUserData(user);
    }

    public void friendsMenu(ActionEvent actionEvent) throws IOException {
        //paneSelection("friendsManagement-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friendsManagement-view.fxml"));
        Stage stage = (Stage) menuBar.getScene().getWindow();
        User user = (User) stage.getUserData();
        fxmlLoader.setControllerFactory(controllerClass -> new FriendsManagementController(user));
        Pane pane = fxmlLoader.load();
        borderPane.setCenter(pane);
    }

    public void friendRequestMenu(ActionEvent actionEvent) throws IOException {
        //paneSelection("sendFriendRequest-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sendFriendRequest-view.fxml"));
        Stage stage = (Stage) menuBar.getScene().getWindow();
        User user = (User) stage.getUserData();
        fxmlLoader.setControllerFactory(controllerClass -> new SendFriendRequestController(user));
        Pane pane = fxmlLoader.load();
        borderPane.setCenter(pane);
    }

    public void friendRequestReceivedMenu(ActionEvent actionEvent) throws IOException {
        //paneSelection("receiveFriendRequest-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("receiveFriendRequest-view.fxml"));
        Stage stage = (Stage) menuBar.getScene().getWindow();
        User user = (User) stage.getUserData();
        fxmlLoader.setControllerFactory(controllerClass -> new ReceiveFriendRequestController(user));
        Pane pane = fxmlLoader.load();
        borderPane.setCenter(pane);
    }


}
