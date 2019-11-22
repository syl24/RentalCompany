package ca.ubc.cs304.model;

import java.sql.Date;
import java.sql.Timestamp;

public class rentalsModel {
    private final int id;
    private final String vehicleLicense;
    private final String dLicense;
    private final Date fromDate;
    private final Timestamp fromTime;
    private final Date toDate;
    private final Timestamp toTime;
    private final int odometer;
    private final String cardName;
    private final int cardNo;
    private final Date expDate;
    private final int confNo;

    public rentalsModel(int id, String vehicleLicense, String dLicense, Date fromDate, Timestamp fromTime, Date toDate, Timestamp toTime, int odometer, String cardName, int cardNo, Date expDate, int confNo){
        this.id = id;
        this.vehicleLicense = vehicleLicense;
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

    public int getId(){
        return id;
    }

    public String getVehicleLicense(){
        return vehicleLicense;
    }

    public String getdLicense(){
        return dLicense;
    }

    public Date getFromDate(){
        return fromDate;
    }

    public Timestamp getFromTime(){
        return fromTime;
    }

    public Date getToDate(){
        return toDate;
    }

    public Timestamp getToTime(){
        return toTime;
    }

    public int getOdometer(){
        return  odometer;
    }

    public String getCardName(){
        return cardName;
    }

    public int getCardNo(){
        return cardNo;
    }

    public Date getExpDate(){
        return expDate;
    }

    public int getConfNo(){
        return confNo;
    }

}
