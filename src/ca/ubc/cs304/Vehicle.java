package ca.ubc.cs304;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Vehicle extends VehicleType {
    private int vid;
    private String vLicense;
    private String make;
    private String model;
    private int year;
    private String color;
    private int odometer;
    private String status;
    private String bLocation;
    private String bCity;
    private String typeName;
    private int WRate;
    private int DRate;
    private int HRate;
    private int KRate;
    private int WiRate;
    private int DiRate;


    public Vehicle() {
        this(0, null, null, null, 0 ,null, 0, null, null, null, null);
    }




    public Vehicle(int vid, String vLicense, String make, String model, int year, String color, int odometer,
                   String status, String typeName, String bLocation, String bCity) {
        vid = this.vid;
        vLicense = this.vLicense;
        make = this.make;
        model = this.model;
        year = this.year;
        color = this.color;
        odometer = this.odometer;
        status = this.status;
        typeName = this.typeName;
        bLocation = this.bLocation;
        bCity = this.bCity;

//        this.vid = new int(vid);
//        this.vLicense = new String(vLicense);
//        this.make = new SimpleStringProperty(make);
//        this.model = new SimpleStringProperty(model);
//        this.year = new SimpleIntegerProperty(year);
//        this.color = new SimpleStringProperty(color);
//        this.odometer = new SimpleIntegerProperty(odometer);
//        this.isAvailable = new SimpleBooleanProperty(isAvailable);
//        this.vType = new SimpleStringProperty(vType);
//        this.bLocation = new SimpleStringProperty(bLocation);
//        this.bCity = new SimpleStringProperty(bCity);
    }

    public int getVID() {
        return vid;
    }



    public String getMake() {
        return make;
    }

//    public void setMake() {
//        this.make.set(String.valueOf(make));
//    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public int getOdometer() {
        return odometer;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getBLocation() {
        return bLocation;
    }

    public String getBCity() {
        return bCity;
    }


    public Vehicle(String vLicense) {
        Connection con = null;
        // try to connect
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_ktnliu", "a19619155");
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM vehicles WHERE vehicles_license= '" + vLicense + "'"); {
                while(rs.next()) {
                    System.out.println("SEARCHING FOR VEHICLE WITH LICENSE " + vLicense);
                    this.vLicense = vLicense;
                    this.vid = rs.getInt("vehicles_id");
                    this.make = rs.getString("vehicles_make");
                    this.model = rs.getString("vehicles_model");
                    this.year = rs.getInt("vehicles_year");
                    this.color = rs.getString("vehicles_color");
                    this.odometer = rs.getInt("vehicles_odometer");
                    this.status = rs.getString("vehicles_status");
                    this.typeName = rs.getString("vehicletypes_name");
                    this.bLocation = rs.getString("branch_location");
                    this.bCity = rs.getString("branch_city");

 /*                   try(ResultSet typeResult = con.createStatement().executeQuery("SELECT * FROM vehiclestypes WHERE vehicletypes_name = '" + typeName + "'")) {
                        while (typeResult.next()) {
                            System.out.println("SEARCHING FOR VEHICLE WITH TYPE EQUAL TO" + typeName);
                            this.WRate = typeResult.getInt("vehiclestypes_wrate");
                            this.DRate = typeResult.getInt("vehiclestypes_drate");
                            this.HRate = typeResult.getInt("vehiclestypes_hrate");
                            this.KRate = typeResult.getInt("vehiclestypes_krate");
                            this.DiRate = typeResult.getInt("vehiclestypes_dirate");
                            this.WiRate = typeResult.getInt("vehiclestypes_wirate");
                        }
                    }*/

                }
            }
        } catch (SQLException e) {
            Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /*public boolean getStatus() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_ktnliu", "a19619155");
            // checking the rentals table
            try (ResultSet rs1 = con.createStatement().executeQuery(
                    "SELECT vehicles_license FROM rentals WHERE vehicles_license = " + this.vLicense)) {
                while (rs1.next()) {
                    this.status = false;
                    return status;
                }
            }

            // checking the reservations table
            try (ResultSet rs2 = con.createStatement().executeQuery(
                    "SELECT vehicles_license FROM reservations WHERE vehicles_license = " + this.vLicense)) {
                while (rs2.next()) {
                this.status = false;
                return status;
                }
            }

    } catch (SQLException e) {
            Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, e);
        }
        status = true;
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }*/

    public int getHRate() {
        return HRate;
    }
}
