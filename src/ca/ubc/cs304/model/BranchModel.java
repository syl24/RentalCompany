package ca.ubc.cs304.model;

/*
 * The intent for this class is to update/store information about a single branch
 */
public class BranchModel {
	private final String location;
	private final String city;

	
	public BranchModel(String location, String city) {
		this.location= location;
		this.city = city;
	}

	public String getAddress() {
		return location;
	}

	public String getCity() {
		return city;
	}

}
