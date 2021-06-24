package com.ustb.zerotrust.mapper;

import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class LinkDataBase {
    public void insertData(String appName, String txid) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://123.56.246.148:3306/zerotrust?characterEncoding=utf8","root","ustb_scce_lab202");
        String sql = "insert into Application(appName,txid) values(?,?)";
        PreparedStatement statement = connection.prepareCall(sql);
        statement.setString(1, appName);
        statement.setString(2,txid);

        int i = statement.executeUpdate();
        System.out.println(i);

        statement.close();
        connection.close();
    }

    public String getTxid(String appName) throws ClassNotFoundException, SQLException {
        String txid = "";
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://123.56.246.148:3306/zerotrust?characterEncoding=utf8","root","ustb_scce_lab202");
        String sql = "select txid from Application where appName = ?";
        PreparedStatement statement = connection.prepareCall(sql);
        statement.setString(1, appName);

        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            txid = rs.getString(1);
        }

        rs.close();
        statement.close();
        connection.close();

        return txid;
    }
}
