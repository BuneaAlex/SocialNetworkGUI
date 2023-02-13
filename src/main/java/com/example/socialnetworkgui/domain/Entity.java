package com.example.socialnetworkgui.domain;


import java.io.Serializable;

/**
 * Serializable entity
 * @param <ID> -each entity must have an attribute of type ID
 */

public class Entity<ID> implements Serializable {

    private static final long serialVersionUID = 7331115341259248461L;
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
