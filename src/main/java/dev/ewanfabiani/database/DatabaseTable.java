package dev.ewanfabiani.database;

import dev.ewanfabiani.exceptions.DatabaseException;

import java.sql.SQLException;

public abstract class DatabaseTable {

    public DatabaseManager databaseManager;

    public DatabaseTable() throws DatabaseException {
        try {
            databaseManager = new DatabaseManager();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to connect to database");
        }
    }

}