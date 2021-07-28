package com.ustb.zerotrust.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class LinkDataBase {

    @Value("${driverClassName}")
    private String driverClassName;

    @Value("${url}")
    private String url;


    public void insertData(String appName, String txid) throws ClassNotFoundException, SQLException {
        Class.forName(driverClassName);
        Connection connection = DriverManager.getConnection(url,"root","ustb_scce_lab202");
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
        Class.forName(driverClassName);
        Connection connection = DriverManager.getConnection(url,"root","ustb_scce_lab202");
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

    public String getTxidV2(String appName) throws ClassNotFoundException, SQLException {
        String txid = "";
        Class.forName(driverClassName);
        Connection connection = DriverManager.getConnection(url,"root","ustb_scce_lab202");
        String sql = "select tx_id from soft_info where soft_name = ?";
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
