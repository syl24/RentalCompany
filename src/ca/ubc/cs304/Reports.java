package ca.ubc.cs304;

import java.sql.*;

public class Reports {
    Connection con = null;

    public Reports() {

    }

    public void getRentalReports(String location, String date) {
        String query;
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

        try {
            stmt = con.createStatement();

            // entered date and location
            if (date.length() > 0 && !date.equals("TODAY") && location.length() > 0 && !location.equals("ALL")) {
                query = "SELECT rentals.vehicles_license, vehicles.branch_location, " +
                        "vehicles.branch_city, vehicles.vehicletypes_name, rentals.timeperiod_fromdate FROM rentals, vehicles WHERE " +
                        "rentals.vehicles_license = vehicles.vehicles_license AND vehicles.branch_location = " + addQuotation(location) + " AND " + "rentals.timeperiod_fromdate = TO_DATE ('" + date + "', 'YYYY-MM-DD') " +
                        " ORDER BY vehicles.branch_location, vehicles.vehicletypes_name";
            } // entered location only
            else if ((date.length() <= 0 || date.equals("TODAY")) && location.length() > 0 && !location.equals("ALL")){
                query = "SELECT rentals.vehicles_license, vehicles.branch_location, " +
                        "vehicles.branch_city, vehicles.vehicletypes_name, rentals.timeperiod_fromdate FROM rentals, vehicles WHERE " +
                        "rentals.vehicles_license = vehicles.vehicles_license AND vehicles.branch_location = " + addQuotation(location) + " AND rentals.timeperiod_fromdate = CURRENT_DATE"  +
                        " ORDER BY vehicles.branch_location, vehicles.vehicletypes_name";
            } // entered date only
            else if (date.length() > 0 && !date.equals("TODAY") && (location.length() <= 0 || location.equals("ALL"))){
                query = "SELECT rentals.vehicles_license, vehicles.branch_location, " +
                        "vehicles.branch_city, vehicles.vehicletypes_name, rentals.timeperiod_fromdate FROM rentals, vehicles WHERE " +
                        "rentals.vehicles_license = vehicles.vehicles_license AND rentals.timeperiod_fromdate = TO_DATE ('" + date + "', 'YYYY-MM-DD') " +
                        " ORDER BY vehicles.branch_location, vehicles.vehicletypes_name";
            } // no date or location entered
            else {
                query = "SELECT rentals.vehicles_license, vehicles.branch_location, " +
                        "vehicles.branch_city, vehicles.vehicletypes_name, rentals.timeperiod_fromdate FROM rentals, vehicles WHERE " +
                        "rentals.vehicles_license = vehicles.vehicles_license AND rentals.timeperiod_fromdate = CURRENT_DATE" +
                        " ORDER BY vehicles.branch_location, vehicles.vehicletypes_name";
            }

            rs = stmt.executeQuery(query);

            System.out.println("Rented vehicles: ");
            System.out.println(" ");
            int acc = 1;
            while (rs.next()){
                String vLicense = rs.getString(1);
                String loc = rs.getString(2);
                String city = rs.getString(3);
                String vtype = rs.getString(4);
                String startDate = rs.getDate(5).toString();

                System.out.println("Rental " + acc + ":");
                System.out.println("Vehicle license: " + vLicense);
                System.out.println("Rental location: " + loc);
                System.out.println("Rental city: " + city);
                System.out.println("Vehicle type: " + vtype);
                System.out.println("Rental start date: " + startDate);
                System.out.println(" ");

                acc++;
            }

        } catch (SQLException e) {
            System.out.println("Sorry, there are no reports for your search");
            System.out.println(" ");
           // e.printStackTrace();
        }


        try {
            con.close();
        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public void getReturnReports(String location, String date) {
        String query;
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

        try {
            stmt = con.createStatement();

            // entered date and location
            if (date.length() > 0 && !date.equals("TODAY") && location.length() > 0 && !location.equals("ALL")){
                query = "SELECT rentals.vehicles_license, vehicles.branch_location, vehicles.branch_city, vehicles.vehicletypes_name, returns.returns_date " +
                        "FROM rentals, vehicles, returns WHERE rentals.vehicles_license = vehicles.vehicles_license AND returns.rentals_id = rentals.rentals_id " +
                        "AND vehicles.branch_location =" + addQuotation(location) + " AND returns.returns_date = TO_DATE ('" + date + "', 'YYYY-MM-DD') " + " ORDER BY vehicles.branch_location, vehicles.vehicletypes_name ";
            }// entered location only
            else if ((date.length() <= 0 || date.equals("TODAY")) && location.length() > 0 && !location.equals("ALL")){
                query = "SELECT rentals.vehicles_license, vehicles.branch_location, vehicles.branch_city, vehicles.vehicletypes_name, returns.returns_date " +
                        "FROM rentals, vehicles, returns WHERE rentals.vehicles_license = vehicles.vehicles_license AND returns.rentals_id = rentals.rentals_id " +
                        "AND vehicles.branch_location = " + addQuotation(location) + " AND returns.returns_date = CURRENT_DATE ORDER BY vehicles.branch_location, vehicles.vehicletypes_name ";
            } // entered date only
            else if (date.length() > 0 && !date.equals("TODAY") && (location.length() <= 0 || location.equals("ALL"))){
                query = "SELECT rentals.vehicles_license, vehicles.branch_location, vehicles.branch_city, vehicles.vehicletypes_name, returns.returns_date " +
                        "FROM rentals, vehicles, returns WHERE rentals.vehicles_license = vehicles.vehicles_license AND returns.rentals_id = rentals.rentals_id " +
                        " AND returns.returns_date = TO_DATE ('" + date + "', 'YYYY-MM-DD') " + " ORDER BY vehicles.branch_location, vehicles.vehicletypes_name ";
            } // no date or location entered
            else {
                query = "SELECT rentals.vehicles_license, vehicles.branch_location, vehicles.branch_city, vehicles.vehicletypes_name, returns.returns_date " +
                        "FROM rentals, vehicles, returns WHERE rentals.vehicles_license = vehicles.vehicles_license AND returns.rentals_id = rentals.rentals_id " +
                        " AND returns.returns_date = CURRENT_DATE ORDER BY vehicles.branch_location, vehicles.vehicletypes_name ";
            }

            rs = stmt.executeQuery(query);

            System.out.println("Returned vehicles: ");
            System.out.println(" ");
            int acc = 1;

            while (rs.next()){
                String vLicense = rs.getString(1);
                String loc = rs.getString(2);
                String city = rs.getString(3);
                String vtype = rs.getString(4);
                String retDate = rs.getDate(5).toString();

                System.out.println("Rental " + acc + ":");
                System.out.println("Vehicle license: " + vLicense);
                System.out.println("Rental location: " + loc);
                System.out.println("Rental city: " + city);
                System.out.println("Vehicle type: " + vtype);
                System.out.println("Rental start date: " + retDate);
                System.out.println(" ");

                acc++;
            }

        } catch (SQLException e) {
            System.out.println("Sorry, there are no reports for your search");
            System.out.println(" ");
            e.printStackTrace();
        }


        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String addQuotation(String x) {
        return " '" + x + "' ";
    }


}
