package com.example.socialnetworkgui.repository.file;



import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validators.Validator;

import java.util.List;

public class UserFile extends AbstractFileRepo<Integer, User>{


    public UserFile(Validator<User> validator, String filename) {
        super(validator, filename);
    }

    @Override
    public User extractEntity(List<String> attributes) {
        Integer id = Integer.valueOf(attributes.get(0));
        String firstName = attributes.get(1);
        String lastName = attributes.get(2);
        String password = attributes.get(3);

        User user = new User(firstName,lastName, password);
        user.setId(id);
        return user;
    }

    @Override
    protected String createEntityAsString(User entity) {
        String id = entity.getId().toString();
        String firstName = entity.getFirstName();
        String lastName = entity.getLastName();
        return id + "," + firstName + "," + lastName;
    }
}
