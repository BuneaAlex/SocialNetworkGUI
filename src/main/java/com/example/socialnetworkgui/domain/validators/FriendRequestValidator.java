package com.example.socialnetworkgui.domain.validators;

import com.example.socialnetworkgui.domain.FriendRequest;

import java.util.Objects;

public class FriendRequestValidator implements Validator<FriendRequest>{
    @Override
    public void validate(FriendRequest entity) throws ValidationException {
        String error = "";

        if(Objects.equals(entity.getSentFrom(), entity.getSentTo()))
            error+="Cannot send friend request to yourself";

        if(!error.equals(""))
            throw new ValidationException(error);
    }
}
