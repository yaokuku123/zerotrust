package com.ustb.zerotrust.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class LinkDataBase {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


    public void insertData(String appName, String txid) throws ClassNotFoundException, SQLException {
        Class.forName(driverClassName);
        Connection connection = DriverManager.getConnection(url,username,password);
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
        Connection connection = DriverManager.getConnection(url,username,password);
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
    public String getObtxid(int id) throws ClassNotFoundException, SQLException {
        String txid = "";
        Class.forName(driverClassName);
        Connection connection = DriverManager.getConnection(url,username,password);
        String sql = "select obtxid from obtxid_table where id = ?";
        PreparedStatement statement = connection.prepareCall(sql);
        statement.setInt(1, id);

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
        Connection connection = DriverManager.getConnection(url,username,password);
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
    public String getExtractId(String appName) throws ClassNotFoundException, SQLException {
        String extxid = "";
        Class.forName(driverClassName);
        Connection connection = DriverManager.getConnection(url,username,password);
        String sql = "select extract_txid from soft_info where soft_name = ?";
        PreparedStatement statement = connection.prepareCall(sql);
        statement.setString(1, appName);

        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            extxid = rs.getString(1);
        }

        rs.close();
        statement.close();
        connection.close();

        return extxid;
    }
    public boolean getExtractId2(String appName) throws ClassNotFoundException, SQLException {
        String extxid = "";
        Boolean flag = true;
        Class.forName(driverClassName);
        Connection connection = DriverManager.getConnection(url,username,password);
        String sql = "SELECT IFNULL((SELECT extract_txid FROM soft_info WHERE soft_name = ?),\"nodata\")";
        PreparedStatement statement = connection.prepareCall(sql);
        statement.setString(1, appName);

        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            extxid = rs.getString(1);
            if(extxid.length() == 6){
                flag = false;
            }
        }else {
            flag = false;
        }

        rs.close();
        statement.close();
        connection.close();

        return flag;
    }
}
