package ca.ubc.cs304;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Customer {
    private String name;
    private Integer phone_number;
    private String address;
    private String dlicense;
    Connection con = null;







    // (COLEN) PUTTING MY WORK UNDER THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // (COLEN) PUTTING MY WORK UNDER THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // (COLEN) PUTTING MY WORK UNDER THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // (COLEN) PUTTING MY WORK UNDER THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // (COLEN) PUTTING MY WORK UNDER THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    public Customer(String dlicense) {
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:stu", "ora_colenliu", "a15539159");
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM customers WHERE customer_dlicense = " + dlicense);
            while (rs.next()) {
                this.phone_number = rs.getInt("customers_cellphone");
                this.name = rs.getString("customers_name");
                this.address = rs.getString("customers_address");
                this.dlicense = rs.getString("customers_dlicense");
            }

        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
        }
    }


    // creating a new customer or finding one w/ same phone #
    public Customer(Integer phone_number, String  name, String address) {
        try {
            ResultSet customerExist = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:stu", "ora_colenliu", "a15539159").createStatement().executeQuery("SELECT " +
                    "* FROM customers WHERE customers_cellphone = " + phone_number); {
                int count = 0;
                String dlicense = null;

                // storing the dlicense # of existing customer
                while(customerExist.next()) {
                    count++;
                    dlicense = customerExist.getString(1);
                }

                // this runs when no records exist that have same cellphone number
                if (count == 0) {
                    try(PreparedStatement ppst =
                            DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:stu", "ora_colenliu", "a15539159").prepareStatement("INSERT " +
                                    "into customers (customers_cellphone, customers_name, customers_address) " +
                                    "values (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {

                        ppst.setInt(1, phone_number);
                        ppst.setString(2, name);
                        ppst.setString(3, address);

                        ppst.executeUpdate();

                        // testing to see if above query works
                        try(ResultSet rs = ppst.getGeneratedKeys()) {
                            while (rs.next()) {
                                dlicense = rs.getString(1);
                            }
                        }
                    }
                }

                // save new info of customer

                this.phone_number = phone_number;
                this.name = name;
                this.address = address;
                this.dlicense = dlicense;

            }

        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
        }
    }


    // (COLEN) MY WORK IS ABOVE THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // (COLEN) MY WORK IS ABOVE THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // (COLEN) MY WORK IS ABOVE THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // (COLEN) MY WORK IS ABOVE THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // (COLEN) MY WORK IS ABOVE THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // (COLEN) MY WORK IS ABOVE THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!






    // commented the below constructor out so mine could func

//    public Customer(String name, Integer phone_number, String address, String dlicense){
//        this.name = name;
//        this.phone_number = phone_number;
//        this.address = address;
//        this.dlicense = dlicense;
//    }

    /** moved these functions into Bank.java */
/*    public Integer viewVehiclesCount(String type, String loc, String time){

        Connection con = null;
        //try to connect
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_ktnliu", "a19619155");
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
                    "SELECT COUNT (*) AS total FROM (SELECT * FROM vehicles WHERE (vehicles_status LIKE 'AVAILABLE') AND (vehicletypes_name LIKE '%" + type + "%') AND (branch_city LIKE '%" + loc + "%'))");

            while (rs.next()){
                Integer count = rs.getInt("total");
                System.out.println("Available vehicles: " + count);
                if (count <= 0) {
                    System.out.println("Sorry! There are no vehicles that match your search.");
                    con.close();
                    return 0;
                }
                else
                    con.close();
                    return 1;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //close connection
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }



    public void viewVehicles(String type, String loc, String time){

        Connection con = null;
        //try to connect
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_ktnliu", "a19619155");
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
                    "SELECT * FROM vehicles WHERE (vehicles_status LIKE 'AVAILABLE') AND (vehicletypes_name LIKE '%" + type + "%') AND (branch_city LIKE '%" + loc + "%')");


            while (rs.next()){
                Integer vehicleID = rs.getInt(1);
                String vehicleLicense = rs.getString(2);
                String vehicleMake = rs.getString(3);
                String vehicleModel = rs.getString(4);
                Integer vehicleYear = rs.getInt(5);
                String vehicleColor = rs.getString(6);
                Integer vehicleOdo = rs.getInt(7);
                String vehicleLoc = rs.getString(10);

                System.out.println("Vehicle ID: " + vehicleID + ", License: " + vehicleLicense + ", Make: " + vehicleMake + ", Model: " + vehicleModel +
                        ", Year: " + vehicleYear + ", Colour: " + vehicleColor + ", Odometer: " + vehicleOdo +
                        ", Branch Location: " + vehicleLoc + "\n");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //close connection
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    //private void

    /**customer provides the location, the type of the vehicle,
     * and the day and time they would like to pick up and return the vehicle.
     *
     * To make a reservation, the customer provides her/his
     * name and phone number, and the system prints a confirmation number*/

    public void makeReservation(String key){

//        Connection con = null;
//        //try to connect
//        try {
//            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:stu", "ora_ktnliu", "a19619155");
//        } catch (SQLException e) {
//            e.printStackTrace();
//            try {
//                con.close();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//
//        Statement stmt = null;
//        ResultSet rs;
//
//        //query
//        try {
//            stmt = con.createStatement();
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
    }

}