package com.example.socialnetworkgui.repository.db;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageDBRepo extends AbstractDBRepository<Integer, Message>{
    public MessageDBRepo(Validator<Message> validator) {
        super(validator);
    }

    @Override
    protected PreparedStatement getFindAllStatement(Connection connection) throws SQLException {
        String query = "SELECT * FROM messages";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement;
    }

    @Override
    protected Set<Message> getEntities(ResultSet resultSet) throws SQLException {
        Set<Message> messages = new HashSet<>();
        while(resultSet.next())
        {
            Integer id = resultSet.getInt("id");
            Integer sentFrom = resultSet.getInt("sent_from");
            Integer sentTo = resultSet.getInt("sent_to");
            String message = resultSet.getString("message");
            LocalDateTime dateSent = resultSet.getTimestamp("date_sent").toLocalDateTime();

            Message messageEntity =new Message(message,sentFrom,sentTo,dateSent);
            messageEntity.setId(id);
            messages.add(messageEntity);
        }
        return messages;
    }

    /**
     * Find messages between 2 users in cronological order
     * @param idSender the sender
     * @param idReceiver the receiver
     * @return messages
     */

    public List<Message> findAllForUser(Integer idSender,Integer idReceiver){
        List<Message> messages = new ArrayList<>();

        String query = "SELECT * FROM messages WHERE sent_from=? and sent_to=? or sent_from=? and sent_to=? ORDER BY date_sent";

        try(Connection connection = jdbcUtils.getConnection();
        PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1,idSender);
            statement.setInt(2,idReceiver);
            statement.setInt(3,idReceiver);
            statement.setInt(4,idSender);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                Integer id = resultSet.getInt("id");
                Integer sentFrom = resultSet.getInt("sent_from");
                Integer sentTo = resultSet.getInt("sent_to");
                String message = resultSet.getString("message");
                LocalDateTime dateSent = resultSet.getTimestamp("date_sent").toLocalDateTime();

                Message messageEntity =new Message(message,sentFrom,sentTo,dateSent);
                messageEntity.setId(id);
                messages.add(messageEntity);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return messages;
    }

    @Override
    protected PreparedStatement getFindOneStatement(Connection connection) throws SQLException {
        return null;
    }

    @Override
    protected Message getEntity(PreparedStatement statement, Integer id) throws SQLException {
        return null;
    }

    @Override
    protected PreparedStatement getSaveStatement(Connection connection) throws SQLException {
        String query = "INSERT INTO messages(sent_from,sent_to,message,date_sent) VALUES (?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        return statement;
    }

    @Override
    protected void saveEntity(PreparedStatement statement, Message entity) throws SQLException {
        statement.setInt(1,entity.getSentFrom());
        statement.setInt(2,entity.getSentTo());
        statement.setString(3,entity.getMessage());
        statement.setTimestamp(4, Timestamp.valueOf(entity.getDateSent()));
    }

    @Override
    protected PreparedStatement getDeleteStatement(Connection connection) throws SQLException {
        return null;
    }

    @Override
    protected void deleteEntity(PreparedStatement statement, Integer id) throws SQLException {

    }

    @Override
    protected PreparedStatement getUpdateStatement(Connection connection) throws SQLException {
        return null;
    }

    @Override
    protected void updateEntity(PreparedStatement statement, Message entity) throws SQLException {

    }
}
