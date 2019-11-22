package ca.ubc.cs304;

public abstract class VehicleType {
    private String typeName;
    private int WRate;
    private int DRate;
    private int HRate;
    private int WiRate;
    private int DiRate;
    private int KRate;
    //

    public VehicleType() {
        typeName = this.typeName;
        WRate = this.WRate;
        DRate = this.DRate;
        HRate = this.HRate;
        WiRate = this.WiRate;
        DiRate = this.DiRate;
        KRate = this.KRate;

    }
//    java.sql.Connection con = Connection.getInstance().getConnection();
    public int getWRate() {
        return WRate;
    }

    public int getDRate() {
        return DRate;
    }

    public int getHRate() {
        return HRate;
    }

    public int getWiRate() {
        return WiRate;
    }

    public int getDiRate() {
        return DiRate;
    }

    public int getKRate() {
        return KRate;
    }

}
