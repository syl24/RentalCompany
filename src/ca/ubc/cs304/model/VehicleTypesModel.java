package ca.ubc.cs304.model;

public class VehicleTypesModel {

    private final String name;
    private final int weekrate;
    private final int dayrate;
    private final int hourrate;
    private final int wirate;
    private final int dirate;
    private final int krate;

    public VehicleTypesModel(String name, int weekrate, int dayrate, int hourrate, int wirate, int dirate, int krate) {
        this.name= name;
        this.weekrate = weekrate;
        this.dayrate = dayrate;
        this.hourrate = hourrate;
        this.wirate = wirate;
        this.dirate = dirate;
        this.krate = krate;
    }

    public String getName() {
        return name;
    }

    public int getWeekrate() {
        return weekrate;
    }

    public int getDayrate(){
        return dayrate;
    }

    public int getHourrate(){
        return hourrate;
    }

    public int getWirate(){
        return wirate;
    }

    public int getDirate(){
        return dirate;
    }

    public int getKrate(){
        return krate;
    }

}
