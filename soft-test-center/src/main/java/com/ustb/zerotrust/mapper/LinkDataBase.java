package com.ustb.zerotrust.mapper;

import com.ustb.zerotrust.entity.SoftFileStore;
import com.ustb.zerotrust.entity.vo.SoftSimpleInfoVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

@Repository
public class LinkDataBase {


     @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
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
    public int insertSoft(SoftSimpleInfoVo softSimpleInfoVo) throws ClassNotFoundException, SQLException {
        Class.forName(driverClassName);
        Connection connection = DriverManager.getConnection(url,"root","ustb_scce_lab202");
        String sql = "insert into soft_info(soft_name,soft_desc,user_name,phone_num) values(?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = null;
        System.out.println(softSimpleInfoVo);
        statement.setString(4, softSimpleInfoVo.getPhoneNum());
        statement.setString(2,softSimpleInfoVo.getSoftDesc());
        statement.setString(1,softSimpleInfoVo.getSoftName());
        statement.setString(3,softSimpleInfoVo.getUserName());

        int i = statement.executeUpdate();
        rs = statement.getGeneratedKeys();//获得主键的自增Id
        int id = 0;
        if (rs.next()){
            id = rs.getInt(1);//Id在结果集中的第一位
        }


        statement.close();
        rs.close();
        connection.close();
        return id;
    }
    public int insertSoftSotre(SoftFileStore softFileStore) throws ClassNotFoundException, SQLException {
        Class.forName(driverClassName);
        Connection connection = DriverManager.getConnection(url,"root","ustb_scce_lab202");
        String sql = "insert into soft_file_store(soft_info_id,soft_path) values(?,?)";
        PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = null;
        System.out.println(softFileStore);
        statement.setInt(1, softFileStore.getSoftInfoId());
        statement.setString(2,softFileStore.getSoftPath());
        int i = statement.executeUpdate();
        rs = statement.getGeneratedKeys();//获得主键的自增Id
        int id = 0;
        if (rs.next()){
            id = rs.getInt(1);//Id在结果集中的第一位
        }


        statement.close();
        rs.close();
        connection.close();
        return id;
    }

    public String getTxid(String appName) throws ClassNotFoundException, SQLException {
        String txid = "";
        Class.forName(driverClassName);
        Connection connection = DriverManager.getConnection(url,"root","ustb_scce_lab202");
        String sql = "select txid from Application where id = ?";
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
        String sql = "select tx_id from Application where soft_name = ?";
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

    public ArrayList<HashMap<String, Object>> getSoftMessage() throws ClassNotFoundException, SQLException {
        Class.forName(driverClassName);
        Connection connection = DriverManager.getConnection(url,"root","ustb_scce_lab202");
        String sql = "SELECT * FROM soft_info";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //执行查询语句并返回结果集
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData data = resultSet.getMetaData();
        ArrayList<HashMap<String, Object>> al = new ArrayList<HashMap<String, Object>>();

        while (resultSet.next()) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            for (int i = 1; i <= data.getColumnCount(); i++) {// 数据库里从 1 开始
                String c = data.getColumnName(i);
                Object v = resultSet.getObject(c);
                System.out.println(c + ":" + v + "\t");
                map.put(c, v);
            }
            al.add(map);
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return al;
    }

    public void updateStatus(int id) throws ClassNotFoundException, SQLException {

        Class.forName(driverClassName);
        Connection connection = DriverManager.getConnection(url,"root","ustb_scce_lab202");
        String sql = "UPDATE soft_info SET status=1 WHERE id=?";
        PreparedStatement statement = connection.prepareCall(sql);
        statement.setInt(1, id);
        int i = statement.executeUpdate();
        System.out.println(i);
        statement.close();
        connection.close();

    }

}
