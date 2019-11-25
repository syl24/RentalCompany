package ca.ubc.cs304;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Branch {
    private String bLocation;
    private String bCity;

    public Branch(String bLocation, String bCity) {
        // trying to connect
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_ktnliu", "a19619155");
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM vehicles WHERE branch_location = " + bLocation + "AND branch_city = " + bCity); {

                while(rs.next()) {
                    this.bCity = rs.getString("branch_city");
                    this.bLocation = rs.getString("branch_location");
                }

                con.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(Branch.class.getName()).log(Level.SEVERE, null, e);
        }


    }

    public String getbCity() {
        return bCity;
    }

    public String getbLocation() {
        return bLocation;
    }
}
