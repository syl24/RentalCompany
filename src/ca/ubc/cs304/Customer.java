package ca.ubc.cs304;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Customer {
    private String name;
    private String phone_number;
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
            con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_ktnliu", "a19619155");
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM customers WHERE customer_dlicense = " + dlicense);
            while (rs.next()) {
                this.phone_number = rs.getString("customers_cellphone");
                this.name = rs.getString("customers_name");
                this.address = rs.getString("customers_address");
                this.dlicense = rs.getString("customers_dlicense");
            }

            con.close();

        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            con.close();
        } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
        }
    }


    // creating a new customer or finding one w/ same phone #
    public Customer(String phone_number, String name, String address, String dlicense) {
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_ktnliu", "a19619155");
            Statement stmt = null;
            ResultSet rs;

            try{
                stmt = con.createStatement();
                rs = stmt.executeQuery("SELECT COUNT (*) AS total FROM (SELECT * FROM customers WHERE (customers_cellphone = '" + phone_number + "' AND customers_name  = '" + name + "' AND customers_address = '" + address + "' AND customers_dlicense = '" + dlicense + "'))");

                while (rs.next()){
                    Integer count = rs.getInt("total");
                    System.out.println(" ");
                    if (count <= 0) {
                        try (PreparedStatement ppst = con.prepareStatement("INSERT into customers values (?, ?, ?, ?)")) {
                            ppst.setString(1, phone_number);
                            ppst.setString(2, name);
                            ppst.setString(3, address);
                            ppst.setString(4, dlicense);

                            ppst.executeUpdate();
                        }
                    }
                    else{
                        con.close();
                    }
                }
            } catch (SQLException e) {
                //e.printStackTrace();
            }


            this.phone_number = phone_number;
            this.name = name;
            this.address = address;
            this.dlicense = dlicense;


        } catch (SQLException e) {
            //Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            con.close();
        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }


//    public Customer(String phone_number, String  name, String address, String dlicense) {
//
//        try {
//            con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_colenliu", "a15539159");
//        } catch (SQLException e) {
//            e.printStackTrace();
//            try {
//                con.close();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        Statement stmt = null;
//        ResultSet customerExist;
//
//        try {
//            stmt = con.createStatement();
//
//            customerExist = stmt.executeQuery("SELECT * FROM customers WHERE customers_cellphone = " + phone_number);
//            {
//                int count = 0;
//                //String dlicense = null;
//
//                // storing the dlicense # of existing customer
//                while(customerExist.next()) {
//                    count++;
//                    dlicense = customerExist.getString(4);
//                }
//
//                // this runs when no records exist that have same cellphone number
//                try(PreparedStatement ppst =
//                            DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_colenliu", "a15539159").prepareStatement("INSERT " +
//                                    "into customers (customers_cellphone, customers_name, customers_address, customers_dlicense) " +
//                                    "values (?, ?, ?, ?)")) {
//
//                    ppst.setString(1, phone_number);
//                    ppst.setString(2, name);
//                    ppst.setString(3, address);
//                    ppst.setString(4, dlicense);
//
//                    ppst.executeUpdate();
//
//                    // testing to see if above query works
//                    try(ResultSet rs = ppst.getGeneratedKeys()) {
//                        while (rs.next()) {
//                            phone_number = rs.getString(1);
//                        }
//                    }
//                    ppst.close();
//                }
//                if (count == 0) {
//                }
//
//                // save new info of customer
//
//                this.phone_number = phone_number;
//                this.name = name;
//                this.address = address;
//                this.dlicense = dlicense;
//
//            }
//
//            customerExist.close();
//
//        } catch (SQLException e) {
//            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
//        }
//
//        try {
//            con.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }


    // (COLEN) MY WORK IS ABOVE THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // (COLEN) MY WORK IS ABOVE THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // (COLEN) MY WORK IS ABOVE THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // (COLEN) MY WORK IS ABOVE THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // (COLEN) MY WORK IS ABOVE THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // (COLEN) MY WORK IS ABOVE THIS LINE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!




    public String getDLicense() {
        return this.dlicense;
    }

    public String getName() {
        return name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getAddress() {
        return address;
    }

    // commented the below constructor out so mine could func

//    public Customer(String name, Integer phone_number, String address, String dlicense){
//        this.name = name;
//        this.phone_number = phone_number;
//        this.address = address;
//        this.dlicense = dlicense;
//    }


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