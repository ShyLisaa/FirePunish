package dev.shylisaa.firepunish.api.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.shylisaa.firepunish.api.database.type.DatabaseType;
import net.md_5.bungee.api.ProxyServer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Optional;

public class DatabaseExecutor {

    private final String host, user, password, database;
    private final int port;
    private Connection connection;


    public DatabaseExecutor(String host, int port, String user, String password, String database) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s", this.host, this.port, this.database));
        config.setUsername(this.user);
        config.setPassword(this.password);

        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("useServerPrepStmts", true);
        config.addDataSourceProperty("useLocalSessionState", true);
        config.addDataSourceProperty("rewriteBatchedStatements", true);
        config.addDataSourceProperty("cacheResultSetMetadata", true);
        config.addDataSourceProperty("cacheServerConfiguration", true);

        try {
            HikariDataSource hikariDataSource = new HikariDataSource(config);
            this.connection = hikariDataSource.getConnection();
            ProxyServer.getInstance().getLogger().info("MySQL connection has been successful.");
        } catch (SQLException exception) {
            ProxyServer.getInstance().getLogger().severe("MySQL connection was not successful.");
            exception.printStackTrace();
        }
    }

    public void sendKeepAlive() {
        try {
            this.connection.isValid(0);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void close() throws SQLException {
        this.connection.close();
    }

    public void executeUpdate(String query) {
        try (Statement preparedStatement = this.connection.createStatement()) {
            preparedStatement.executeUpdate(query);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public boolean existsInTable(String table, String databaseKey, String key, String value) {
        try (Statement statement = this.connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT " + databaseKey + " FROM " + table + " WHERE " + key + "='" + value + "'");
            System.out.println(resultSet.next());
            return resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean existsInTable(String table, String key, String value) {
        try (Statement statement = this.connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE " + key + "='" + value + "'");
            return resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public void addMoreInTable(String table, String[] types, Object[] list) {
        StringBuilder upload = new StringBuilder("INSERT INTO " + table + "(" + types[0]);
        for (int i = 1; i < types.length; i++) upload.append(", ").append(types[i]);
        upload.append(") VALUES ('").append(list[0]).append("'");
        for (int i = 1; i < list.length; i++) {
            if (!list[i].equals("NULL")) {
                upload.append(", '").append(list[i]).append("'");
            } else {
                upload.append(", NULL");
            }
        }
        upload.append(");");
        executeUpdate(upload.toString());
    }

    public void updateInTable(String table, String setKey, String setValue, String keyRow, String keyValue) {
        try (Statement statement = this.connection.createStatement()) {
            if(!setValue.equals("NULL")) {
                statement.executeUpdate("UPDATE " + table + " SET " + setKey + "= '" + setValue + "' WHERE " + keyRow + "= '" + keyValue + "';");
            } else {
                statement.executeUpdate("UPDATE " + table + " SET " + setKey + "= NULL WHERE " + keyRow + "= '" + keyValue + "';");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public int getTablePosition(String table, String type, String key, String searchBY) {
        try (Statement statement = this.connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM %s ORDER BY %s DESC", table, searchBY));
            while (resultSet.next()) {
                if (resultSet.getString(type).equalsIgnoreCase(key)) {
                    return resultSet.getRow();
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return -1;
        }
        return -1;
    }

    public Object getFromTable(String table, String key, String value, String result) {
        try (Statement statement = this.connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM %s WHERE %s = '%s'", table, key, value));
            if (resultSet.next()) {
                return resultSet.getObject(result);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
        return null;
    }

    public Object getFromTable(String databaseKey, String table, String key, String value, String result) {
        try (Statement statement = this.connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format("SELECT %s FROM %s WHERE %s = '%s'", databaseKey, table, key, value));
            if (resultSet.next()) {
                return resultSet.getObject(result);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
        return null;
    }

    public void createTable(String table, LinkedHashMap<String, DatabaseType> content) {
        StringBuilder update = new StringBuilder("CREATE TABLE IF NOT EXISTS `").append(table).append("` (`");
        int count = 0;
        for (String key : content.keySet()) {
            update.append(key).append("` ").append(content.get(key).getDisplay()).append(count + 1 >= content.size() ? ")" : ", `");
            count++;
        }
        executeUpdate(update.toString());
    }

    public void removeAllFromTable(String table, String column, String value) {
        executeUpdate("DELETE FROM " + table + " WHERE " + column + "='" + value + "';");
    }

    public LinkedHashMap<String, DatabaseType> getTableInformation(String[] keys, DatabaseType[] types) {
        LinkedHashMap<String, DatabaseType> content = new LinkedHashMap<>();
        for (int i = 0; i < keys.length; i++) content.put(keys[i], types[i]);
        return content;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
