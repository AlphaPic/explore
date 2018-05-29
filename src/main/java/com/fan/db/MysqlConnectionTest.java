package com.fan.db;

import java.sql.*;

/**
 * @author:fanwenlong
 * @date:2018-05-16 15:19:38
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class MysqlConnectionTest {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://10.113.9.2:3306/hotel";

    static final String USER = "root";
    static final String PASS = "111111";
    static final String CONSTANT = "";


    static {
        try {
            Class.forName(JDBC_DRIVER);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL,USER,PASS);
        Statement stmt = connection.createStatement();
        ResultSet set = stmt.executeQuery("SELECT * FROM PROD_SUBJECT WHERE 1=1");

        stmt.close();
        connection.close();
    }
}
