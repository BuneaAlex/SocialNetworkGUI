package com.example.socialnetworkgui.domain.validators;



import com.example.socialnetworkgui.domain.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements Validator<User>{

    /**
     * Validates the user
     * @param entity -serializable entity:user
     * @throws ValidationException -if the user is not valid
     */
    @Override
    public void validate(User entity) throws ValidationException {

        String errors = "";

        Pattern containsLetters = Pattern.compile("[a-z]+", Pattern.CASE_INSENSITIVE);

        Matcher matcherFirstName = containsLetters.matcher(entity.getFirstName());
        boolean matchFoundFirstName = matcherFirstName.find();

        if(!matchFoundFirstName)
            errors += "First name is incorrect\n";

        Matcher matcherLastName = containsLetters.matcher(entity.getLastName());
        boolean matchFoundLastName = matcherLastName.find();


        if(!matchFoundLastName)
            errors += "Last name is incorrect\n";


        if(!errors.equals(""))
            throw new ValidationException(errors);

    }
}
