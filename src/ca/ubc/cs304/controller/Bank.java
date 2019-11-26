package ca.ubc.cs304.controller;

import ca.ubc.cs304.*;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.TerminalTransactions;
import ca.ubc.cs304.ui.TransactionsWindow;

import java.sql.*;
import java.util.LinkedList;

/**
 * This is the main controller class that will orchestrate everything.
 */
public class Bank implements LoginWindowDelegate, TerminalTransactionsDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private LoginWindow loginWindow = null;
    private TransactionsWindow transwindow = null;
    private Customer customer = null;
    private Reservation reso = null;
    private Rent rent = null;
    private static final String DEFAULT_LOC = "Vancouver";
    private static final String DEFAULT_TYPE = "SUV";
    private Reports report = new Reports();
    private Return ret = null;

    public Bank() {
        dbHandler = new DatabaseConnectionHandler();
    }

    private void start() {
        loginWindow = new LoginWindow();
        loginWindow.showFrame(this);


    }

    /**
     * LoginWindowDelegate Implementation
     *
     * connects to Oracle database with supplied username and password
     */
    public void login(String username, String password) {
        boolean didConnect = dbHandler.login(username, password);

        if (didConnect) {
            // Once connected, remove login window and start text transaction flow
            loginWindow.dispose();



            TerminalTransactions transaction = new TerminalTransactions();
            transaction.showMainMenu(this);
//			transwindow = new TransactionsWindow();
//			transwindow.showFrame(this);
        } else {
            loginWindow.handleLoginFailed();

            if (loginWindow.hasReachedMaxLoginAttempts()) {
                loginWindow.dispose();
                System.out.println("You have exceeded your number of allowed attempts");
                System.exit(-1);
            }
        }
    }

    /**
     * TermainalTransactionsDelegate Implementation
     *
     * Insert a branch with the given info
     */
    public void insertBranch(BranchModel model) {
        dbHandler.insertBranch(model);
    }

    /**
     * TermainalTransactionsDelegate Implementation
     *
     * Delete branch with given branch ID.
     */
    public void deleteBranch(String loc, String city) {
        dbHandler.deleteBranch(loc, city);
    }

    /**
     * TermainalTransactionsDelegate Implementation
     *
     * Update the branch name for a specific ID
     */

    public void updateBranch(int branchId, String name) {
        dbHandler.updateBranch(branchId, name);
    }

    /**
     * TermainalTransactionsDelegate Implementation
     *
     * Displays information about varies bank branches.
     */
    public void showBranch() {
        BranchModel[] models = dbHandler.getBranchInfo();

        for (int i = 0; i < models.length; i++) {
            BranchModel model = models[i];

            // simplified output formatting; truncation may occur
            System.out.printf("%-10.10s", model.getId());
            System.out.printf("%-20.20s", model.getName());
            if (model.getAddress() == null) {
                System.out.printf("%-20.20s", " ");
            } else {
                System.out.printf("%-20.20s", model.getAddress());
            }
            System.out.printf("%-15.15s", model.getCity());
            if (model.getPhoneNumber() == 0) {
                System.out.printf("%-15.15s", " ");
            } else {
                System.out.printf("%-15.15s", model.getPhoneNumber());
            }

            System.out.println();
        }
    }


    /**	Customer transactions
     * - view # available vehicles
     * - view available vehicles
     * - make a reservation
     */
