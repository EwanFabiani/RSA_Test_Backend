package dev.ewanfabiani.database.tables;

import dev.ewanfabiani.database.DatabaseTable;
import dev.ewanfabiani.exceptions.DatabaseException;

import java.sql.ResultSet;

public class AuthTable extends DatabaseTable {

    public AuthTable() throws DatabaseException {
    }

    public void createToken(String username, String token) throws DatabaseException {
        String query = "INSERT INTO auth (username, token) VALUES (?, ?)";
        try {
            System.out.println("Creating token for " + username);
            databaseManager.executeUpdate(query, username, token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to create token");
        }
    }

    public String getToken(String username) throws DatabaseException {
        String query = "SELECT token FROM auth WHERE username = ?";
        try(ResultSet rs = databaseManager.executeQuery(query, username)) {
            if (rs.next()) {
                return rs.getString("token");
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to get token");
        }
    }

    public void deleteToken(String username) throws DatabaseException {
        String query = "DELETE FROM auth WHERE username = ?";
        try {
            databaseManager.executeUpdate(query, username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to delete token");
        }
    }

}
