package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validators.FriendshipValidator;
import com.example.socialnetworkgui.domain.validators.UserValidator;
import com.example.socialnetworkgui.repository.Repository;
import com.example.socialnetworkgui.repository.db.FriendshipDBRepo;
import com.example.socialnetworkgui.repository.db.UserDBRepo;
import com.example.socialnetworkgui.service.UserService;
import com.example.socialnetworkgui.utils.SimpleAlertBuilder;
import com.example.socialnetworkgui.utils.Pair;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignUpController extends AbstractController{

    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private PasswordField passwordTextField;

    public void registerNewUser(ActionEvent actionEvent) {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String password = passwordTextField.getText();

        User user = new User(firstName,lastName, password);

        try
        {
            service.save(user);
            String userId = user.getId().toString();
            SimpleAlertBuilder simpleAlertBuilder = new SimpleAlertBuilder(Alert.AlertType.INFORMATION, "Message",
                    "Sign up complete! Your ID is " + userId);
            simpleAlertBuilder.show();

        }
        catch (Exception e)
        {
            SimpleAlertBuilder simpleAlertBuilder = new SimpleAlertBuilder(Alert.AlertType.WARNING, "Message", e.getMessage());
            simpleAlertBuilder.show();
        }

    }
}
