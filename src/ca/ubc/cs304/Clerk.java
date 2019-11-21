package ca.ubc.cs304;

import java.sql.Date;
import java.sql.Time;

public class Clerk {
    private String customer = "customer";
    private String superCustomer = "superCustomer";
    //java.sql.Connection con = Connection.getInstance().getConnection();
    private static Object[][] receipt = new Object[30][5];
    private static int rentCount;

    public Clerk() {
        // auto-generated constructor stub

    }

    public String returnCustomerType(int intType) {
        String type = null;

        return type;
        //todo
    }

    public boolean addCustomer(int cellphone, String name, String address, int dlicense) {
        return false;
        //todo
    }

    public boolean rentWithReservation(int confno, int cellphone) {
        return false;
        //todo
    }

    public boolean rentWithoutReservation(String location, String type, Date fromdate, Time fromtime,
                                          Date todate, Date totime) {
        return false;
        //todo
    }

    public Object[][] getReceipt() {
        return receipt;
    }

    public int getCount() {
        return rentCount;
    }

    public String returnVehicle(String vehicles_license, Date date, Time time, int odometer, boolean gas) {
        return null;
        //todo
    }

    public Object[][] generateDailyRentalsReport() {
        return null;
        //todo
    }

    public Object[][] generateDailyRentalsReportBranch() {
        return null;
        //todo
    }

    public Object[][] generateDailyReturnsReport() {
        return null;
        //todo
    }

    public Object[][] generateDailyReturnsReportBranch() {
        return null;
        //todo
    }



}
