package ca.ubc.cs304;

import java.sql.*;

public class Reservation {
    Connection con = null;
    final int H = 3600000, D = 86400000, W = 604800000;
    private int id; //confno
    private String dLicense;
    private String vLicense;
    private String typeName;
    private Date fromDate;
    private Timestamp fromTime;
    private Date toDate;
    private Timestamp toTime;
    private int estimate;

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
            con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_ktnliu", "a19619155");
            ResultSet rs = con.createStatement().executeQuery("SELECT COUNT (*) AS total FROM (SELECT * FROM reservations WHERE reservations_confNo LIKE " + confNo + ")");


            while (rs.next()){
                Integer count = rs.getInt("total");
                System.out.println(" ");
                if (count <= 0) {
                    System.out.println("Sorry! That reservation doesn't exist.");
                    System.out.println(" ");
                    con.close();
                }
                else{
                    rs = con.createStatement().executeQuery("SELECT * FROM reservations WHERE reservations_confNo LIKE " + confNo);

                    while (rs.next()) {
                        this.id = rs.getInt("reservations_confNo");
                        this.dLicense = rs.getString("customers_dlicense");
                        this.typeName = rs.getString("vehicletypes_name");
                        this.fromDate = rs.getDate("timeperiod_fromdate");
                        this.fromTime = rs.getTimestamp("timeperiod_fromtime");
                        this.toDate = rs.getDate("timeperiod_todate");
                        this.toTime = rs.getTimestamp("timeperiod_totime");
                    }
                }
            }
            // additional eqipment?
            con.close();

            // adding the generated results from above into our reservations model
            // adding any additional equipment?
        } catch (SQLException e) {
            //Logger.getLogger(Reservation.class.getName()).log(Level.SEVERE, null, e);
            //e.printStackTrace();
            //System.out.println("Goodbye");

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
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                try {
                    this.id = rs.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // close when done
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public Reservation(int confNo, String vtype, String dlicense, Date fromdate, Timestamp fromtime, Date todate, Timestamp totime){
        this.id = confNo;
        this.dLicense = dLicense;
        this.typeName = typeName;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
    }


    // returns -1 if confReso was not successful
    // actually inserts reso into database
    public void confReso(int id, String dLicense, String typeName, Date fromDate, Timestamp fromTime, Date toDate, Timestamp toTime) {
        int confNo = -1;

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

        /*String SQL = "INSERT into reservations (reservations_confNo, vehicletypes_name, customers_dlicense, timeperiod_fromdate, timeperiod_fromtime, " +
                "timeperiod_todate, timeperiod_totime)" +
                " values (" + id + "," + addQuotation(dLicense) + "," + addQuotation(typeName) + "," + addQuotation(fromDate.toString()) + "," +
                addQuotation(fromTime.toString()) + "," + addQuotation(toDate.toString()) + "," + addQuotation(toTime.toString()) + ")";*/

        PreparedStatement ps = null;
        Statement stmt = null;
        ResultSet rs;

        String timePeriod = "INSERT INTO timeperiod VALUES (?,?,?,?)";
        String SQL = "INSERT INTO reservations VALUES (?,?,?,?,?,?,?)";

        try {
            stmt = con.createStatement();
            String query = "SELECT * FROM timeperiod WHERE (timeperiod_fromdate = TO_DATE ('" + fromDate + "', 'YYYY-MM-DD') " +
                    " AND timeperiod_fromtime = TO_TIMESTAMP ('" + fromTime + "', 'YYYY-MM-DD HH24:MI:SS.FF') " +
                    " AND timeperiod_todate = TO_DATE ('" + toDate + "', 'YYYY-MM-DD') " +
                    " AND timeperiod_totime = TO_TIMESTAMP ('" + toTime + "', 'YYYY-MM-DD HH24:MI:SS.FF'))";
            //SELECT COUNT (*) AS total FROM (SELECT * FROM timeperiod WHERE (timeperiod_fromdate = TO_DATE ('2019-11-20', 'YYYY-MM-DD')  AND timeperiod_fromtime = TO_DATE ('2019-11-20 05:00:00.0', 'YYYY-MM-DD')  AND timeperiod_todate = TO_DATE ('2019-11-25', 'YYYY-MM-DD')  AND timeperiod_totime = TO_DATE ('2019-11-25 05:00:00.0', 'YYYY-MM-DD')))
            rs = stmt.executeQuery("SELECT COUNT (*) AS total FROM (" + query +")");
            while (rs.next()){
                Integer count = rs.getInt("total");
                if (count <= 0){
                    ps = con.prepareStatement(timePeriod);
                    ps.setDate(1, fromDate);
                    ps.setTimestamp(2, fromTime);
                    ps.setDate(3, toDate);
                    ps.setTimestamp(4, toTime);

                    ps.executeUpdate();
                }
            }

            ps = con.prepareStatement(SQL);
            ps.setInt(1, id);
            ps.setString(2, typeName);
            ps.setString(3, dLicense);
            ps.setDate(4, fromDate);
            ps.setTimestamp(5, fromTime);
            ps.setDate(6, toDate);
            ps.setTimestamp(7, toTime);

            ps.executeUpdate();

            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /*
    Cancel reservation that was booked already
     */
    public void cancelReso(int id) {
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_ktnliu", "a19619155");
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
