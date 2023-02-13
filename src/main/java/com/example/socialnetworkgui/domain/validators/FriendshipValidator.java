package com.example.socialnetworkgui.domain.validators;


import com.example.socialnetworkgui.domain.Friendship;

import java.util.Objects;

public class FriendshipValidator implements Validator<Friendship>{
    @Override
    public void validate(Friendship entity) throws ValidationException {
        String errors = "";

        if(Objects.equals(entity.getId().first, entity.getId().second))
            errors+="User cannot be friends with himself";

        if(entity.getId().first > entity.getId().second)
            errors+="IDs must be in ascending order!";

        if(!errors.equals(""))
            throw new ValidationException(errors);
    }
}
