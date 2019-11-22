package ca.ubc.cs304.model;

import java.sql.Date;
import java.sql.Timestamp;

public class TimePeriodModel {
    private final Date fromdate;
    private final Timestamp fromtime;
    private final Date todate;
    private final Timestamp totime;

    public TimePeriodModel(Date fromdate, Timestamp fromtime, Date todate, Timestamp totime){
        this.fromdate = fromdate;
        this.fromtime = fromtime;
        this.todate = todate;
        this.totime = totime;
    }

    public Date getFromdate() {
        return fromdate;
    }

    public Timestamp getFromtime() {
        return fromtime;
    }

    public Date getTodate(){
        return todate;
    }

    public Timestamp getTotime(){
        return totime;
    }
}
