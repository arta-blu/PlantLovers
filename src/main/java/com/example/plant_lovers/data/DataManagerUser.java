package com.example.plant_lovers.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManagerUser {
    private static final String connectionUrl = "jdbc:mysql://localhost:3306/plant_lovers?serverTimezone=UTC";


    public Integer addUser(User user) {
        Connection con = null;

        try {
            con = getConnection();

            var insertStat = con.prepareStatement(
                    "insert into user (user_login, user_name, user_password) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            insertStat.setString(1, user.getLogin());
            insertStat.setString(2, user.getName());
            insertStat.setString(3, user.getPassword());

            insertStat.executeUpdate();

            Integer id = 0;

            try (ResultSet keys = insertStat.getGeneratedKeys()) {
                keys.next();
                id = keys.getInt(1);
                user.setId(id);
            }
            con.close();

            return id;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public List<User> getUser() {
        List<User> users = new ArrayList<>();

        try {
            var con = getConnection();
            var sat = con.createStatement();
            var rs = sat.executeQuery("SELECT * FROM user");

            while (rs.next()) {
                users.add(User.createUser(rs));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }



    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl, "test", "test123");
    }
}

