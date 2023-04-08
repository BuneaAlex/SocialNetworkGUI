package com.example.socialnetworkgui.controller;


import com.example.socialnetworkgui.service.UserService;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public abstract class AbstractController{

    protected UserService service = UserService.getInstance();

    protected void closeWindow(ActionEvent actionEvent)
    {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
