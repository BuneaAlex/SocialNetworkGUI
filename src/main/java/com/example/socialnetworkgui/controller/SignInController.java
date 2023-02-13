package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.HelloApplication;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.utils.SimpleAlertBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SignInController extends AbstractController{

    @FXML
    private TextField idUserTextField;
    @FXML
    private PasswordField passwordTextField;

    @FXML
    public void goToMainApp(ActionEvent actionEvent) throws IOException {

        if(idUserTextField.getText().isEmpty() || passwordTextField.getText().isEmpty())
        {
            SimpleAlertBuilder simpleAlertBuilder =new SimpleAlertBuilder(Alert.AlertType.WARNING,"Message",
                    "Text fields cannot be empty!");
            simpleAlertBuilder.show();
            return;
        }

        Integer idUser = Integer.valueOf(idUserTextField.getText());
        User user = service.findUser(idUser);

        if(user==null)
        {
            SimpleAlertBuilder simpleAlertBuilder =new SimpleAlertBuilder(Alert.AlertType.WARNING,"Message",
                    "There is no user with this ID!");
            simpleAlertBuilder.show();
        }
        else
        {
            if(Objects.equals(user.getPassword(), passwordTextField.getText()))
            {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainApp-view.fxml"));
                fxmlLoader.setControllerFactory(controllerClass -> new MainAppController(user));
                Parent root = fxmlLoader.load();

                Scene scene = new Scene(root);
                Stage stage = new Stage();

                String title = "User " + user.getId().toString();
                stage.setTitle(title);
                stage.setScene(scene);
                stage.setUserData(user);

                stage.show();
            }
            else {
                SimpleAlertBuilder simpleAlertBuilder = new SimpleAlertBuilder(Alert.AlertType.WARNING, "Message", "Incorrect password!");
                simpleAlertBuilder.show();
            }
        }


    }
}
