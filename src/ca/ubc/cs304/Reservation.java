package ca.ubc.cs304;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reservation {
    Connection con = null;
    final int H = 3600000, D = 86400000, W = 604800000;
    private String dLicense;
    private String vLicense;
    private String typeName;
    private Date fromDate;
    private Timestamp fromTime;
    private Date toDate;
    private Timestamp toTime;
    private int estimate;
    private int id;

    public Reservation() {

    }

    /*
    Creating a Reservation object using a confNo
     */
    public Reservation(int confNo) {
        // if we add additional equipment (eventually)
//        this.additionalEquipment = new ArrayList<>();

        // try to connect
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_colenliu", "a15539159");
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM reservations WHERE reservations_confNo = " + confNo);

            // additional eqipment?

            // adding the generated results from above into our reservations model
            while (rs.next()) {
                this.id = rs.getInt("reservations_confNo");
                this.dLicense = rs.getString("customers_dlicense");
                this.typeName = rs.getString("vehicletypes_name");
                this.fromDate = rs.getDate("timeperiod_fromdate");
                this.fromTime = rs.getTimestamp("timeperiod_fromtime");
                this.toDate = rs.getDate("timeperiod_todate");
                this.toTime = rs.getTimestamp("timeperiod_totime");
            }
            // adding any additional equipment?
        } catch (SQLException e) {
            Logger.getLogger(Reservation.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /*
    Creating a Reservation object using these parameters
     */
    public Reservation(String dLicense, String typeName, Date fromDate, Timestamp fromTime, Date toDate, Timestamp toTime) {
        this.dLicense = dLicense;
        this.typeName = typeName;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;

        // additional equipment?

        // estimate cost for customer
        this.estimate = estiPrice();
    }


    // returns -1 if confReso was not successful
    public int confReso(String typeName, String dLicense, Date fromDate, Timestamp fromTime, Date toDate, Timestamp toTime) {
        int confNo = -1;
        String SQL = "INSERT into reservations (vehicletypes_name, customers_dlicense, timeperiod_fromdate, timeperiod_fromtime, " +
                "timeperiod_todate, timeperiod_totime)" +
                " values (" + addQuotation(typeName) + "," + addQuotation(dLicense) + "," + addQuotation(fromDate.toString()) + "," +
                addQuotation(fromTime.toString()) + "," + addQuotation(toDate.toString()) + "," + addQuotation(toTime.toString()) + ")";
        System.out.println(SQL);
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:stu", "ora_colenliu", "a15539159");
            st = con.createStatement();
            st.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
            if (rs.next()) {
                confNo = rs.getInt(1);
            }
        } catch (SQLException e) {
            Logger.getLogger(Reservation.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {

                }
            }

            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {

                }
            }

    }

        return confNo;

}

//    public void confReso() {
//        try {
//            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_colenliu", "a15539159");
//            try (PreparedStatement ppst = con.prepareStatement("INSERT into reservations (vehicletypes_name, customers_dlicense," +
//                    "timeperiod_fromdate, timeperiod_fromtime, timeperiod_todate, timeperiod_totime)" +
//                    "values(?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
//                System.out.println("INSERT into reservations (vehicletypes_name, customers_dlicense, " +
//                        "timeperiod_fromdate, timeperiod_fromtime, timeperiod_todate, timeperiod_totime)"
//                + "values (" + typeName + "," + dLicense + "," + fromDate + "," + fromTime + "," + toDate + "," + toTime + ")" );
//
//                ppst.setString(1, typeName);
//                ppst.setString(2, dLicense);
//                ppst.setDate(3, fromDate);
//                ppst.setTimestamp(4, fromTime);
//                ppst.setDate(5, toDate);
//                ppst.setTimestamp(6, toTime);
//
//                ppst.executeUpdate();
//
//                // test whether or not addition to reservation was successful
//                try (ResultSet rs = ppst.getGeneratedKeys()) {
//                    while (rs.next()) {
//                        this.id = rs.getInt(1);
//
//                        // ADD ADDITIONAL EQUIPMENT?!?!?
//
//                    }
//                    ppst.close();
//                }
//
//            }
//
//        } catch (SQLException e) {
//            Logger.getLogger(Reservation.class.getName()).log(Level.SEVERE, null, e);
//        }
//        try {
//            con.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    /*
    Cancel reservation that was booked already
     */
    public void cancelReso(int id) {
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_colenliu", "a15539159");
            Statement st = con.createStatement(); {
                st.executeUpdate("DELETE FROM reservations WHERE reservations_confNo = " + id + ";");
            }
        } catch (SQLException e) {
            System.out.println("UPDATE HAS FAILED");
        }
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
Estimate price for customer
 */
    private int estiPrice() {
        // obtaining vehicle type from the vehicles license
        Vehicle vehicle = new Vehicle(this.vLicense);
        int HRate = vehicle.getHRate();
        int WRate = vehicle.getWRate();
        int DRate = vehicle.getDRate();
        int KRate = vehicle.getKRate();
        int WiRate = vehicle.getWiRate();
        int DiRate = vehicle.getDiRate();

        long diff = toTime.getTime() - fromTime.getTime();

        int week = (int) diff / W;
        diff %= W;

        int day = (int) diff / D;
        diff %= D;

        int hour = (int) diff / H;
        diff %= H;

        int priceEsti = week * WRate + day * DRate + hour * HRate;
        System.out.println("YOUR ESTIMATED PRICE IS: " + priceEsti);

        int premEsti = week * WiRate + day * DiRate + hour * KRate;
        System.out.println("YOUR ESTIMATED PREMIUM IS: " + premEsti);

        // check RoadStar status of customer (WAITING FOR KATRINA'S CUSTOMER.JAVA)
//        Customer customer = new Customer(dLicense);
//        if (customer.getIsRoadStar()) {
//            priceEsti += premEsti / 2;
//        } else {
//            priceEsti += premEsti;
//        }

        // calc additional equipment cost (ADDITIONAL EQUIPMENT NOT DONE)
//        priceEsti += (week * 7 + day) * equipDailyRate;
//        priceEsti += hour * equipHourlyRate;

        return priceEsti;
    }

    public void setConfNo(int confNo) {
        this.id = confNo;
    }

    public int getConfNo() {
        return id;
    }

    public void setDLicense(String dLicense) {
        this.dLicense = dLicense;
    }

    public String getDLicense() {
        return dLicense;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromTime(Timestamp fromTime) {
        this.fromTime = fromTime;
    }

    public Timestamp getFromTime() {
        return fromTime;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToTime(Timestamp toTime) {
        this.toTime = toTime;
    }

    public Timestamp getToTime() {
        return toTime;
    }

    private String addQuotation(String x) {
        return " '" + x + "' ";
    }






}
