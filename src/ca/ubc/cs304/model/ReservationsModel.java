package ca.ubc.cs304.model;

import java.sql.Date;
import java.sql.Timestamp;

public class ReservationsModel {
    private final int confNo;
    private final String vehicleTypeName;
    private final String dlicense;
    private final Date fromDate;
    private final Timestamp fromTime;
    private final Date toDate;
    private final Timestamp toTime;

    public ReservationsModel(int confNo, String vehicleTypeName, String dlicense, Date fromDate, Timestamp fromTime, Date toDate, Timestamp toTime){
        this.confNo = confNo;
        this.vehicleTypeName = vehicleTypeName;
        this.dlicense = dlicense;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
    }

    public int getConfNo(){
        return confNo;
    }

    public String getVehicleTypeName(){
        return vehicleTypeName;
    }

    public String getDlicense(){
        return dlicense;
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

}
