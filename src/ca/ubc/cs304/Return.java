package ca.ubc.cs304;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Return {
    Connection con = null;
    final int H = 3600000, D = 86400000, W = 604800000;
    private int rentID;
    private Rent rentInstance;
    private Vehicle vehicleInstance;
    private int odometer;
    private boolean fullTank;
    private int grandTotal;
    private Date returnDate;
    private Timestamp returnTime;
    private int returnID;
    private int totalPrice;

    public Return(int returnID) {
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:stu", "ora_colenliu", "a15539159");
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM returns WHERE returns_value = " + returnID);

            while (rs.next()) {
                this.returnID = rs.getInt("returns_value");
                this.rentID = rs.getInt("rentals_id");
                this.returnDate = rs.getDate("returns_date");
                this.returnTime = rs.getTimestamp("returns_time");
                this.odometer = rs.getInt("returns_odometer");
                this.fullTank = rs.getBoolean("returns_fulltank");

            }
    } catch (SQLException e) {
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

    public void confReturn(Date returnDate, Timestamp returnTime, int odometer, boolean fullTank ) {
        this.returnDate = returnDate;
        this.returnTime = returnTime;
        this.odometer = odometer;
        this.fullTank = fullTank;

        totalPrice = getTotalPrice();
        // trying to connect
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:stu", "ora_colenliu", "a15539159");
            try (PreparedStatement ppst = con.prepareStatement("INSERT into returns (rentals_id, returns_date, " +
                    "returns_time, returns_odometer, returns_fulltank) " +
                    "values (?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                System.out.println("INSERT into returns (rentals_id, returns_date, returns_time, returns_odometer, returns_fulltank)" +
                        "values (" + rentID + "," + returnDate + "," + returnTime + "," + odometer + "," + fullTank + ")");

                ppst.setInt(1, rentID);
                ppst.setDate(2, returnDate);
                ppst.setTimestamp(3, returnTime);
                ppst.setInt(4, odometer);
                ppst.setBoolean(5, fullTank);

                ppst.executeUpdate();

                // testing if return was successful or not
                try (ResultSet rs = ppst.getGeneratedKeys()) {
                    while (rs.next()) {
                        returnID = rs.getInt(1);
                    }
                }
            }


        } catch (SQLException e) {
            Logger.getLogger(Return.class.getName()).log(Level.SEVERE, null, e);
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

    public void setReturnID(int returnID) {
        this.returnID = returnID;
    }

    public int getReturnID() {
        return returnID;
    }

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
