package ca.ubc.cs304.controller;

import ca.ubc.cs304.Customer;
import ca.ubc.cs304.Reservation;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.TerminalTransactions;
import ca.ubc.cs304.ui.TransactionsWindow;

import java.sql.*;

/**
 * This is the main controller class that will orchestrate everything.
 */
public class Bank implements LoginWindowDelegate, TerminalTransactionsDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private LoginWindow loginWindow = null;
    private TransactionsWindow transwindow = null;
    private Customer customer = null;
    private Reservation reso = null;

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
    public void deleteBranch(int branchId) {
        dbHandler.deleteBranch(branchId);
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

    public Integer customerVehiclesCount(String type, String loc, String time){
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

    public void customerVehiclesView(String type, String loc, String time){
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


    public void customerLogin(String phone_number, String  name, String address){
        customer = new Customer(phone_number, name, address);
    }

    public void makeNewReservation(String key) {

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