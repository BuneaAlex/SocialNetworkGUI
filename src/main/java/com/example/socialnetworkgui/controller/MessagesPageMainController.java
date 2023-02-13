package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.utils.Pair;
import com.example.socialnetworkgui.utils.events.UserChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MessagesPageMainController extends AbstractController implements Initializable, Observer<UserChangeEvent> {

    private final User sender;
    private final User receiver;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox messagesArea;
    @FXML
    private TextArea messageTextArea;

    public MessagesPageMainController(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        service.addObserver(this);

    }

    private Label messageLabel(String message,String color)
    {
        Label label = new Label();
        label.setText(message);
        label.setWrapText(true);
        // Apply the CSS to the label
        label.setStyle(
                "-fx-background-color:" + color + ";" +
                        "-fx-padding: 10px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-border-color: #ccc; " +
                        "-fx-border-width: 1px; " +
                        "-fx-background-radius: 10px;");

        return label;
    }

    private void placeMessage(Message message)
    {
        HBox messageHArea = new HBox();
        messageHArea.setMaxHeight(Double.MAX_VALUE);
        Pair<String,Integer> rez = buildMultilineMessage(message.getMessage());
        String messageText = rez.first;
        String messageTextWithDate = messageText + "\n" + message.getDateFormatted();
        Integer noRows = rez.second + 1;
        messageHArea.setMinHeight(38*noRows);
        if(Objects.equals(message.getSentFrom(), sender.getId()))
        {
            Label label = messageLabel(messageTextWithDate,"#90EE90");
            messageHArea.getChildren().setAll(label);
            messageHArea.setAlignment(Pos.CENTER_RIGHT);
        }
        else
        {
            Label label = messageLabel(messageTextWithDate,"white");
            messageHArea.getChildren().setAll(label);
            messageHArea.setAlignment(Pos.CENTER_LEFT);
        }

        messagesArea.getChildren().add(messageHArea);

    }

    private Pair<String,Integer> buildMultilineMessage(String message)
    {
        int characterCount = 20;
        if(message.length() > characterCount)
        {
            List<String> words = List.of(message.split(" "));
            StringBuilder newMessage = new StringBuilder();
            int ct = 0;
            Integer noRows = 1;
            for(String word:words)
            {
                if(ct<=characterCount)
                {
                    newMessage.append(word).append(" ");
                    ct+=word.length();
                }

                else
                {
                    newMessage.append("\n").append(word).append(" ");
                    ct=word.length();
                    noRows++;
                }

            }

            return new Pair<>(newMessage.toString(),noRows);
        }


        return new Pair<>(message,1);
    }

    private void initModel()
    {
        List<Message> messages = service.getMessagesBetweenUsers(sender.getId(),receiver.getId());
        messagesArea.getChildren().clear();
        for(Message message:messages)
        {
            placeMessage(message);
        }
    }

    public void sendMessage(ActionEvent actionEvent) {
        String text = messageTextArea.getText();
        if(!Objects.equals(text, ""))
        {
            service.sendMessage(sender.getId(),receiver.getId(),text);
            messageTextArea.clear();
        }
    }


    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initModel();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initModel();
        messagesArea.heightProperty().addListener(observable -> scrollPane.setVvalue(1D));
    }
}
