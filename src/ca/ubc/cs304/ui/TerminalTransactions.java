package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.BranchModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The class is only responsible for handling terminal text inputs. 
 */
public class TerminalTransactions {
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	private static final int INVALID_INPUT = Integer.MIN_VALUE;
	private static final int EMPTY_INPUT = 0;
	private static final String EMPTY_STRING = "";

	private BufferedReader bufferedReader = null;
	private TerminalTransactionsDelegate delegate = null;

	public TerminalTransactions() {
	}

	/**
	 * Displays simple text interface
	 */
	public void showMainMenu(TerminalTransactionsDelegate delegate) {
		this.delegate = delegate;

		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		int choice = INVALID_INPUT;

		while (choice != 5) {
			System.out.println();
			System.out.println("1. Insert branch");
			System.out.println("2. Delete branch");
			System.out.println("3. Update branch name");
			System.out.println("4. Show branch");
			System.out.println("5. Quit");
			System.out.println("6. Customer Login");
			System.out.println("7. Clerk Login");
			System.out.print("Please choose one of the above 5 options: ");

			choice = readInteger(false);

			System.out.println(" ");

			if (choice != INVALID_INPUT) {
				switch (choice) {
					case 1:
						handleInsertOption();
						break;
					case 2:
						handleDeleteOption();
						break;
					case 3:
						handleUpdateOption();
						break;
					case 4:
						delegate.showBranch();
						break;
					case 5:
						handleQuitOption();
						break;
					case 6:
						handleCustomer();
						break;
					case 7:
						handleClerk();
						break;
					default:
						System.out.println(WARNING_TAG + " The number that you entered was not a valid option.");
						break;
				}
			}
		}
	}

	private void handleDeleteOption() {
		int branchId = INVALID_INPUT;
		while (branchId == INVALID_INPUT) {
			System.out.print("Please enter the branch ID you wish to delete: ");
			branchId = readInteger(false);
			if (branchId != INVALID_INPUT) {
				delegate.deleteBranch(branchId);
			}
		}
	}

	private void handleInsertOption() {
		int id = INVALID_INPUT;
		while (id == INVALID_INPUT) {
			System.out.print("Please enter the branch ID you wish to insert: ");
			id = readInteger(false);
		}

		String name = null;
		while (name == null || name.length() <= 0) {
			System.out.print("Please enter the branch name you wish to insert: ");
			name = readLine().trim();
		}

		// branch address is allowed to be null so we don't need to repeatedly ask for the address
		System.out.print("Please enter the branch address you wish to insert: ");
		String address = readLine().trim();
		if (address.length() == 0) {
			address = null;
		}

		String city = null;
		while (city == null || city.length() <= 0) {
			System.out.print("Please enter the branch city you wish to insert: ");
			city = readLine().trim();
		}

		int phoneNumber = INVALID_INPUT;
		while (phoneNumber == INVALID_INPUT) {
			System.out.print("Please enter the branch phone number you wish to insert: ");
			phoneNumber = readInteger(true);
		}

		BranchModel model = new BranchModel(address,
				city,
				id,
				name,
				phoneNumber);
		delegate.insertBranch(model);
	}

