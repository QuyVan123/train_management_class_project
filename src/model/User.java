package model;

//
/*A Model specific for clients
 * 		1)contains methods to get and set variables
 * 		2)contains methods to create a unique ID
 * 		3)contains methods to determine type
 * 
 * 3 different types of users: Staff, Admin, Manager
 * 
 * Contains:
 * username, password, user_type
 * 
 * The following are user inputed:
 * username, password, user_type
 * 
 * @deprecated equals(Object), method not used
 */

import java.io.*;

public class User extends Model
{
 private static final int ADMIN=1;
 private static final int MANAGER=2;
 private static final int STAFF=3;
 
 private String username;
 private String password;
 public int user_type;
 private String message;
 
  public User() throws IOException
  {
	  this(null);
	  message = "=1";
  }
 
  public User(String username) throws IOException
  {
	  super("users.csv",username);
  }
  
  public int getUserType()
  {
	  return user_type;
  }
  
  public boolean isAdmin()
  {
	  return user_type==ADMIN;
  }

  public boolean isManager()
  {
	  return user_type==MANAGER;
  }
  
  public boolean isStaff()
  {
	  return user_type==STAFF;
  }
  
  @Override
  protected Object getID()
  {
	  return username;
  }
  
  @Override
  protected void setID(Object val)
  {
	  this.username=(String)val;
  }
  
  @Override
  protected Model createNew()
  {
	  try 
	  {
		  return new User();
	  }
	  catch (IOException ioe)
	  {
		  ioe.printStackTrace();
		  System.out.println("Weird!  IOException while not loading from a file.");
		  return null;
	  }
  }
   
//method used to get all attribute values as a String
  @Override
  public Object get(String key)
  {
	  if (key.equals("username"))
		  return username;
	  else if (key.equals("password"))
		  return password;
	  else if (key.equals("type"))
		  return ""+user_type;
	  else if (key.equals("message"))
		  return ""+ message;
	  else
		  throw new IllegalArgumentException("Invalid key: "+key);
  }
  
  @Override
  public boolean checkUnique(Object input1, Object input2, Object input3) throws IOException
	{
		String userNameCheck = (String)get("username");
		Model[] all = loadAll();
		int i;
		boolean uniqueFlag1;
		boolean uniqueFlag2;
		boolean uniqueFlag3;
		int index1 =-1;
		int index2 =-1;
		int index3 =-1;
		int len = fields.keySet().size();
		
		//determines which arguments were inputted from user
		if (input1==null)
		{
			uniqueFlag1=false;
		}
		else
		{
			uniqueFlag1=true;
		}
		if (input2==null)
		{
			uniqueFlag2=false;
		}
		else
		{
			uniqueFlag2=true;
		}
		if(input3==null)
		{
			uniqueFlag3=false;
		}
		else
		{
			uniqueFlag3=true;
		}

		//gets the index the attribute is located in Hashmap fields
		for (i = 0; i <len; i++)
		{
			if (fields.get(i).equals(input1))
			{
				index1=i;
			}

			if (fields.get(i).equals(input2))
			{
				index2=i;
			}

			if (fields.get(i).equals(input3))
			{
				index3=i;
			}

		}
		
		//checks to see if any other Models have the same value of attributes
		for (Model m: all)
		{	
			if ((String)userNameCheck == (String)m.get("username"))
			{
				if (index1!=-1)
				{
					if (m.get(fields.get(index1)).equals(this.get(fields.get(index1))) )
					{
						uniqueFlag1=false;
					}
				}
				if (index2!=-1)
				{
					if (m.get(fields.get(index2)).equals(this.get(fields.get(index2))))
					{
						uniqueFlag2=false;
					}
				}
				if (index3!=-1)
				{
					if (m.get(fields.get(index3)).equals(this.get(fields.get(index3))))
					{
						uniqueFlag3=false;
					}
				}
			}

		}
		
		//if there is an attribute with same value, returns false
		if (!uniqueFlag1 && !uniqueFlag2 && !uniqueFlag3)
		{
			return false;
		}
		// if there is no attribute with same value, return true
		else
		{
			return true;
		}

	}
  
//method used to set all attribute values with string
  @Override
  public void set(String key,Object value)
  {
	  if (key.equals("username"))
		  this.username=(String)value;
	  else if (key.equals("password"))
		  this.password=(String)value;
	  else if (key.equals("message"))
		  this.message=(String)value;
	  else if (key.equals("type"))
	  {
		  int typeInt = Integer.parseInt((String)value);
		  user_type=typeInt;
	  }
  }

	//adds the current Model to it's .csv file
  @Override
	public void addToFile() throws IOException
	{
		Model[] all = loadAll();
		PrintStream out = new PrintStream(new FileOutputStream(filename));
		out.println(getFirstLine());

		for (Model m: all)
		{
			out.println(m.toCSV());
		}

		out.println(this.toCSV());
		out.flush();
		out.close();  
	}
	//checks if the ID's are equals, which means that they should be the same
	//may cause complications if used with different Model Types
	@Deprecated
	public boolean equals(Object o)
	{
		return (o instanceof User) 
				&& ((User)o).username.equals(this.username);
	}

}
