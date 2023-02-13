package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.RequestStatus;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.UserFriendRequestDTO;
import com.example.socialnetworkgui.utils.SimpleAlertBuilder;
import com.example.socialnetworkgui.utils.events.UserChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;

import java.util.List;
import java.util.ResourceBundle;

public class ReceiveFriendRequestController extends AbstractController implements Initializable, Observer<UserChangeEvent> {

    @FXML
    private Button acceptRequestButton;
    @FXML
    private Button rejectRequestButton;

    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private TableColumn<UserFriendRequestDTO,String> firstNameColumn;
    @FXML
    private TableColumn<UserFriendRequestDTO,String> lastNameColumn;

    @FXML
    private TableColumn<UserFriendRequestDTO, String> requestDateColumn;
    @FXML
    private TableView<UserFriendRequestDTO> usersTableView;
    @FXML
    private ObservableList<UserFriendRequestDTO> friendRequests = FXCollections.observableArrayList();

    private User user;

    public ReceiveFriendRequestController(User user) {
        this.user = user;
        service.addObserver(this);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastName()));
        requestDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDateOfTheRequest()));
        usersTableView.setItems(friendRequests);
        initModel();

    }

    public void initModel()
    {
        List<UserFriendRequestDTO> requestDTOS = service.getUserFriendRequestDTOS(user.getId(),RequestStatus.PENDING);
        friendRequests.setAll(requestDTOS);
        toggleGroup.getToggles().get(0).setSelected(true);
    }

    @Override
    public void update(UserChangeEvent event) {
        initModel();
    }

    public void acceptFriendRequest(ActionEvent actionEvent) {
        int index = usersTableView.getSelectionModel().getSelectedIndex();

        if(index!=-1)
        {
            UserFriendRequestDTO newFriend = usersTableView.getSelectionModel().getSelectedItem();
            service.acceptFriendRequest(newFriend.getId());
            SimpleAlertBuilder simpleAlertBuilder = new SimpleAlertBuilder(Alert.AlertType.INFORMATION,"Message",
                    "Request accepted!");
            simpleAlertBuilder.show();
        }
    }

    public void rejectFriendRequest(ActionEvent actionEvent) {
        int index = usersTableView.getSelectionModel().getSelectedIndex();

        if(index!=-1)
        {
            UserFriendRequestDTO newFriend = usersTableView.getSelectionModel().getSelectedItem();
            service.rejectFriendRequest(newFriend.getId());
            SimpleAlertBuilder simpleAlertBuilder = new SimpleAlertBuilder(Alert.AlertType.INFORMATION,"Message",
                    "Request rejected!");
            simpleAlertBuilder.show();
        }
    }

    public void selectPendingRequests(ActionEvent actionEvent) {
        List<UserFriendRequestDTO> requestDTOS = service.getUserFriendRequestDTOS(user.getId(),RequestStatus.PENDING);
        friendRequests.setAll(requestDTOS);
        acceptRequestButton.setDisable(false);
        rejectRequestButton.setDisable(false);
    }

    public void selectAcceptedRequests(ActionEvent actionEvent) {
        List<UserFriendRequestDTO> requestDTOS = service.getUserFriendRequestDTOS(user.getId(),RequestStatus.ACCEPTED);
        friendRequests.setAll(requestDTOS);
        acceptRequestButton.setDisable(true);
        rejectRequestButton.setDisable(true);
    }

    public void selectRejectedRequests(ActionEvent actionEvent) {
        List<UserFriendRequestDTO> requestDTOS = service.getUserFriendRequestDTOS(user.getId(),RequestStatus.REJECTED);
        friendRequests.setAll(requestDTOS);
        acceptRequestButton.setDisable(true);
        rejectRequestButton.setDisable(true);
    }



}
