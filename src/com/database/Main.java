package com.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            Properties props = new Properties();
            FileInputStream in = new FileInputStream("db.properties");
            props.load(in);
            in.close();

            String driver = props.getProperty("jdbc.driver");
            Class.forName(driver).newInstance();

            String myUrl = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");

            conn = DriverManager.getConnection(myUrl, username, password);

            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from artist");

            while (rs.next()) {
                System.out.println(rs.getString("Aname"));
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("1. Add artist");
        System.out.println("2. Add customer");
        System.out.println("3. Add artwork");
        System.out.println("4. Add liked groups");
        System.out.println("5. Update style of the artist");
        System.out.println("6. Show artists");
        System.out.println("7. Show artworks");
        System.out.println("8. Show groups");
        System.out.println("9. Show classifications");
        System.out.println("10. Show liked groups");
        System.out.println("11. Show liked artists");


    }
}
