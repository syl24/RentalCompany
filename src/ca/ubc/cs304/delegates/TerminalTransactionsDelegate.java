package ca.ubc.cs304.delegates;

import ca.ubc.cs304.Customer;
import ca.ubc.cs304.model.BranchModel;

import java.sql.Date;
import java.sql.Timestamp;

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
	void deleteBranch(int branchId);
	void insertBranch(BranchModel model);
	void showBranch();
	void updateBranch(int branchId, String name);

	// Customer transactions
	Integer customerVehiclesCount(String type, String loc);
	void customerVehiclesView(String type, String loc);

	Customer customerLogin(String phone_number, String  name, String address, String dlicense);
	void makeNewReservation(String dlicense, String typeName, Date fromDate, Timestamp fromTime, Date toDate, Timestamp toTime);
	void confirmReso();

	// Clerk Transactions

	void terminalTransactionsFinished();
}