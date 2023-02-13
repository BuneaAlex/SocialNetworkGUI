package com.example.socialnetworkgui.domain;

import com.example.socialnetworkgui.utils.Constants;

import java.time.LocalDateTime;
import java.util.Objects;

public class FriendRequest extends Entity<Integer>{

    private Integer sentFrom;
    private Integer sentTo;
    private RequestStatus status;
    private LocalDateTime dateSent;

    public FriendRequest(Integer sentFrom, Integer sentTo, RequestStatus status, LocalDateTime dateSent) {

        this.sentFrom = sentFrom;
        this.sentTo = sentTo;
        this.status = status;
        this.dateSent = dateSent;
    }

    public Integer getSentFrom() {
        return sentFrom;
    }

    public Integer getSentTo() {
        return sentTo;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public LocalDateTime getDateSent() {
        return dateSent;
    }

    public String getDateSentFormatted()
    {
        return dateSent.format(Constants.DATE_FORMAT);
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequest that = (FriendRequest) o;
        return Objects.equals(sentFrom, that.sentFrom) && Objects.equals(sentTo, that.sentTo) && status == that.status && Objects.equals(dateSent, that.dateSent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sentFrom, sentTo, status, dateSent);
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "sentFrom=" + sentFrom +
                ", sentTo=" + sentTo +
                ", status=" + status +
                ", dateSent=" + getDateSentFormatted() +
                '}';
    }
}


