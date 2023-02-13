package com.example.socialnetworkgui.domain;

import com.example.socialnetworkgui.utils.Constants;

import java.time.LocalDateTime;


public class Message extends Entity<Integer>{
    private String message;
    private Integer sentFrom;
    private Integer sentTo;
    private LocalDateTime dateSent;

    public Message(String message, Integer sentFrom, Integer sentTo, LocalDateTime dateSent) {
        this.message = message;
        this.sentFrom = sentFrom;
        this.sentTo = sentTo;
        this.dateSent = dateSent;
    }

    public String getMessage() {
        return message;
    }

    public Integer getSentFrom() {
        return sentFrom;
    }

    public Integer getSentTo() {
        return sentTo;
    }

    public LocalDateTime getDateSent() {
        return dateSent;
    }

    public String getDateFormatted()
    {
        return dateSent.format(Constants.DATE_FORMAT);
    }

    @Override
    public String toString() {

        return "message=" + message + '|' +
                "from=" + sentFrom + '|' +
                "to=" + sentTo + '|' +
                "date=" + getDateFormatted();
    }
}
