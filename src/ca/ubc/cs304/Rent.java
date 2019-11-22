package ca.ubc.cs304;

import ca.ubc.cs304.database.DatabaseConnectionHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Rent extends DatabaseConnectionHandler {
    private Connection con;
    final int H = 3600000, D = 86400000, W = 604800000;
    private String dLicense;
    private String vLicense;
    private int confNo;
    private int rentalID;
    private Date fromDate;
    private Timestamp fromTime;
    private Date toDate;
    private Timestamp toTime;
    private int estimate;
    private int credCardNum;
    private Date expDate;
    private int credCardExpYear;
    private long odoReading;
//    private ArrayList<AdditionalEquipment> addEquip;

    public Rent() {
        con = null;

    }
    /*
    Create a Rent object with only rentID as parameter.
     */
    public Rent(int rentID) {
//        this.addEquip = new ArrayList<>();
        // try to connect
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:stu", "ora_colenliu", "a15539159");
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM rentals WHERE " + "rentals_id=" + rentID);

            // add rental info to rental model
            while (rs.next()) {
                this.rentalID = rs.getInt("rental_id");
                this.vLicense = rs.getString("vehicles_license");
                this.confNo = rs.getInt("reservations_confNo");
                this.fromDate = rs.getDate("timeperiod_fromdate");
                this.fromTime = rs.getTimestamp("timeperiod_fromtime");
                this.toDate = rs.getDate("timeperiod_todate");
                this.toTime = rs.getTimestamp("timeperiod_totime");
                this.odoReading = rs.getInt("rentals_odometer");
                this.dLicense = rs.getString("customers_dlicense");
                this.credCardNum = rs.getInt("rentals_cardno");
                this.expDate = rs.getDate("rentals_expdate");
                Vehicle vehicle = new Vehicle(this.vLicense);



            }
            // add additional equipment (if needed) to rent model
        } catch (SQLException e) {
            Logger.getLogger(Reservation.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    /*
    Create Rent object with these following parameters.
     */
    public Rent (String dLicense) {

    }



    }

