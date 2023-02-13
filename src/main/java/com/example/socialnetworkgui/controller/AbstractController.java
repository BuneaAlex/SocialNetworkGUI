package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.FriendRequest;
import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validators.FriendRequestValidator;
import com.example.socialnetworkgui.domain.validators.FriendshipValidator;
import com.example.socialnetworkgui.domain.validators.UserValidator;
import com.example.socialnetworkgui.repository.Repository;
import com.example.socialnetworkgui.repository.db.FriendRequestDBRepo;
import com.example.socialnetworkgui.repository.db.FriendshipDBRepo;
import com.example.socialnetworkgui.repository.db.UserDBRepo;
import com.example.socialnetworkgui.service.UserService;
import com.example.socialnetworkgui.utils.Pair;
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