/*

    public Integer customerVehiclesCount(String key){

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

            if(key == "Vancouver" || key == "Richmond") {
                rs = stmt.executeQuery(
                        "SELECT COUNT (*) AS total FROM (SELECT * FROM vehicles WHERE (vehicles_status LIKE 'AVAILABLE') AND (branch_city LIKE '%" + key + "%'))");
            }else{
                rs = stmt.executeQuery(
                        "SELECT COUNT (*) AS total FROM (SELECT * FROM vehicles WHERE (vehicles_status LIKE 'AVAILABLE') AND (vehicletypes_name LIKE '%" + key + "%') AND (branch_city LIKE '%" + DEFAULT_LOC + "%'))");
            }

            while (rs.next()){
                Integer count = rs.getInt("total");
                System.out.println("Available vehicles: " + count);
                System.out.println(" ");
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
*/


    public Integer customerVehiclesCount(String type, String loc){
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
                System.out.println(" ");
                if (count <= 0) {
                    System.out.println("Sorry! There are no vehicles that match your search.");
                    con.close();
                    return 0;
                }
                else{
                    con.close();
                    return 1;
                }
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

    public void customerVehiclesView(String type, String loc){
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

            System.out.println("Available vehicles: ");
            int acc = 1;

            while (rs.next()){
                Integer vehicleID = rs.getInt(1);
                String vehicleLicense = rs.getString(2);
                String vehicleMake = rs.getString(3);
                String vehicleModel = rs.getString(4);
                Integer vehicleYear = rs.getInt(5);
                String vehicleColor = rs.getString(6);
                Integer vehicleOdo = rs.getInt(7);
                String vehicleLoc = rs.getString(10);

                System.out.println("Vehicle " + acc + ": ");
                System.out.println("Vehicle ID: " + vehicleID + ", License: " + vehicleLicense + ", Make: " + vehicleMake + ", Model: " + vehicleModel +
                        ", Year: " + vehicleYear + ", Colour: " + vehicleColor + ", Odometer: " + vehicleOdo +
                        ", Branch Location: " + vehicleLoc + "\n");

                acc++;

//                con.close();
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
    }


    public Customer customerLogin(String phone_number, String  name, String address, String dlicense){
        customer = new Customer(phone_number, name, address, dlicense);
        return customer;
    }

    public void makeNewReservation(String dlicense, String typeName, Date fromDate, Timestamp fromTime, Date toDate, Timestamp toTime) {
        reso = new Reservation(dlicense, typeName, fromDate, fromTime, toDate, toTime);
    }

    public void confirmReso(String dLicense, String typeName, Date fromDate, Timestamp fromTime, Date toDate, Timestamp toTime){
        reso.confReso(reso.getConfNo(), dLicense, typeName, fromDate, fromTime, toDate, toTime);
        System.out.println("Thank you for your reservation!");
        System.out.println("Your Confirmation number: " + reso.getConfNo());
    }

    public int clerkReso(){
        return reso.getConfNo();
    }

    public LinkedList<String> getOneCar(String type, String loc, Timestamp fromTime, Timestamp toTime){
        LinkedList<String> list = new LinkedList<>();

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

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(
                    "SELECT * FROM vehicles WHERE (vehicles_status LIKE 'AVAILABLE') AND (vehicletypes_name LIKE '%" + type + "%') AND (branch_city LIKE '%" + loc + "%') AND ROWNUM = 1");

            if(rs.next()){
                list.add(rs.getString("vehicles_license"));
                list.add(rs.getString("vehicles_odometer"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void makeNewRental(int confNo, String vLicense, String dLicense, Date fromDate, Timestamp fromTime, Date toDate, Timestamp toTime,
                              int odometer, String cardName, int cardNo, Date expDate){
        rent = new Rent(confNo, vLicense, dLicense, fromDate, fromTime, toDate, toTime, odometer, cardName, cardNo, expDate);
        rent.confRent(confNo);
    }

    public LinkedList getResoInfo(Integer confNo){

        LinkedList info = new LinkedList();
        reso = new Reservation(confNo);
        //System.out.println("made the new reso");
        // want: String type, String loc, Date fromDate, Timestamp fromTime, Date toDate, Timestamp toTime

        if (reso != null && reso.getTypeName() != null){
            info.add(reso.getTypeName());
            info.add(reso.getFromDate());
            info.add(reso.getFromTime());
            info.add(reso.getToDate());
            info.add(reso.getToTime());
            return info;
        }
        else
            return null;

    }

    public void processReturn(Integer rentID) {
        ret = new Return(rentID);
        ret = new Return(rentID);
        ret.confReturn();
    }

    public void viewTables(String name){

        Connection con = null;
        int counter = -1;
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

        try {
            stmt = con.createStatement();

            if (name.equals("ALL")){
                rs = stmt.executeQuery("SELECT DISTINCT table_name FROM user_tables");
                while (rs.next()){
                    System.out.println(rs.getString(1));
                }
            } else {
                rs = stmt.executeQuery("SELECT COUNT (*) AS total FROM " + name);
                while(rs.next()){
                    Integer count = rs.getInt("total");
                    if (count <= 0) {
                        System.out.println("Sorry! There are no vehicles that match your search.");
                        con.close();
                    }
                    else{
                        rs = stmt.executeQuery("SELECT count (*) AS total FROM (user_tab_columns) WHERE table_name LIKE '" + name + "'");
                        while (rs.next()){
                            //this is the total columns);
                            counter = rs.getInt("total");
                        }
                        rs = stmt.executeQuery("SELECT * FROM " + name);
                        int acc = 1;
                        while (rs.next()){
                            System.out.println(name + " " + acc + ": ");
                            for (int i = 1; i < counter+1; i++){
                                System.out.println(rs.getString(i));
                            }
                            acc++;
                            System.out.println(" ");
                        }
                    }
                    count = 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Sorry, that table does not exist.");
            //e.printStackTrace();
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(String table, String primaryKey, int key) {
        dbHandler.deleteData(table, primaryKey, key);
    }

    public void deleteData(String table, String primaryKey, String key) {
        dbHandler.deleteData(table, primaryKey, key);
    }

/*    public void insertReservation(Integer confNo, String vtype, String dlicense, String fromdate, String fromtime, String todate, String totime){
        reso = new Reservation(confNo, vtype, dlicense, Date.valueOf(fromdate), Timestamp.valueOf(fromtime), Date.valueOf(todate), Timestamp.valueOf(totime));

        reso.confReso(confNo, vtype, dlicense, Date.valueOf(fromdate), Timestamp.valueOf(fromtime), Date.valueOf(todate), Timestamp.valueOf(totime));
        System.out.print("Added new reservation!");
    }

    public void insertRental(Integer rentalID, String vLicense, String dLicense, Date fromDate, Timestamp fromTime,
                             Date toDate, Timestamp toTime, Integer odometer, String cardName, Integer cardNo, Date expDate, Integer confNo) {
        rent = new Rent(rentalID, vLicense, dLicense, fromDate, fromTime, toDate, toTime, odometer, cardName, cardNo, expDate, confNo);
        rent.confRent(confNo);

    }*/

    public void insertCustomer(String phone, String name, String address, String dLicense){
        customer = new Customer(phone,name,address,dLicense);
    }

    public void updateCustomer(String query){
        dbHandler.updateCustomer(query);
    }

    public void rentReports(String loc, String date){
        report.getRentalReports(loc, date);
    }

    public void returnReports(String loc, String date){
        report.getReturnReports(loc, date);
    }



    /**
     * TerminalTransactionsDelegate Implementation
     *
     * The TerminalTransaction instance tells us that it is done with what it's
     * doing so we are cleaning up the connection since it's no longer needed.
     */
    public void terminalTransactionsFinished() {
        dbHandler.close();
        dbHandler = null;

        System.exit(0);
    }

    /**
     * Main method called at launch time
     */
    public static void main(String[] args) {
        Bank bank = new Bank();
        bank.start();
    }
}