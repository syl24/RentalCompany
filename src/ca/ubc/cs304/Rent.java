package ca.ubc.cs304;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Rent extends DatabaseConnectionHandler {
    private Connection con;
    final int H = 3600000, D = 86400000, W = 604800000;
    private String dLicense;
    private String vLicense;
    private int confNo;
    private int rentalID;
    private Date fromDate;
    private Timestamp fromTime;
    private Date toDate;
    private Timestamp toTime;
    private int estimate;
    private int cardNo;
    private Date expDate;
    private int odometer;
    private String cardName;
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
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:stu", "ora_colenliu", "a15539159");
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

    }

    /*
    Create Rent object with these following parameters.
     */
    public Rent (String dLicense, int confNo, String vLicense, Date fromDate, Timestamp fromTime, Date toDate, Timestamp toTime,
                 int odometer, String cardName, int cardNo, Date expDate) {
        this.dLicense = dLicense;
        this.confNo = confNo;
        this.vLicense = vLicense;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
        this.odometer = odometer;
        this.cardNo = cardNo;
        this.cardName = cardName;
        this.expDate = expDate;

        Vehicle vehicle = new Vehicle(this.vLicense);
        vid = vehicle.getVID();

        // calc estimate for customer
        this.estimate = estiPrice();
    }

    /*
    Finalizes rent process and inserts rental entry into DB
     */
    public void confRent() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:stu", "ora_colenliu", "a15539159"); {
                try (PreparedStatement ppst = con.prepareStatement("INSERT INTO rentals (customers_dlicense, vehicles_license" +
                        "timeperiod_fromdate, timeperiod_fromtime, timeperiod_todate, timeperiod_totime, rentals_odometer," +
                        "rentals_cardname, rentals_cardno, rentals_expdate)" +
                        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                    System.out.println("INSERT INTO rentals (customers_dlicense, vehicles_license, timeperiod_fromdate," +
                            "timeperiod_fromtime, timeperiod_todate, timeperiod_totime, rentals_odometer, rentals_cardname," +
                            "rentals_cardno, rentals_expdate)" +
                            "values (" + dLicense + "," + vLicense + "," + fromDate + "," + fromTime + "," + toDate + "," +
                            toTime + "," + odometer + "," + cardName + "," + cardNo + "," + expDate + ")");

                    ppst.setString(1, dLicense);
                    ppst.setString(2, vLicense);
                    ppst.setDate(3, fromDate);
                    ppst.setTimestamp(4, fromTime);
                    ppst.setDate(5, toDate);
                    ppst.setTimestamp(6, toTime);
                    ppst.setInt(7, odometer);
                    ppst.setString(8, cardName);
                    ppst.setInt(9, cardNo);
                    ppst.setDate(10, expDate);

                    ppst.executeUpdate();

                    // test to see if rent operation successful
                    try (ResultSet rs = ppst.getGeneratedKeys()) {
                        while (rs.next()) {
                            this.dLicense = rs.getString(1);
                        }
                    }
                }
            }


        } catch (SQLException e) {
            Logger.getLogger(Reservation.class.getName()).log(Level.SEVERE, null, e);
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







}

