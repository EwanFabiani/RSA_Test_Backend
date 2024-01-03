package dev.ewanfabiani.database;

import org.apache.commons.dbutils.DbUtils;

import java.sql.*;

public class DatabaseManager {

    private boolean transactionActive = false;

    private Connection connection;

    public DatabaseManager() throws SQLException {
        DriverManager.registerDriver(new org.mariadb.jdbc.Driver());
        //DatabaseCredentialService credentials = new DatabaseCredentialService();
        connection = DriverManager.getConnection("jdbc:mariadb://45.84.196.211:3306/rsa_test", "root", "43OL8pLuhL?o");
        //TODO: add credentials
    }

    public ResultSet executeQuery(String query, String... params) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                ps.setString(i + 1, params[i]);
            }
            return ps.executeQuery();
        }catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (Object o : params) {
                if (o instanceof String) {
                    ps.setString(ps.getParameterMetaData().getParameterCount(), (String) o);
                }else if (o instanceof Integer) {
                    ps.setInt(ps.getParameterMetaData().getParameterCount(), (Integer) o);
                } else {
                    throw new IllegalArgumentException("Invalid parameter type");
                }
            }
            return ps.executeQuery();
        }catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public int executeUpdate(String query, String... params) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                ps.setString(i + 1, params[i]);
            }
            return ps.executeUpdate();
        }catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public int executeUpdate(String query, Object... params) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (Object o : params) {
                if (o instanceof String) {
                    ps.setString(ps.getParameterMetaData().getParameterCount(), (String) o);
                }else if (o instanceof Integer) {
                    ps.setInt(ps.getParameterMetaData().getParameterCount(), (Integer) o);
                }else if (o instanceof Long) {
                    ps.setLong(ps.getParameterMetaData().getParameterCount(), (Long) o);
                } else {
                    throw new IllegalArgumentException("Invalid parameter type");
                }
            }
            return ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    public void beginTransaction() throws SQLException {
        if (transactionActive) {
            throw new IllegalStateException("Transaction already active");
        }
        connection.setAutoCommit(false);
        transactionActive = true;
    }

    public void commitTransaction() throws SQLException {
        if (!transactionActive) {
            throw new IllegalStateException("No transaction active");
        }
        connection.commit();
        connection.setAutoCommit(true);
        transactionActive = false;
    }

    public void rollbackTransaction() throws SQLException {
        if (!transactionActive) {
            throw new IllegalStateException("No transaction active");
        }
        connection.rollback();
        connection.setAutoCommit(true);
        transactionActive = false;
    }

    public boolean isTransactionActive() {
        return transactionActive;
    }

    public void close() {
        DbUtils.closeQuietly(connection);
    }

}