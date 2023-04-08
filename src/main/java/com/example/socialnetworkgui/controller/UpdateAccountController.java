package com.example.socialnetworkgui.controller;


import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.utils.SimpleAlertBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class UpdateAccountController extends AbstractController{
    @FXML
    private TextField newFirstNameTextField;
    @FXML
    private TextField newLastNameTextField;
    @FXML
    private PasswordField oldPasswordTextField;
    @FXML
    private PasswordField newPasswordTextField;

    @FXML
    public void updateUser(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        User user = (User)stage.getUserData();

        String newFirstName = newFirstNameTextField.getText();
        String newLastName = newLastNameTextField.getText();
        String oldPassword = oldPasswordTextField.getText();
        String newPassword = newPasswordTextField.getText();

        if(Objects.equals(user.getPassword(), oldPassword))
        {
            User newUser = new User(newFirstName,newLastName,newPassword);
            newUser.setId(user.getId());
            try
            {
                service.update(newUser);
                SimpleAlertBuilder simpleAlertBuilder = new SimpleAlertBuilder(Alert.AlertType.INFORMATION,"Message",
                        "Update successful!");
                simpleAlertBuilder.show();
            }catch (Exception e)
            {
                SimpleAlertBuilder simpleAlertBuilder = new SimpleAlertBuilder(Alert.AlertType.INFORMATION,"Message",
                        e.getMessage());
                simpleAlertBuilder.show();
            }

        }
        else {
            SimpleAlertBuilder simpleAlertBuilder = new SimpleAlertBuilder(Alert.AlertType.WARNING,"Message",
                    "Incorrect old password!");
            simpleAlertBuilder.show();
        }


    }
}
