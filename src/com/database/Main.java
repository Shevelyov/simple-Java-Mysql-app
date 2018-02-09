package com.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {

        printMenuItems();

        Connection conn = DBUtils.initialize();

//        stmt = conn.createStatement();
//        rs = stmt.executeQuery("select * from artist");
//
//        while (rs.next()) {
//            System.out.println(rs.getString("Aname"));
//        }

        Scanner scan = new Scanner(System.in);
        String i = scan.nextLine();

        switch (i) {
            //add new artist
            case "1":

                System.out.println("Artist name: ");
                String artistName = scan.nextLine();

                System.out.println("Birthplace: ");
                String birthplace = scan.nextLine();

                System.out.println("Age: ");
                String stringAge = scan.nextLine();
                int age = Integer.parseInt(stringAge);

                System.out.println("Style: ");
                String style = scan.nextLine();

                DBUtils.addArtist(conn, artistName, birthplace, age, style);
                break;

            //add new customer
            case "2":

                System.out.println("Customer id: ");
                String custId = scan.nextLine();
                int custIdParsed = Integer.parseInt(custId);

                System.out.println("Name: ");
                String customerName = scan.nextLine();

                System.out.println("Address: ");
                String custAddress = scan.nextLine();

                System.out.println("Amount: ");
                String custAmount = scan.nextLine();
                double custAmountParsed = Double.parseDouble(custAmount);

                DBUtils.addCustomer(conn, custIdParsed, customerName, custAddress, custAmountParsed);
                break;

            //add new artwork
            case "3":

                System.out.println("Title: ");
                String artTitle = scan.nextLine();

                System.out.println("Year: ");
                String artYear = scan.nextLine();
                int artYearParsed = Integer.parseInt(artYear);

                System.out.println("Type: ");
                String artType = scan.nextLine();

                System.out.println("Price: ");
                String artPrice = scan.nextLine();
                double artPriceParsed = Double.parseDouble(artPrice);

                System.out.println("Artist name: ");
                String artistArtName = scan.nextLine();

                System.out.println("Group: ");
                int exists = 0;
                String groupName;
                do {
                    if(exists > 0) { System.out.println("Group name already exists, specify new one: "); }
                    groupName = scan.nextLine();
                    exists = DBUtils.checkIfExists(conn, groupName);
                } while (exists > 0);

                DBUtils.addArtwork(conn, artTitle, artYearParsed, artType, artPriceParsed, artistArtName, groupName);
                break;

            //add liked groups
            case "4":
                System.out.println("Customer id: ");
                String customerId = scan.nextLine();
                int cId = Integer.parseInt(customerId);

                System.out.println("Group name: ");
                String grName = scan.nextLine();

                DBUtils.addLikedGroup(conn, cId, grName);
                break;

            //Update artist's style
            case "5":
               System.out.println("Artist name: ");
               String arName = scan.nextLine();

               System.out.println("New style: ");
               String newStyle = scan.nextLine();

               DBUtils.updateStyle(conn, arName, newStyle);
               break;
            case "6": DBUtils.showArtists(conn);
                      break;
            case "7": DBUtils.showArtworks(conn);
                      break;
            case "8": DBUtils.showGroups(conn);
                      break;
            case "9": DBUtils.showClassifications(conn);
                      break;
            case "10": DBUtils.showLikedGroups(conn);
                       break;
            case "11": DBUtils.showLikedArtists(conn);
                       break;
            case "12": System.exit(0);

        }

    }

    public static void printMenuItems(){
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
        System.out.println("12. Exit");
    }
}
