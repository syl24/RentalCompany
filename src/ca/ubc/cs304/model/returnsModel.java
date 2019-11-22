package ca.ubc.cs304.model;

import java.sql.Date;
import java.sql.Timestamp;

public class returnsModel {
    private final int id;
    private final Date date;
    private final Timestamp time;
    private final int odometer;
    private final String fullTank;
    private final int value;

    public returnsModel(int id, Date date, Timestamp time, int odometer, String fullTank, int value){
        this.id = id;
        this.date = date;
        this.time = time;
        this.odometer = odometer;
        this.fullTank = fullTank;
        this.value = value;
    }

    public int getId(){
        return id;
    }

    public Date getDate(){
        return date;
    }

    public Timestamp getTime(){
        return time;
    }

    public int getOdometer(){
        return odometer;
    }

    public String getFullTank(){
        return fullTank;
    }

    public int getValue(){
        return value;
    }
}
