package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.utils.events.UserChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MessagesPageController extends AbstractController implements Initializable, Observer<UserChangeEvent> {

    private final User sender;
    private final User receiver;
    @FXML
    private TextField messageTextField;

    @FXML
    private VBox receiverMessagesBox;

    @FXML
    private VBox senderMessagesBox;

    private final int msgLen = 23;


    public MessagesPageController(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        service.addObserver(this);
    }

    private Label messageLabel(String message)
    {
        Label label = new Label(message);
        label.setWrapText(true);
        // Apply the CSS to the label
        label.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-border-radius: 2 2 0 2;" +
                        "-fx-border-color: #dddddd;" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, #cccccc, 8, 0.5, 0, 0);" +
                        "-fx-padding: 3;" +
                        "-fx-font-size: 12;" +
                        "-fx-text-fill: #000000;" +
                        "-fx-alignment: left");

        return label;
    }

    private void placeMessage(Message message)
    {
        Label label = messageLabel(message.getMessage());
        Region region = new Region();
        int size = message.getMessage().length()/msgLen + 1;

        if(Objects.equals(message.getSentFrom(), sender.getId()))
        {

            senderMessagesBox.getChildren().add(label);
            region.setPrefSize(40,25*size);
            receiverMessagesBox.getChildren().add(region);
        }
        else
        {
            receiverMessagesBox.getChildren().add(label);
            region.setPrefSize(40,25*size);
            senderMessagesBox.getChildren().add(region);
        }

    }

    private void initModel()
    {
        List<Message> messages = service.getMessagesBetweenUsers(sender.getId(),receiver.getId());
        senderMessagesBox.getChildren().clear();
        receiverMessagesBox.getChildren().clear();
        for(Message message:messages)
        {
            placeMessage(message);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initModel();
    }

    public void sendMessage(ActionEvent actionEvent) {
        String text = messageTextField.getText();
        if(!Objects.equals(text, ""))
        {
            service.sendMessage(sender.getId(),receiver.getId(),text);
            messageTextField.clear();
        }
    }

    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initModel();
    }
}
