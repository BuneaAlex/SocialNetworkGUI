package com.example.socialnetworkgui.domain;

import com.example.socialnetworkgui.utils.Constants;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserFriendRequestDTO extends Entity<Integer>{

    private String firstName;

    private String lastName;

    private LocalDateTime dateOfTheRequest;

    public UserFriendRequestDTO(String firstName, String lastName, LocalDateTime dateOfTheRequest) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfTheRequest = dateOfTheRequest;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfTheRequest() {
        return dateOfTheRequest.format(Constants.DATE_FORMAT);
    }

    public void setDateOfTheRequest(LocalDateTime dateOfTheRequest) {
        this.dateOfTheRequest = dateOfTheRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFriendRequestDTO that = (UserFriendRequestDTO) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(dateOfTheRequest, that.dateOfTheRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, dateOfTheRequest);
    }

    @Override
    public String toString() {
        return "UserFriendRequestDTO{ " + getId() +
                " firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfTheRequest=" + dateOfTheRequest +
                '}';
    }
}