	private void handleQuitOption() {
		System.out.println("Good Bye!");

		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				System.out.println("IOException!");
			}
		}

		delegate.terminalTransactionsFinished();
	}

	private void handleUpdateOption() {
		int id = INVALID_INPUT;
		while (id == INVALID_INPUT) {
			System.out.print("Please enter the branch ID you wish to update: ");
			id = readInteger(false);
		}

		String name = null;
		while (name == null || name.length() <= 0) {
			System.out.print("Please enter the branch name you wish to update: ");
			name = readLine().trim();
		}

		delegate.updateBranch(id, name);
	}

	private int readInteger(boolean allowEmpty) {
		String line = null;
		int input = INVALID_INPUT;
		try {
			line = bufferedReader.readLine();
			input = Integer.parseInt(line);
		} catch (IOException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		} catch (NumberFormatException e) {
			if (allowEmpty && line.length() == 0) {
				input = EMPTY_INPUT;
			} else {
				System.out.println(WARNING_TAG + " Your input was not an integer");
			}
		}
		return input;
	}

	private String readLine() {
		String result = null;
		try {
			result = bufferedReader.readLine();
			if (result.length() == 0)
				result = EMPTY_STRING;
		} catch (IOException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result;
	}





	private void handleClerk(){
		//todo
	}

	private void handleCustomer(){
		//todo
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		int choice = INVALID_INPUT;

		while (choice != 4) {
			System.out.println();
			System.out.println("1. Search for vehicles");
			System.out.println("2. Make a reservation");
			System.out.println("3. Return to main menu");
			System.out.print("Please choose one of the above 3 options: ");

			choice = readInteger(false);

			System.out.println(" ");

			if (choice != INVALID_INPUT) {
				switch (choice) {
					case 1:
						handleCustSearch();
						break;

					case 2:
						handleCustReso();
						break;

					case 3:
						showMainMenu(delegate);
						break;

					default:
						System.out.println(WARNING_TAG + " The number that you entered was not a valid option.");
						break;
				}
			}
		}
	}

	private void handleCustSearch(){
		String type = null;
		String loc = null;
		String time = null;

		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		int choice_type = INVALID_INPUT;


		while (type == null || type.length() <= 0){
			System.out.println("1. SUV");
			System.out.println("2. Economy");
			System.out.println("3. Compact");
			System.out.println("4. Standard");
			System.out.println("5. Full");
			System.out.println("6. Truck");
			System.out.print("Please enter a vehicle type: ");

			choice_type = readInteger(true);

			System.out.println(" ");

			if (choice_type != 7) {
				switch (choice_type) {
					case 1:
						type = "SUV";
						break;

					case 2:
						type = "ECONOMY";
						break;

					case 3:
						type = "COMPACT";
						break;

					case 4:
						type = "STANDARD";
						break;

					case 5:
						type = "FULL";
						break;

					case 6:
						type = "TRUCK";
						break;

					default:
						System.out.println(WARNING_TAG + " The number that you entered was not a valid option.");
						break;
				}
			}

			// type = readLine().trim();
		}

		while (loc == null || loc.length() <= 0) {

			System.out.print("Please enter a location: ");
			loc = readLine().trim();
		}

		while (time == null || time.length() <= 0) {
			System.out.print("Please enter a time: ");
			time = readLine().trim();
		}

		System.out.println(" ");

		delegate.customerVehiclesCount(type, loc, time);

		handleViewYN(type, loc, time);
	}

	private void handleViewYN(String type, String loc, String time) {

		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		int viewYN = INVALID_INPUT;

		while (viewYN != 3) {
			System.out.println("1. Yes");
			System.out.println("2. No");
			System.out.print("View these vehicles? ");

			viewYN = readInteger(true);

			System.out.println(" ");

			if (viewYN != 7) {
				switch (viewYN) {
					case 1:
						handleCustView(type, loc, time);
						handleResoYN();
						break;

					case 2:
						handleCustomer();
						break;

					default:
						System.out.println(WARNING_TAG + " The number that you entered was not a valid option.");
						break;
				}
			}
		}
	}

	private void handleCustView(String type, String loc, String time){
		delegate.customerVehiclesView(type, loc, time);
	}

	private void handleResoYN(){
		//stub
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		int resoYN = INVALID_INPUT;

		while (resoYN != 3) {
			System.out.println("1. Yes");
			System.out.println("2. No, return to menu");
			System.out.print("Would you like to reserve one of these vehicles? ");

			resoYN = readInteger(true);

			System.out.println(" ");

			if (resoYN != 7) {
				switch (resoYN) {
					case 1:
						handleCustReso();//type, loc, time);
						//handleResoYN();
						break;

					case 2:
						handleCustomer(); //returns to menu
						break;

					default:
						System.out.println(WARNING_TAG + " The number that you entered was not a valid option.");
						break;
				}
			}
		}
	}

	private void handleCustReso(){
		//todo
	}
}