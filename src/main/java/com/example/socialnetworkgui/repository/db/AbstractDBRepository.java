package com.example.socialnetworkgui.repository.db;


import com.example.socialnetworkgui.domain.Entity;
import com.example.socialnetworkgui.domain.validators.Validator;
import com.example.socialnetworkgui.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;


public abstract class AbstractDBRepository<ID,E extends Entity<ID>> implements Repository<ID,E> {

    protected final JDBCUtils jdbcUtils = new JDBCUtils();

    private Validator<E> validator;

    public AbstractDBRepository(Validator<E> validator) {
        this.validator = validator;
    }

    protected abstract PreparedStatement getFindAllStatement(Connection connection) throws SQLException;
    protected abstract Set<E> getEntities(ResultSet resultSet) throws SQLException;
    protected abstract PreparedStatement getFindOneStatement(Connection connection) throws SQLException;
    protected abstract E getEntity(PreparedStatement statement,ID id) throws SQLException;
    protected abstract PreparedStatement getSaveStatement(Connection connection) throws SQLException;
    protected abstract void saveEntity(PreparedStatement statement,E entity) throws SQLException;

    protected abstract PreparedStatement getDeleteStatement(Connection connection) throws SQLException;
    protected abstract void deleteEntity(PreparedStatement statement,ID id) throws SQLException;

    protected abstract PreparedStatement getUpdateStatement(Connection connection) throws SQLException;
    protected abstract void updateEntity(PreparedStatement statement,E entity) throws SQLException;

    @Override
    public E findOne(ID id) {

        E entity = null;

        try(PreparedStatement statement = getFindOneStatement(jdbcUtils.getConnection()))
        {
            entity = getEntity(statement,id);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return entity;
    }



    @Override
    public Iterable<E> findAll() {

        Set<E> entities = new HashSet<>();

        try(PreparedStatement statement = getFindAllStatement(jdbcUtils.getConnection());
            ResultSet resultSet = statement.executeQuery())
        {
            entities = getEntities(resultSet);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return entities;
    }


    @Override
    public E save(E entity) {
        validator.validate(entity);

        try(PreparedStatement statement = getSaveStatement(jdbcUtils.getConnection()))
        {
            saveEntity(statement,entity);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public E delete(ID id) {

        try(PreparedStatement statement = getDeleteStatement(jdbcUtils.getConnection()))
        {
            deleteEntity(statement,id);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public E update(E entity) {

        validator.validate(entity);

        try(PreparedStatement statement = getUpdateStatement(jdbcUtils.getConnection()))
        {
            updateEntity(statement,entity);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return null;
    }
}
