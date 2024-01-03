package dev.ewanfabiani.database.tables;

import dev.ewanfabiani.api.data.User;
import dev.ewanfabiani.database.DatabaseTable;
import dev.ewanfabiani.exceptions.DatabaseException;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserTable extends DatabaseTable {

    public UserTable() throws DatabaseException {
    }

    public void createUser(String username, String modulus, String exponent) throws DatabaseException {
        String query = "INSERT INTO user (username, modulus, exponent) VALUES (?, ?, ?)";
        try {
            databaseManager.executeUpdate(query, username, modulus, exponent);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to create user");
        }
    }

    public ArrayList<User> getAllUsers() throws DatabaseException {
        String query = "SELECT * FROM user";
        try(var rs = databaseManager.executeQuery(query)) {
            ArrayList<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new User(rs.getString("username"), rs.getString("modulus"), rs.getString("exponent")));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to get all users");
        }
    }

    public User getUser(String username) throws DatabaseException {
        String query = "SELECT * FROM user WHERE username = ?";
        try(var rs = databaseManager.executeQuery(query, username)) {
            if (rs.next()) {
                return new User(rs.getString("username"), rs.getString("modulus"), rs.getString("exponent"));
            } else {
                System.out.println("No user with username " + username);
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to get user");
        }
    }
}
