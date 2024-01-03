package dev.ewanfabiani.database.tables;

import dev.ewanfabiani.api.data.EncryptedMessage;
import dev.ewanfabiani.api.data.FullEncryptedMessage;
import dev.ewanfabiani.database.DatabaseTable;
import dev.ewanfabiani.exceptions.DatabaseException;

import java.sql.ResultSet;
import java.util.ArrayList;

public class MessageTable extends DatabaseTable {

    public MessageTable() throws DatabaseException {
    }

    public void storeMessage(String message, String sender, String receiver, String signature) throws DatabaseException {
        String query = "INSERT INTO message (message, sender, receiver, signature) VALUES (?, ?, ?, ?)";
        try {
            databaseManager.executeUpdate(query, message, sender, receiver, signature);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to store message");
        }
    }

    public ArrayList<FullEncryptedMessage> getUndeliveredMessages(String sender, String receiver) throws DatabaseException {
        String query = "SELECT * FROM message WHERE sender = ? AND receiver = ? AND status = 'UNDELIVERED'";
        try(ResultSet rs = databaseManager.executeQuery(query, sender, receiver)) {
            ArrayList<FullEncryptedMessage> messages = new ArrayList<>();
            while (rs.next()) {
                FullEncryptedMessage fullEncryptedMessage = new FullEncryptedMessage(
                        rs.getString("message"),
                        rs.getString("signature"),
                        rs.getString("sender"),
                        rs.getString("receiver"),
                        rs.getTimestamp("timestamp").getTime());
                messages.add(fullEncryptedMessage);
            }
            return messages;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to get chat");
        }
    }

    public ArrayList<FullEncryptedMessage> getAllUndeliveredMessages(String receiver) throws DatabaseException {
        String query = "SELECT * FROM message WHERE receiver = ? AND status = 'UNDELIVERED'";
        try(ResultSet rs = databaseManager.executeQuery(query, receiver)) {
            ArrayList<FullEncryptedMessage> messages = new ArrayList<>();
                while (rs.next()) {
                FullEncryptedMessage fullEncryptedMessage = new FullEncryptedMessage(
                        rs.getString("message"),
                        rs.getString("signature"),
                        rs.getString("sender"),
                        rs.getString("receiver"),
                        rs.getTimestamp("timestamp").getTime());
                messages.add(fullEncryptedMessage);
            }
            return messages;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to get chat");
        }
    }

    public void markMessagesAsDelivered(String sender, String receiver) throws DatabaseException {
        String query = "UPDATE message SET status = 'DELIVERED' WHERE sender = ? AND receiver = ?";
        try {
            databaseManager.executeUpdate(query, sender, receiver);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to mark message as delivered");
        }
    }

    public void markAllMessagesAsDelivered(String receiver) throws DatabaseException {
        String query = "UPDATE message SET status = 'DELIVERED' WHERE receiver = ?";
        try {
            databaseManager.executeUpdate(query, receiver);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to mark message as delivered");
        }
    }

}
