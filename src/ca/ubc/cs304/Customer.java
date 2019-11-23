package ca.ubc.cs304;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Customer {
    private String dLicense;
    private String name;
    private String adrs;
    private String cellPhone;

    public Customer(String dLicense) {
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:stu", "ora_colenliu", "a15539159");
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM customers WHERE customers_dlicense = " + dLicense);

            while (rs.next()) {
                this.dLicense = rs.getString("customers_dlicense");
                this.name = rs.getString("customers_name");
                this.adrs = rs.getString("customers_address");
                this.cellPhone = rs.getString("customers_cellphone");
            }

    } catch (SQLException e) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
        }
    }




}
