package com.example.socialnetworkgui.domain;

import java.util.Objects;

/**
 * A serializable entity which has the ID of type Integer
 */
public class User extends Entity<Integer>{

    private String firstName;
    private String lastName;

    private String password;

    /**
     * Constructor
     *
     * @param firstName -user's first name
     * @param lastName  -user's last name
     * @param password -user's password
     */
    public User(String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" + "ID=" + getId() + ", nume=" + firstName + " " + lastName + "}";
    }

    /**
     * Verify if a user is equal to another user
     * @param o -the user object
     * @return true if the user is equal to o, else return false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) ;
    }

    /**
     * hashcode of the user
     * @return hashcode of the user
     */
    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }
}

