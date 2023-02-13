package com.example.socialnetworkgui.repository.db;



import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validators.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class UserDBRepo extends AbstractDBRepository<Integer, User>{

    public UserDBRepo(Validator<User> validator) {
        super(validator);
    }



    @Override
    protected PreparedStatement getFindOneStatement(Connection connection) throws SQLException {
        String query = "SELECT * FROM users WHERE id_user=?";

        PreparedStatement statement = connection.prepareStatement(query);

        return statement;
    }

    @Override
    protected User getEntity(PreparedStatement statement,Integer id) throws SQLException {

        User user = null;
        statement.setInt(1,id);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next())
        {
            String first_name = resultSet.getString("first_name");
            String last_name = resultSet.getString("last_name");
            String password = resultSet.getString("password");

            user = new User(first_name,last_name, password);
            user.setId(id);
        }
        resultSet.close();

        return user;
    }

    @Override
    protected PreparedStatement getFindAllStatement(Connection connection) throws SQLException {
        String query = "SELECT * FROM users";

        PreparedStatement statement = connection.prepareStatement(query);

        return statement;
    }
    @Override
    protected Set<User> getEntities(ResultSet resultSet) throws SQLException {
        Set<User> users = new HashSet<>();

        while (resultSet.next()) {
            Integer id = resultSet.getInt("id_user");
            String first_name = resultSet.getString("first_name");
            String last_name = resultSet.getString("last_name");
            String password = resultSet.getString("password");

            User user = new User(first_name,last_name, password);
            user.setId(id);

            users.add(user);
        }

        return users;
    }

    @Override
    protected PreparedStatement getSaveStatement(Connection connection) throws SQLException {

        String query = "INSERT INTO users(id_user,first_name,last_name,password) VALUES(?,?,?,?)";

        PreparedStatement statement = connection.prepareStatement(query);

        return statement;
    }

    @Override
    protected void saveEntity(PreparedStatement statement, User entity) throws SQLException {
        statement.setInt(1,entity.getId());
        statement.setString(2,entity.getFirstName());
        statement.setString(3,entity.getLastName());
        statement.setString(4,entity.getPassword());

    }

    @Override
    protected PreparedStatement getDeleteStatement(Connection connection) throws SQLException {
        String query = "DELETE FROM users WHERE id_user = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        return statement;
    }

    @Override
    protected void deleteEntity(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected PreparedStatement getUpdateStatement(Connection connection) throws SQLException {

        String query = "UPDATE users SET first_name=?,last_name=?,password=? WHERE id_user = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement;
    }

    @Override
    protected void updateEntity(PreparedStatement statement, User entity) throws SQLException {
        statement.setString(1,entity.getFirstName());
        statement.setString(2,entity.getLastName());
        statement.setString(3,entity.getPassword());
        statement.setInt(4, entity.getId());

    }


}
