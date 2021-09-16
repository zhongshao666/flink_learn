package test;


import java.sql.*;
import java.util.HashMap;


public class MySql {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.18.70:3306?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true", "root", "Secsmart#612");
        Statement statement = conn.createStatement();
        ResultSet res = statement.executeQuery("show databases");
        while (res.next()){
            System.out.println(res.getMetaData());
        }
        statement.close();
        conn.close();

        new HashMap<>();
    }
}
