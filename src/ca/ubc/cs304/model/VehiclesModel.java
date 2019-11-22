package ca.ubc.cs304.model;

public class VehiclesModel {
    private final int id;
    private final String license;
    private final String make;
    private final String model;
    private final int year;
    private final String color;
    private final int odometer;
    private final String status;
    private final String vehicleTypeName;
    private final String branchLocation;
    private final String branchCity;

    public VehiclesModel(int id, String license, String make, String model, int year, String color, int odometer, String status, String vehicleTypeName, String branchLocation, String branchCity){
        this.id = id;
        this.license = license;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.odometer = odometer;
        this.status = status;
        this.vehicleTypeName = vehicleTypeName;
        this.branchLocation = branchLocation;
        this.branchCity = branchCity;
    }

    public int getId(){
        return id;
    }

    public String getLicense(){
        return license;
    }

    public String getMake(){
        return make;
    }

    public String getModel(){
        return model;
    }

    public int getYear(){
        return year;
    }

    public String getColor(){
        return color;
    }

    public int getOdometer(){
        return odometer;
    }

    public String getStatus(){
        return status;
    }

    public String getVehicleTypeName(){
        return vehicleTypeName;
    }

    public String getBranchLocation(){
        return branchLocation;
    }

    public String getBranchCity(){
        return branchCity;
    }

}
