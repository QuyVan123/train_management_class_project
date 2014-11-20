package model;

//
/*A Model specific for clients
 * 		1)contains methods to get and set variables
 * 		2)contains methods to create a unique ID
 * 
 * Contains:
 * ID, firstName, lastName, address, phone, email, sex, birthday, dateJoined, listOfReservations
 * 
 * The following are user inputed:
 * firstName, lastName, address, phone, email, sex, birthday
 * 
 * The following are generated by class:
 * ID, dateJoined, listOfReservations
 * 
 * @deprecated equals(Object), method not used
 */


import java.io.*;
import java.util.*;

public class Client extends Model {

	// attributes
	private String firstName, lastName;
	private String address, email, sex;
	private String listOfReservations;// should be saved with dots (".") between each reservation
										//needs to be implemented****
	private String phone;
	private String birthday;
	private String dateJoined; // generated, current date

	public Client() throws IOException {
		this(null);
		setDateJoined();
		listOfReservations = "-1";
	}

	public Client(Integer id) throws IOException {
		super("clients.csv", id);
	}

	//method used to set all attribute values with string
	@Override
	public void set(String key, Object value) {
		if (key.equals("id"))
			setID(value);
		else if (key.equals("firstName"))
			firstName = (String)value;
		else if (key.equals("lastName"))
			lastName = (String)value;
		else if (key.equals("address"))
			address = (String)value;
		else if (key.equals("phone"))
			phone = (String)value;
		else if (key.equals("email"))
			email = (String)value;
		else if (key.equals("sex"))
			sex = (String)value;
		else if (key.equals("birthday"))
			birthday =(String)value;	
		else if (key.equals("dateJoined"))
			dateJoined = (String) value;
		else if (key.equals("listOfReservations"))
		{
			listOfReservations = (String)value;
		}

	}
	
	//method used to get all attribute values as a String
	@Override
	public Object get(String key) {
		if (key.equals("id"))
			return id;
		else if (key.equals("firstName"))
			return firstName;
		else if (key.equals("lastName"))
			return lastName;
		else if (key.equals("address"))
			return address;
		else if (key.equals("phone"))
			return phone;
		else if (key.equals("email"))
			return email;
		else if (key.equals("sex"))
			return sex;
		else if (key.equals("birthday"))
			return birthday;
		else if (key.equals("dateJoined"))
			return dateJoined;
		else if (key.equals("listOfReservations"))
		{
			return listOfReservations;
		}
		else
			throw new IllegalArgumentException("Invalid key: " + key);
	}
	
	@Override
	protected Model createNew() {
		try {
			return new Client();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	//method used to set the dateJoined of client
	//used only in no arg constructor
	protected void setDateJoined() {
		dateJoined = dateFormatDay.format(new Date());
	}
	
	//method used to check if the client is unique (email, birthday)
	//true if it is unique, false if not
	public boolean checkUniqueClient() throws IOException
	{
		Model[] all = loadAll();
		
		for (Model m: all)
		{	
			if (((String)(m.get("email"))).equals((String)(this.get("email"))) &&
				(((String)(m.get("birthday"))).equals((String)(this.get("birthday")))) && !(m.get("id")+"").equals(this.get("id") + ""))
			{
				return false;
			}
		}
		return true;


	}
	
	public String toString()
	{
		return ("ID: " + id +
				" First Name: " + firstName +
				" Last Name: " + lastName +
				" Address: " + address +
				" Phone: " + phone +
				" Email: " + email+
				" Sex: " + sex+
				" Birthday: " + birthday+
				"  Date Joined: " + dateJoined +
				"Reservations id(s): " + listOfReservations);
	}
	//checks if the ID's are equals, which means that they should be the same
	//may cause complications if used with different Model Types
	@Deprecated
	public boolean equals(Object o) {
		return (o instanceof Client) && (((Client) o).id == this.id);
	}
}