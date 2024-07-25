package dev.pilati.pilatihomes.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dev.pilati.pilatihomes.PilatiHomes;
import dev.pilati.pilatihomes.database.Datasource;

public abstract class Repository<P, M> {

    protected Datasource datasource = PilatiHomes.getInstance().getDatasource();

    protected abstract M find(P id) throws SQLException;

    protected abstract void save(M model) throws SQLException;

    protected abstract void delete(M model) throws SQLException;

    protected Object executeQuery(
        String sql,
        RepositoryFunction<PreparedStatement, Void> setParameters,
        RepositoryFunction<ResultSet, Object> handleData
    ) throws SQLException {
    
        try (Connection connection = datasource.getConnection()) {
            
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                setParameters.apply(statement);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return handleData.apply(resultSet);
                }
            }
        }
    }

    protected void executeUpdate(
        String sql,
        RepositoryFunction<PreparedStatement, Void> setParameters
    ) throws SQLException {
        
        try (Connection connection = datasource.getConnection()) {
            
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                setParameters.apply(statement);
                statement.executeUpdate();
            }
        }
    }

    protected P executeInsert(
        String sql,
        RepositoryFunction<PreparedStatement, Void> setParameters,
        RepositoryFunction<ResultSet, P> handleGetKey
    ) throws SQLException {
        
        try (Connection connection = datasource.getConnection()) {
            
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                setParameters.apply(statement);
                
                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) { 
                    return null;
                }

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return handleGetKey.apply(resultSet);
                    }
                }
            }
        }
        return null;
    }

    @FunctionalInterface
    protected interface RepositoryFunction<T, R> {
        R apply(T t) throws SQLException;
    }
}
