package com.example.socialnetworkgui.repository.db;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.validators.Validator;
import com.example.socialnetworkgui.utils.Pair;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class FriendshipDBRepo extends AbstractDBRepository<Pair<Integer,Integer>, Friendship>{
    public FriendshipDBRepo(Validator<Friendship> validator) {
        super(validator);
    }


    @Override
    protected PreparedStatement getFindAllStatement(Connection connection) throws SQLException {
        String query = "SELECT * FROM friendships";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement;
    }

    @Override
    protected Set<Friendship> getEntities(ResultSet resultSet) throws SQLException {
        Set<Friendship> friendships = new HashSet<>();

        {
            while(resultSet.next())
            {
                Integer id1 = resultSet.getInt("id_user1");
                Integer id2 = resultSet.getInt("id_user2");
                LocalDateTime date = resultSet.getTimestamp("friends_from").toLocalDateTime();

                Friendship friendship = new Friendship(id1,id2,date);
                friendships.add(friendship);
            }
        }
        return friendships;
    }

    @Override
    protected PreparedStatement getFindOneStatement(Connection connection) throws SQLException {
        String query = "SELECT * FROM friendships WHERE id_user1=? and id_user2=?";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement;
    }

    @Override
    protected Friendship getEntity(PreparedStatement statement, Pair<Integer, Integer> friendship_id) throws SQLException {

        Friendship friendship = null;

        statement.setInt(1,friendship_id.first);
        statement.setInt(2,friendship_id.second);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Integer id1 = resultSet.getInt("id_user1");
            Integer id2 = resultSet.getInt("id_user2");
            LocalDateTime date = resultSet.getTimestamp("friends_from").toLocalDateTime();

            friendship = new Friendship(id1,id2,date);
        }
        resultSet.close();
        return friendship;
    }

    @Override
    protected PreparedStatement getSaveStatement(Connection connection) throws SQLException {
        String query = "INSERT INTO friendships(id_user1,id_user2,friends_from) VALUES(?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement;
    }

    @Override
    protected void saveEntity(PreparedStatement statement, Friendship entity) throws SQLException {
        statement.setInt(1,entity.getId().first);
        statement.setInt(2,entity.getId().second);
        Timestamp date = Timestamp.valueOf(entity.getFriendsFrom());
        statement.setTimestamp(3,date);


    }


    @Override
    protected PreparedStatement getDeleteStatement(Connection connection) throws SQLException {
        String query = "DELETE FROM friendships WHERE id_user1=? and id_user2=?";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement;
    }

    @Override
    protected void deleteEntity(PreparedStatement statement, Pair<Integer, Integer> friendship_id) throws SQLException {

        statement.setInt(1,friendship_id.first);
        statement.setInt(2,friendship_id.second);

    }


    @Override
    protected PreparedStatement getUpdateStatement(Connection connection) throws SQLException {
        String query = "UPDATE friendships SET friends_from=? WHERE id_user1=? and id_user2=?";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement;
    }

    @Override
    protected void updateEntity(PreparedStatement statement, Friendship entity) throws SQLException {

        statement.setTimestamp(1,Timestamp.valueOf(entity.getFriendsFrom()));
        statement.setInt(2,entity.getId().first);
        statement.setInt(3,entity.getId().second);

    }
}
