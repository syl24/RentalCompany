package ca.ubc.cs304.delegates;

import ca.ubc.cs304.model.BranchModel;

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

	public void customerVehiclesCount(String type, String loc, String time);
	public void customerVehiclesView(String type, String loc, String time);
	public void customerMakeReservation(String key);
	
	void terminalTransactionsFinished();
}
