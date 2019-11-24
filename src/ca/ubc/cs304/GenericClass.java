package ca.ubc.cs304;

import java.sql.*;
import java.util.List;

public abstract class GenericClass<T> {
    protected Connection scon;
    protected GenericClass(Connection con) {
        this.scon = con;
    }

    public abstract List<T> getAll() throws SQLException;

    protected int exeUpdateSQL(String com) throws SQLException {
        Statement stmt = this.scon.createStatement();
        int countRow = stmt.executeUpdate(com);
        this.scon.commit();
        stmt.close();
        if (countRow == 0) {
            throw new SQLException("ERROR: RESOURCE HAS NOT BEEN UPDATED");
        }
        return countRow;
    }

    protected int exeUpdateSQL(String data, List<Integer> types, List<Object> values) throws SQLException {
        PreparedStatement ppst = this.scon.prepareStatement(data);
        setupPPST(ppst, types, values);
        int countRow = ppst.executeUpdate();
        this.scon.commit();
        ppst.close();
        return countRow;
    }

    protected ResultSet exeQuerySQL(String data) throws SQLException {
        Statement stmt = this.scon.createStatement();
        return stmt.executeQuery(data);
    }

    protected ResultSet exeQuerySQL(String data, List<Integer> types, List<Object> values) throws SQLException {
        PreparedStatement ppst = this.scon.prepareStatement(data);
        setupPPST(ppst, types, values);
        return ppst.executeQuery();
    }

    protected void setupPPST(PreparedStatement ppst, List<Integer> types, List<Object> values) throws SQLException {
        if (types.size() != values.size()) {
            throw new SQLException("ERROR: LENGTH COUNT MISMATCH BETWEEN TYPES AND VALUES ARRAY");
        }
        for (int i = 0; i < types.size(); i++) {
            int x = types.get(i);
            Object y = values.get(i);
            switch(x) {
                case Types.INTEGER:
                    if (y == null) {
                        ppst.setNull(i + 1, Types.INTEGER);
                    } else {
                        ppst.setInt(i + 1, (int) y);
                    }
                    break;
                case Types.CHAR:
                    if (y == null) {
                        ppst.setNull(i + 1, Types.CHAR);
                    } else {
                        ppst.setString(i + 1, (String) y);
                    }
                    break;
                case Types.TIME:
                    if (y == null) {
                        ppst.setNull(i + 1, Types.TIME);
                    } else {
                        ppst.setTime(i + 1, (Time) y);
                    }
                    break;
                case Types.DATE:
                    if (y == null) {
                        ppst.setNull(i + 1, Types.DATE);
                    } else {
                        ppst.setDate(i + 1, (Date) y);
                    }
                    break;
                default:
                    throw new SQLException("ERROR: TYPE UNKNOWN - " + x + "HAS BEEN PASSED TO setupPPST");
            }
        }
    }






}
