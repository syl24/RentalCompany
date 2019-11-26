package ca.ubc.cs304.delegates;

import ca.ubc.cs304.Customer;
import ca.ubc.cs304.model.BranchModel;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedList;

/**
 * This interface uses the delegation design pattern where instead of having
 * the TerminalTransactions class try to do everything, it will only
 * focus on handling the UI. The actual logic/operation will be delegated to the 
 * controller class (in this case Bank).
 *
 * TerminalTransactions calls the methods that we have listed below but 
 * Bank is the actual class that will implement the methods.
 */
public interface TerminalTransactionsDelegate {
	void deleteBranch(String loc, String city);
	void insertBranch(BranchModel model);
	void showBranch();
	void updateBranch(int branchId, String name);


	// Customer transactions
	Integer customerVehiclesCount(String type, String loc);
	void customerVehiclesView(String type, String loc);

	Customer customerLogin(String phone_number, String  name, String address, String dlicense);
	void makeNewReservation(String dlicense, String typeName, Date fromDate, Timestamp fromTime, Date toDate, Timestamp toTime);
	void confirmReso(String dLicense, String typeName, Date fromDate, Timestamp fromTime, Date toDate, Timestamp toTime);


	// Clerk Transactions
	int clerkReso();
	void makeNewRental(int confNo, String vLicense, String dLicense, Date fromDate, Timestamp fromTime, Date toDate, Timestamp toTime,
					   int odometer, String cardName, int cardNo, Date expDate);
	LinkedList<String> getOneCar(String type, String loc, Timestamp fromTime, Timestamp toTime);
	LinkedList getResoInfo(Integer confNo);
	void processReturn(Integer rentID);

	// Database Transactions
	void viewTables(String name);
	void deleteData(String table, String primaryKey, int key);
	void deleteData(String table, String primaryKey, String key);
//    void updateData(String table, String primaryKey, int key, String attribute, );
//	void updateData(String table, String primaryKey, String key);

/*	void insertReservation(Integer confNo, String vtype, String dlicense, String fromdate, String fromtime, String todate, String totime);
	void insertRental(Integer rentalID, String vLicense, String dLicense, Date fromDate, Timestamp fromTime, Date toDate,
					  Timestamp toTime, Integer odometer, String cardName, Integer cardNo, Date expDate, Integer confNo);*/

	void insertCustomer(String phone, String name, String address, String dLicense);

	void updateCustomer(String query);



	void terminalTransactionsFinished();
	void rentReports(String loc, String date);

	void returnReports(String loc, String date);
}