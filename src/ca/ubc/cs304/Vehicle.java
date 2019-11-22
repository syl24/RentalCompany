package ca.ubc.cs304;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Vehicle {
    private int vid;
    private String vLicense;
    private String make;
    private String model;
    private int year;
    private String color;
    private int odometer;
    private String status;
    private String vType;
    private String bLocation;
    private String bCity;

    public Vehicle() {
        this(0, null, null, null, 0 ,null, 0, null, null, null, null);
    }




    public Vehicle(int vid, String vLicense, String make, String model, int year, String color, int odometer,
                   String status, String vType, String bLocation, String bCity) {

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

    public String getStatus() {
        return status;
    }

    public String getVType() {
        return vType;
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
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:stu", "ora_colenliu", "a15539159");
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM vehicle WHERE vehicle_license=" + vLicense); {
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
                    this.vType = rs.getString("vehicletypes_name");
                    this.bLocation = rs.getString("branch_location");
                    this.bCity = rs.getString("branch_city");

                }
            }
        } catch (SQLException e) {
            Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, e);
        }
    }



//    public boolean checkLicense(String vehicles_license) {
//        return false;
//        //todo
//    }
//
//    public int getId (String vehicles_license) {
//        return 0;
//        //todo
//    }
//
//    public int getStatus(String vehicles_license) {
//        return 0;
//        //todo
//    }
//
//    public String getMake(String vehicles_license) {
//        return null;
//        //todo
//    }
//
//    public int getYear(String vehicles_license) {
//        return 0;
//        //todo
//    }
//
//    public String getColor(String vehicles_license) {
//        return null;
//        //todo
//    }
//
//    public int getOdometer(String vehicles_license) {
//        return 0;
//        //todo
//    }
//    /*
//    returns true if gas tank is full, false otherwise.
//     */
//    public boolean getGas(String vehicles_license) {
//        return false;
//        //todo
//    }
//
//    public boolean vehicleExist(String vehicles_license) {
//        return false;
//        //todo
//    }
//
//    public String getBranchLocation(String vehicles_license) {
//        return null;
//        //todo
//    }
//
//    public String getBranchCity(String vehicles_license) {
//        return null;
//        //todo
//    }





}
