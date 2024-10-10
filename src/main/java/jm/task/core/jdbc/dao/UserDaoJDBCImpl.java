package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {
    }

    final private String CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS users (\n" +
            "  id int NOT NULL AUTO_INCREMENT,\n" +
            "  name varchar(45) NOT NULL,\n" +
            "  age int NOT NULL,\n" +
            "  email varchar(64) DEFAULT NULL,\n" +
            "  lastName varchar(45) NOT NULL,\n" +
            "  telephone_number bigint DEFAULT NULL,\n" +
            "  PRIMARY KEY (id)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3";
    final private String DROP_TABLE_STATEMENT = "DROP TABLE IF EXISTS users";
    final private String SAVE_USER_STATEMENT = "INSERT INTO users (name, age, lastName)" +
            " VALUES ('%s', %d, '%s')";
    final private String DELETE_USER_STATEMENT = "DELETE FROM users WHERE (id = %d)";
    final private String CLEAN_TABLE_STATEMENT = "truncate table users";
    final private String GET_ALL_USERS_STATEMENT = "SELECT * FROM users";

    private void executeStatement(String sql) {
        try {
            Connection connection = Util.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (Exception e) {
        }
    }

    public void createUsersTable() {
        // System.out.println("trying to create table `users`");
        executeStatement(CREATE_TABLE_STATEMENT);
    }

    public void dropUsersTable() {
        // System.out.println("trying to drop table `users`");
        executeStatement(DROP_TABLE_STATEMENT);
    }

    public void saveUser(String name, String lastName, byte age) {
        // System.out.println("trying to save user. name: " + name + ", lastName: " + lastName + ", age: " + age);
        createUsersTable();
        String sqlStatement = String.format(SAVE_USER_STATEMENT, name, age, lastName);
        executeStatement(sqlStatement);
    }

    public void removeUserById(long id) {
        // System.out.println("trying to remove user by id: " + id);
        createUsersTable();
        String sqlStatement = String.format(DELETE_USER_STATEMENT, id);
        executeStatement(sqlStatement);
    }

    public List<User> getAllUsers() {
        // System.out.println("trying to get all users DF from DB");
        List<User> users = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_USERS_STATEMENT)) {
            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public void cleanUsersTable() {
        // System.out.println("trying to clean table `users`");
        createUsersTable();
        executeStatement(CLEAN_TABLE_STATEMENT);
    }
}
