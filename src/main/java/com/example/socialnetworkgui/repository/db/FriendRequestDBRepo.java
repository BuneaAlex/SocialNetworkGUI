package com.example.socialnetworkgui.repository.db;

import com.example.socialnetworkgui.domain.FriendRequest;
import com.example.socialnetworkgui.domain.RequestStatus;
import com.example.socialnetworkgui.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class FriendRequestDBRepo extends AbstractDBRepository<Integer, FriendRequest>{
    public FriendRequestDBRepo(Validator<FriendRequest> validator) {
        super(validator);
    }

    @Override
    protected PreparedStatement getFindAllStatement(Connection connection) throws SQLException {
        String query = "SELECT * FROM friend_requests";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement;
    }

    @Override
    protected Set<FriendRequest> getEntities(ResultSet resultSet) throws SQLException {
        Set<FriendRequest> friendRequests = new HashSet<>();
        while(resultSet.next())
        {
            Integer sentFrom = resultSet.getInt("sent_from");
            Integer sentTo = resultSet.getInt("sent_to");
            RequestStatus status = RequestStatus.valueOf(resultSet.getString("status"));
            LocalDateTime dateSent = resultSet.getTimestamp("date_sent").toLocalDateTime();
            Integer id = resultSet.getInt("id");

            FriendRequest friendRequest = new FriendRequest(sentFrom,sentTo,status,dateSent);
            friendRequest.setId(id);

            friendRequests.add(friendRequest);
        }
        resultSet.close();
        return friendRequests;
    }

    @Override
    protected PreparedStatement getFindOneStatement(Connection connection) throws SQLException {
        String query = "SELECT * FROM friend_requests WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement;
    }

    @Override
    protected FriendRequest getEntity(PreparedStatement statement, Integer id) throws SQLException {
        FriendRequest friendRequest = null;
        statement.setInt(1,id);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next())
        {
            Integer sentFrom = resultSet.getInt("sent_from");
            Integer sentTo = resultSet.getInt("sent_to");
            RequestStatus status = RequestStatus.valueOf(resultSet.getString("status"));
            LocalDateTime dateSent = resultSet.getTimestamp("date_sent").toLocalDateTime();

            friendRequest = new FriendRequest(sentFrom,sentTo,status,dateSent);
            friendRequest.setId(id);

        }
        return friendRequest;
    }

    @Override
    protected PreparedStatement getSaveStatement(Connection connection) throws SQLException {
        String query = "INSERT INTO friend_requests(sent_from,sent_to,status,date_sent) VALUES (?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement;
    }

    @Override
    protected void saveEntity(PreparedStatement statement, FriendRequest entity) throws SQLException {
        statement.setInt(1,entity.getSentFrom());
        statement.setInt(2,entity.getSentTo());
        statement.setString(3,entity.getStatus().toString());
        statement.setTimestamp(4,Timestamp.valueOf(entity.getDateSent()));

    }

    @Override
    protected PreparedStatement getDeleteStatement(Connection connection) throws SQLException {
        String query = "DELETE FROM friend_requests WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement;
    }

    @Override
    protected void deleteEntity(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1,id);
    }

    @Override
    protected PreparedStatement getUpdateStatement(Connection connection) throws SQLException {
        String query = "UPDATE friend_requests SET status=? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement;
    }

    @Override
    protected void updateEntity(PreparedStatement statement, FriendRequest entity) throws SQLException {
        statement.setString(1,entity.getStatus().toString());
        statement.setInt(2,entity.getId());
    }

    public Integer findOnePending(Integer sender,Integer receiver)
    {
        String query = "SELECT id FROM friend_requests WHERE sent_from=? and receiver=? and status='PENDING'";

        try(Connection connection = jdbcUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1,sender);
            statement.setInt(2,receiver);
            ResultSet resultSet = statement.executeQuery();
            Integer id = null;
            while(resultSet.next())
            {
                id = resultSet.getInt("id");
            }
            resultSet.close();
            return id;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }


}
