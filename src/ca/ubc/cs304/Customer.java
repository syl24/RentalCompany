package ca.ubc.cs304;

import java.sql.*;

public class Customer {


    public void viewVehiclesCount(String key){

        Connection con = null;
        //try to connect
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:stu", "ora_ktnliu", "a19619155");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }


        Statement stmt = null;
        ResultSet rs;

        //query
        try {
            stmt = con.createStatement();

            rs = stmt.executeQuery(
                    "SELECT COUNT (*) AS total FROM (SELECT * FROM vehicles" +
                            "WHERE (vehicles_status LIKE 'AVAILABLE')" +
                            "OR (vehicletypes_name LIKE '% " + key + " %')" +
                            "OR (branch_location LIKE '% " + key + " %'))");

            while (rs.next()){
                Integer count = rs.getInt("total");
                System.out.println(count);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    public void viewVehicles(String key){

        Connection con = null;
        //try to connect
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:stu", "ora_ktnliu", "a19619155");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }


        Statement stmt = null;
        ResultSet rs;

        //query
        try {
            stmt = con.createStatement();

            rs = stmt.executeQuery(
                    "SELECT * FROM vehicles" +
                            "WHERE (vehicles_status LIKE 'AVAILABLE')" +
                            "OR (vehicletypes_name LIKE '% " + key + " %')" +
                            "OR (branch_location LIKE '% " + key + " %')");

            while (rs.next()){
                Integer vehicleID = rs.getInt(1);
                String vehicleLicense = rs.getString(2);
                String vehicleMake = rs.getString(3);
                String vehicleModel = rs.getString(4);
                Integer vehicleYear = rs.getInt(5);
                String vehicleColor = rs.getString(6);
                Integer vehicleOdo = rs.getInt(7);
                String vehicleLoc = rs.getString(10);

                System.out.println("Vehicle ID: " + vehicleID + ", Make: " + vehicleMake + ", Model: " + vehicleModel +
                        ", Year: " + vehicleYear + ", Colour: " + vehicleColor + ", Odometer: " + vehicleOdo +
                        ", Branch Location: " + vehicleLoc + "\n");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}

