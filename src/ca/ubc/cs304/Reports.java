package ca.ubc.cs304;

import javax.swing.text.TableView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reports {
    Connection con = null;

    public Reports() {

    }

    public void getRentalReports(String location, LocalDate date) {
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:stu", "ora_colenliu", "a15539159");

            if (date == null) {
                ResultSet rs = con.createStatement().executeQuery("SELECT rentals.vehicle_license, vehicles.branch_location, " +
                        "vehicles.branch_city, vehicles.vehicletypes_name, rentals.timeperiod_fromdate FROM rentals, vehicles WHERE " +
                        "rentals.vehicles_license = vehicles.vehicles_license AND rentals.timeperiod_fromdate = CURRENT_DATE");
            } else {
                ResultSet rs = con.createStatement().executeQuery("SELECT rentals.vehicle_license, vehicles.branch_location, " +
                        "vehicles.branch_city, vehicles.vehicletypes_name, rentals.timeperiod_fromdate FROM rentals, vehicles WHERE " +
                        "rentals.vehicles_license = vehicles.vehicles_license AND rentals.timeperiod_fromdate = " + addQuotation(date.toString()));
            }

            if (location.equals("")) {
                ResultSet rs = con.createStatement().executeQuery("SELECT rentals.vehicle_license, vehicles.branch_location, " +
                        "vehicles.branch_city, vehicles.vehicletypes_name, rentals.timeperiod_fromdate FROM rentals, vehicles WHERE " +
                        "rentals.vehicles_license = vehicles.vehicles_license ORDER BY vehicles.branch_location, vehicles.vehicletyes_name ;");
            } else {
                ResultSet rs = con.createStatement().executeQuery("SELECT rentals.vehicle_license, vehicles.branch_location, " +
                        "vehicles.branch_city, vehicles.vehicletypes_name, rentals.timeperiod_fromdate FROM rentals, vehicles WHERE " +
                        "rentals.vehicles_license = vehicles.vehicles_license AND vehicles.branch_location = " + addQuotation(location) +
                        "ORDER BY vehicles.branch_location, vehicles.vehicletypes_name ;");
            }



        } catch (SQLException e) {
            Logger.getLogger(Reports.class.getName()).log(Level.SEVERE, null, e);
        }

        
    }

    private String addQuotation(String x) {
        return " '" + x + "' ";
    }


}
