package com.database;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBUtils {

    private static Connection conn = null;
    private static Statement stmt = null;
    private static PreparedStatement pstmt = null;
    private static ResultSet rs = null;

    public static Connection initialize(){

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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static void showArtists(Connection conn) {
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from Artist");
            while (rs.next()) {
                System.out.println("Artist name: " + rs.getString("Aname") + "; Style: " + rs.getString("Style"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showArtworks(Connection conn) {
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from Artwork");
            while (rs.next()) {
                System.out.println("Artwork name: " + rs.getString("Title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showGroups(Connection conn) {
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from Agroup");
            while (rs.next()) {
                System.out.println("Group name: " + rs.getString("GName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showClassifications(Connection conn) {
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from Classify");
            while (rs.next()) {
                System.out.println("Artwork Title: " + rs.getString("Title"));
                System.out.println("Artwork Group: " + rs.getString("GName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showLikedGroups(Connection conn) {
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from Like_Group");
            while (rs.next()) {
                System.out.println("Customer: " + rs.getString("CustID"));
                System.out.println("GName: " + rs.getString("GName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        public static void showLikedArtists(Connection conn) {
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("select * from Like_Artist");
                while (rs.next()) {
                    System.out.println("Customer: " + rs.getString("CustID"));
                    System.out.println("Artist: " + rs.getString("AName"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static void addArtist(Connection conn, String artistName, String birthPlace, int age, String style) {
            String insertTableSQL = "INSERT INTO ARTIST"
                    + " VALUES (?,?,?,?)";
            try {
                pstmt = conn.prepareStatement(insertTableSQL);
                pstmt.setString(1, artistName);
                pstmt.setString(2, birthPlace);
                pstmt.setInt(3, age);
                pstmt.setString(4, style);

                int result = pstmt.executeUpdate();
                if(result == 1){
                    System.out.println("Successfully inserted a new artist!");
                }
                else System.out.println("Something went wrong, try again.");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    public static void addCustomer(Connection conn, int customerId, String name, String address, double amount) {
        String insertTableSQL = "INSERT INTO CUSTOMER"
                + " VALUES (?,?,?,?)";
        try {
            pstmt = conn.prepareStatement(insertTableSQL);
            pstmt.setInt(1, customerId);
            pstmt.setString(2, name);
            pstmt.setString(3, address);
            pstmt.setDouble(4, amount);

            int result = pstmt.executeUpdate();
            if(result == 1){
                System.out.println("Successfully inserted a new customer!");
            }
            else System.out.println("Something went wrong, try again.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addArtwork(Connection conn, String title, int year, String type, double price, String aname, String group) {
        String insertArtworkSQL = "INSERT INTO ARTWORK"
                + " VALUES (?,?,?,?,?)";
        try {
            pstmt = conn.prepareStatement(insertArtworkSQL);
            pstmt.setString(1, title);
            pstmt.setInt(2, year);
            pstmt.setString(3, type);
            pstmt.setDouble(4, price);
            pstmt.setString(5, aname);

            int result = pstmt.executeUpdate();
            int groupResult = addGroup(conn, group);
            int classifyResult = addClassify(conn, title, group);

            if(result == 1 && groupResult == 1 && classifyResult == 1){
                System.out.println("Successfully inserted a new artwork!");
            }
            else System.out.println("Something went wrong, try again.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int addGroup(Connection conn, String group) {
        int result = 0;
        String insertGroupSQL = "INSERT INTO AGROUP"
                + " VALUES (?)";
        try {
            pstmt = conn.prepareStatement(insertGroupSQL);
            pstmt.setString(1, group);
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int addClassify(Connection conn, String title, String group) {
        int result = 0;
        String insertClassifySQL = "INSERT INTO CLASSIFY"
                + " VALUES (?,?)";
        try {
            pstmt = conn.prepareStatement(insertClassifySQL);
            pstmt.setString(1, title);
            pstmt.setString(2, group);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int addLikedGroup(Connection conn, int customerId, String groupName) {
        int result = 0;
        String insertLikeGroupSQL = "INSERT INTO LIKE_GROUP"
                + " VALUES (?,?)";
        try {
            pstmt = conn.prepareStatement(insertLikeGroupSQL);
            pstmt.setInt(1, customerId);
            pstmt.setString(2, groupName);
            result = pstmt.executeUpdate();

            String artwork = getArtworkByGroup(conn, groupName);
            String artist = getArtistNameByArtwork(conn, artwork);

            addLikedArtist(conn, customerId, artist);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int addLikedArtist(Connection conn, int cid, String artist) {
        int result = 0;
        String insertLikeArtistSQL = "INSERT INTO LIKE_ARTIST"
                + " VALUES (?,?)";
        try {
            pstmt = conn.prepareStatement(insertLikeArtistSQL);
            pstmt.setInt(1, cid);
            pstmt.setString(2, artist);
            result = pstmt.executeUpdate();

            if(result == 1){
                System.out.println("Successfully inserted a new liked artist!");
            }
            else System.out.println("Something went wrong, try again.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getArtistNameByArtwork(Connection conn, String artwork){
        String result = null;
        String selectByGroupName = "SELECT AName FROM Artwork WHERE Title = ?";
        try{
            pstmt = conn.prepareStatement(selectByGroupName);
            pstmt.setString(1, artwork);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                result = rs.getString(1);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public static String getArtworkByGroup(Connection conn, String groupName){
        String result = null;
        String selectByGroupName = "SELECT Title FROM CLASSIFY WHERE GName = ?";
        try{
            pstmt = conn.prepareStatement(selectByGroupName);
            pstmt.setString(1, groupName);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                result = rs.getString(1);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public static int updateStyle(Connection conn, String artistName, String style) {
        int result = 0;
        String insertClassifySQL = "UPDATE ARTIST SET Style = ? WHERE AName = ?";
        try {
            pstmt = conn.prepareStatement(insertClassifySQL);
            pstmt.setString(1, style);
            pstmt.setString(2, artistName);
            result = pstmt.executeUpdate();

            if(result == 1){
                System.out.println("Successfully updated style!");
            }
            else System.out.println("Something went wrong, try again.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int checkIfExists(Connection conn, String groupName){
        int exists = 0;
        try {
            pstmt = conn.prepareStatement("select count(*) from AGROUP where GName = ?");
            pstmt.setString(1, groupName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                exists = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
}
