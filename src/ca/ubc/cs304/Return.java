package ca.ubc.cs304;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Return {
    Connection con = null;
    final int H = 3600000, D = 86400000, W = 604800000;

    private int rentID;
    private Date returnDate;
    private Timestamp returnTime;
    private int odometer;
    private boolean fullTank;
    private int totalPrice;

    private String rentVLicense;
    private Rent rentInstance;
    private Vehicle vehicleInstance;
//    private int grandTotal;
//    private int returnID;

    public Return(int rentID, Date returnDate, Timestamp returnTime, int odometer, boolean fullTank, int totalPrice){
        this.rentID = rentID;
        this.returnDate = returnDate;
        this.returnTime = returnTime;
        this.odometer = odometer;
        this.fullTank = fullTank;
        this.totalPrice = totalPrice;
    }

    public Return(int rentID) {
        this.rentID = rentID;
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_ktnliu", "a19619155");
            ResultSet rs = con.createStatement().executeQuery("SELECT rentals.timeperiod_todate, rentals.timeperiod_totime, rentals.rentals_odometer, rentals.vehicles_license " +
                    "FROM returns, rentals WHERE rentals.rentals_id = " + rentID);

            while (rs.next()) {
                //this.returnID = rs.getInt("returns_value");
                this.returnDate = rs.getDate(1);
                this.returnTime = rs.getTimestamp(2);
                this.odometer = rs.getInt(3);
                this.fullTank = true;
                this.totalPrice = calcTotalPrice();
                this.rentVLicense = rs.getString(4);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Sorry, that rental doesn't exist");
            Logger.getLogger(Return.class.getName()).log(Level.SEVERE, null, e);

        }



    }

    // use this rentInstance
    public Return(Rent rentInstance) {
        this.rentInstance = rentInstance;
        this.rentID = rentInstance.getRentalID();
        this.vehicleInstance = rentInstance.getVehicle();
    }

    // finalize return and inserts new return entry into DB

    public void confReturn() {

        // trying to connect
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_ktnliu", "a19619155");

            try (PreparedStatement ppst = con.prepareStatement("INSERT into returns VALUES (?, ?, ?, ?, ?, ?)")){

                ppst.setInt(1, rentID);
                ppst.setDate(2, returnDate);
                ppst.setTimestamp(3, returnTime);
                ppst.setInt(4, odometer);
                ppst.setBoolean(5, fullTank);
                ppst.setInt(6, odometer);

                ppst.executeUpdate();

                // testing if return was successful or not
/*                try (ResultSet rs = ppst.getGeneratedKeys()) {
                    while (rs.next()) {
                        rentID = rs.getInt(1);
                    }
                }*/
            }
            // update the returned vehicle to available
            try {
                PreparedStatement ps = con.prepareStatement("UPDATE vehicles SET vehicles_status = 'AVAILABLE' WHERE vehicles_license = ?");
                ps.setString(1, rentVLicense);
                System.out.println("Vehicle has been returned!");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Could not update vehicle");
            }
            con.close();

        } catch (SQLException e) {
            Logger.getLogger(Return.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int calcGrossTotal() {
        int HRate = vehicleInstance.getHRate();
        int DRate = vehicleInstance.getDRate();
        int WRate = vehicleInstance.getWRate();

        long diff = Timestamp.valueOf(LocalDateTime.now()).getTime() - rentInstance.getFromTime().getTime();

        int week = (int) diff / W;
        diff %= W;

        int day = (int) diff / D;
        diff %= D;

        int hour = (int) diff / H;
        diff %= H;

        return week * WRate + day * DRate + hour * HRate;

    }

    public int calcInsurCost() {
        // obtaining vehicle type and model from vehicle license
        int DiRate = vehicleInstance.getDiRate();
        int WiRate = vehicleInstance.getWiRate();
        int KRate = vehicleInstance.getKRate();

        long diff = Timestamp.valueOf(LocalDateTime.now()).getTime() - rentInstance.getFromTime().getTime();

        int week = (int) diff / W;
        diff %= W;

        int day = (int) diff / D;
        diff %= D;

        int hour = (int) diff / H;
        diff %= H;

        int insurance = week * WiRate + day * DiRate + hour  * KRate;

//        // see if customer isRoadStar is true or not
//        if (rentInstance.getCustomer().getIsRoadStar()) {
//            insurance = insurance / 2;
//        }

        return insurance;
    }

    public String getRentTimePeriod() {
        String tPeriod = "";

        long diff = Timestamp.valueOf(LocalDateTime.now()).getTime() - rentInstance.getFromTime().getTime();

        int week = (int) diff / W;
        diff %= W;
        if (week > 0) {
            tPeriod += week + " Week" + (week > 1 ? "s " : " ");
        }

        int day = (int) diff / D;
        diff %= D;
        if (day > 0) {
            tPeriod += day + " Day" + (day > 1 ? "s " : " ");
        }

        int hour = (int) diff / H;
        diff %= H;
        if (hour > 0) {
            tPeriod += hour + " Hour" + (hour > 1 ? "s " : " ");
        }

        return tPeriod;
    }
    public int calcTotalPrice() {
        // and additional equipment?
        totalPrice = calcGrossTotal() + calcInsurCost();
        return totalPrice;
    }

//    public void setReturnID(int returnID) {
//        this.returnID = returnID;
//    }
//
//    public int getReturnID() {
//        return returnID;
//    }

    public void setRentID(int rentID) {
        this.rentID = rentID;
    }

    public int getRentID() {
        return rentID;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setFullTank(boolean fullTank) {
        this.fullTank = fullTank;
    }

    public boolean isFullTank() {
        return fullTank;
    }

    public int getGrossTotal() {
        return calcGrossTotal();
    }

    public int getInsurCost() {
        return calcInsurCost();
    }

    public void setTotalPrice(int amt) {
        this.totalPrice = amt;
    }





}
