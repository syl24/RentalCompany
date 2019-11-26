package ca.ubc.cs304;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Rent extends DatabaseConnectionHandler {
    private Connection con;
    final int H = 3600000, D = 86400000, W = 604800000;
    private int rentalID;
    private String vLicense;
    private String dLicense;
    private Date fromDate;
    private Timestamp fromTime;
    private Date toDate;
    private Timestamp toTime;
    private int odometer;
    private String cardName;
    private int cardNo;
    private Date expDate;
    private int confNo;
    private int estimate;
    private int vid;

//    private ArrayList<AdditionalEquipment> addEquip;

    public Rent() {
        con = null;

    }
    /*
    Create a Rent object with only rentID as parameter.
     */
    public Rent(int rentID) {
        Connection con = null;
//        this.addEquip = new ArrayList<>();
        // try to connect
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_ktnliu", "a19619155");
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM rentals WHERE " + "rentals_id=" + rentID);

            // add rental info to rental model
            while (rs.next()) {
                this.rentalID = rs.getInt("rental_id");
                this.vLicense = rs.getString("vehicles_license");
                this.confNo = rs.getInt("reservations_confNo");
                this.fromDate = rs.getDate("timeperiod_fromdate");
                this.fromTime = rs.getTimestamp("timeperiod_fromtime");
                this.toDate = rs.getDate("timeperiod_todate");
                this.toTime = rs.getTimestamp("timeperiod_totime");
                this.odometer = rs.getInt("rentals_odometer");
                this.dLicense = rs.getString("customers_dlicense");
                this.cardNo = rs.getInt("rentals_cardno");
                this.expDate = rs.getDate("rentals_expdate");
                Vehicle vehicle = new Vehicle(this.vLicense);
                vid = vehicle.getVID();

            }


            // ADD ADDITIONAL EQUIPMENT IF NEEDED TO THIS RENT MODEL


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
    Create Rent object with these following parameters.
     */
    public Rent (int confNo, String vLicense, String dLicense, Date fromDate, Timestamp fromTime, Date toDate, Timestamp toTime,
                 int odometer, String cardName, int cardNo, Date expDate) {
        this.vLicense = vLicense;
        this.dLicense = dLicense;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
        this.odometer = odometer;
        this.cardName = cardName;
        this.cardNo = cardNo;
        this.expDate = expDate;
        this.confNo = confNo;

        Vehicle vehicle = new Vehicle(this.vLicense);
        vid = vehicle.getVID();

        // calc estimate for customer
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
                    this.rentalID = rs.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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

    public Rent(int rentalID, String vLicense, String dLicense, Date fromDate, Timestamp fromTime, Date toDate, Timestamp toTime,
                 int odometer, String cardName, int cardNo, Date expDate, int confNo) {
        this.rentalID = rentalID;
        this.vLicense = vLicense;
        this.dLicense = dLicense;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
        this.odometer = odometer;
        this.cardName = cardName;
        this.cardNo = cardNo;
        this.expDate = expDate;
        this.confNo = confNo;
    }

    /*
    Finalizes rent process and inserts rental entry into DB
     */
    public void confRent(int confNo) {

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

/*        String query = "INSERT INTO rentals (rentals_id, vehicles_license, customers_dlicense, timeperiod_fromdate," +
        "timeperiod_fromtime, timeperiod_todate, timeperiod_totime, rentals_odometer, rentals_cardname," +
                "rentals_cardno, rentals_expdate, reservations_confNo)" +
                "values (" + rentalID + dLicense + "," + vLicense + "," + fromDate + "," + fromTime + "," + toDate + "," +
                toTime + "," + odometer + "," + cardName + "," + cardNo + "," + expDate + confNo + ")";*/

        //String query = "INSERT INTO rentals VALUES (" + rentalID + ", '" + vLicense + "', '" + dLicense + "', " + fromDate + ", " + fromTime + ", " + toDate + ", " + toTime + ", " + odometer + ", '" + cardName + "', " + cardNo + ", " + expDate + ", " + confNo +  ")";

        PreparedStatement ps = null;
        Statement stmt = null;
        ResultSet rs;

        String query = "INSERT INTO rentals VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        String timePeriod = "INSERT INTO timeperiod VALUES (?,?,?,?)";

        try {
            stmt = con.createStatement();
            String query2 = "SELECT * FROM timeperiod WHERE (timeperiod_fromdate = TO_DATE ('" + fromDate + "', 'YYYY-MM-DD') " +
                    " AND timeperiod_fromtime = TO_TIMESTAMP ('" + fromTime + "', 'YYYY-MM-DD HH24:MI:SS.FF') " +
                    " AND timeperiod_todate = TO_DATE ('" + toDate + "', 'YYYY-MM-DD') " +
                    " AND timeperiod_totime = TO_TIMESTAMP ('" + toTime + "', 'YYYY-MM-DD HH24:MI:SS.FF'))";
            //SELECT COUNT (*) AS total FROM (SELECT * FROM timeperiod WHERE (timeperiod_fromdate = TO_DATE ('2019-11-20', 'YYYY-MM-DD')  AND timeperiod_fromtime = TO_DATE ('2019-11-20 05:00:00.0', 'YYYY-MM-DD')  AND timeperiod_todate = TO_DATE ('2019-11-25', 'YYYY-MM-DD')  AND timeperiod_totime = TO_DATE ('2019-11-25 05:00:00.0', 'YYYY-MM-DD')))
            rs = stmt.executeQuery("SELECT COUNT (*) AS total FROM (" + query2 + ")");
            while (rs.next()) {
                Integer count = rs.getInt("total");
                if (count <= 0) {
                    ps = con.prepareStatement(timePeriod);
                    ps.setDate(1, fromDate);
                    ps.setTimestamp(2, fromTime);
                    ps.setDate(3, toDate);
                    ps.setTimestamp(4, toTime);

                    ps.executeUpdate();
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, rentalID);
            ps.setString(2, vLicense);
            ps.setString(3, dLicense);
            ps.setDate(4, fromDate);
            ps.setTimestamp(5, fromTime);
            ps.setDate(6, toDate);
            ps.setTimestamp(7, toTime);
            ps.setInt(8, odometer);
            ps.setString(9, cardName);
            ps.setInt(10, cardNo);
            ps.setDate(11, expDate);
            ps.setInt(12, confNo);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.print("Sorry, could not insert this rental");
            System.out.print(" ");
            //e.printStackTrace();
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

    public void setRentalID(int rentalID) {
        this.rentalID = rentalID;
    }

    public int getRentalID() {
        return rentalID;
    }

    public void setdLicense(String dLicense) {
        this.dLicense = dLicense;
    }

    public String getdLicense() {
        return dLicense;
    }

    public void setVLicense(String vLicense) {
        this.vLicense = vLicense;
    }

    public String getVLicense() {
        return vLicense;
    }

    public void setConfNo(int confNo) {
        this.confNo = confNo;
    }

    public int getConfNo() {
        return confNo;
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

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setCardNo(int cardNo) {
        this.cardNo = cardNo;
    }

    public int getCardNo() {
        return cardNo;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardName() {
        return cardName;
    }

    public Vehicle getVehicle() {
        Vehicle vehicle = new Vehicle(vLicense);
        return vehicle;
    }







}

