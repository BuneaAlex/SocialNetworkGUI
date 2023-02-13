package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeMenuController {

    @FXML
    private Button signInButton;
    @FXML
    private Button signUpButton;

    @FXML
    private BorderPane welcomePagePane;

    @FXML
    private Label welcomeLabel;

    @FXML
    public void goToSignInMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("signIn-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("SIGN IN");
        stage.setScene(scene);
        stage.show();
    }

    public void goToSignUpMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("signUp-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("SIGN UP");
        stage.setScene(scene);
        stage.show();
    }
}
