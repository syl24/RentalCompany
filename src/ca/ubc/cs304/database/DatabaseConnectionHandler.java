package ca.ubc.cs304.database;

import ca.ubc.cs304.model.BranchModel;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";

	private Connection connection = null;


//	public DatabaseConnectionHandler(){}

	public DatabaseConnectionHandler() {
		try {
			// Load the Oracle JDBC driver
			// Note that the path could change for new drivers
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
/*		int lport;
		String rhost;
		int rport;
		public void go(){
			String user = "sshusername";
			String password = "sshpassword";
			String host = "remote.students.cs.ubc.ca";
			int port=22;
			try
			{
				JSch jsch = new JSch();
				Session session = jsch.getSession(user, host, port);
				lport = 4321;
				rhost = "dbhost.students.cs.ubc.ca";
				rport = 1522;
				session.setPassword(password);
				session.setConfig("StrictHostKeyChecking", "no");
				System.out.println("Establishing Connection...");
				session.connect();
				int assinged_port=session.setPortForwardingL(lport, rhost, rport);
				System.out.println("localhost:"+assinged_port+" -> "+rhost+":"+rport);
			}
			catch(Exception e){System.err.print(e);}
		}
		public boolean login(String username, String password) {
            try {
                if (connection != null) {
                    connection.close();
                }
                go();
                String url = "jdbc:oracle:thin:@localhost:1522:stu";
                connection = DriverManager.getConnection(url, "ora_ktnliu", "a19619155");
                connection.setAutoCommit(false);
                System.out.println("\nConnected to Oracle!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }*/
	}

	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public void deleteBranch(String loc, String city) {
		try {
			connection = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_ktnliu", "a19619155");
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM branch WHERE branch_location = '"+ loc +"' AND branch_city = '" + city + "'");
//			ps.setString(1, loc);
//			ps.setString(2, city);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " Branch " + loc + " in " + city + " does not exist!");
			}
			else{
			connection.commit();
			System.out.print("Deleted " + loc + " in " + city + " from BRANCH!");
			System.out.print(" ");
			}


			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}

	// delete for resos, rentals, returns
	public void deleteData(String table, String primaryKey, int key) {
		try {
			connection = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_ktnliu", "a19619155");
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM " + table + " WHERE " + primaryKey + " LIKE " + key );
//			ps.setString(1, primaryKey);
//			ps.setInt(2, key);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " "+ table +" " + key + " does not exist!");
				System.out.print(" ");

			}

			connection.commit();
			System.out.print("Deleted "+ key + " from " + table + "!");
			System.out.print(" ");


			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}

		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// delete for vtype, customer, vehicle
	public void deleteData(String table, String primaryKey, String key) {
		try {
			connection = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_ktnliu", "a19619155");
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM " + table + " WHERE " + primaryKey + " LIKE '" + key + "'");
//			ps.setString(1, primaryKey);
//			ps.setString(2, key);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " "+ table +" " + key + " does not exist!");
				System.out.print(" ");

			}

			connection.commit();
			System.out.print("Deleted "+ key + " from " + table + "!");
			System.out.print(" ");


			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			e.printStackTrace();
			rollbackConnection();
		}

		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void insertBranch(BranchModel model) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO branch VALUES (?,?,?,?,?)");
			ps.setInt(1, model.getId());
			ps.setString(2, model.getName());
			ps.setString(3, model.getAddress());
			ps.setString(4, model.getCity());
			if (model.getPhoneNumber() == 0) {
				ps.setNull(5, java.sql.Types.INTEGER);
			} else {
				ps.setInt(5, model.getPhoneNumber());
			}

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}

	public BranchModel[] getBranchInfo() {
		ArrayList<BranchModel> result = new ArrayList<BranchModel>();

		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM branch");

//    		// get info on ResultSet
//    		ResultSetMetaData rsmd = rs.getMetaData();
//
//    		System.out.println(" ");
//
//    		// display column names;
//    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
//    			// get column name and print it
//    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
//    		}

			while(rs.next()) {
				BranchModel model = new BranchModel(rs.getString("branch_addr"),
						rs.getString("branch_city"),
						rs.getInt("branch_id"),
						rs.getString("branch_name"),
						rs.getInt("branch_phone"));
				result.add(model);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}

		return result.toArray(new BranchModel[result.size()]);
	}

	public void updateBranch(int id, String name) {
		try {
			PreparedStatement ps = connection.prepareStatement("UPDATE branch SET branch_name = ? WHERE branch_id = ?");
			ps.setString(1, name);
			ps.setInt(2, id);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " Branch " + id + " does not exist!");
			}

			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}

	public boolean login(String username, String password) {
		try {
			if (connection != null) {
				connection.close();
			}

			connection = DriverManager.getConnection(ORACLE_URL, username, password);
			connection.setAutoCommit(false);

			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return false;
		}
	}

	private void rollbackConnection() {
		try  {
			connection.rollback();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public void updateCustomer(String query) {
		try {
			connection = DriverManager.getConnection("jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu", "ora_ktnliu", "a19619155");
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("Sorry, could not update with that query");
			try {
				connection.close();
			} catch (SQLException ex) {
				//ex.printStackTrace();
			}
		}

		try {
			PreparedStatement ps = connection.prepareStatement(query);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " Customer does not exist!");
				System.out.print(" ");
			}
			connection.commit();
			System.out.print("Updated customer!");


			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			//e.printStackTrace();
			rollbackConnection();
		}

		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}