package model;

//ABSTRACT
/*Used to hold information in this program
 * 		1)
 * 
 * Contains:
 * boolean loaded, HashMap fields, String filename;
 * 
 * 
 * @deprecated Model(String), not used
*/

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class Model 
{
	protected boolean loaded;
	protected String filename;
	protected int id; // uniquely generated, must be in all Model types
	protected final DateFormat dateFormatDay = new SimpleDateFormat("dd/MM/yy");
	protected final DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
	protected HashMap<Integer, String> fields = new HashMap<Integer,String>();
 
	@Deprecated
	public Model(String filename) throws IOException
	{
		this(filename,null);
	}
	
	//Constructor 
	// 1)links the model type and filename
	// 2)sets id
	public Model(String filename,Object idValue) throws IOException
	{
		this.filename="data/"+filename;
		if (idValue!=null)
		{
			this.setID(idValue);
			load();
		}
	}
	
	//method used to return ID
	protected Object getID() {

		return id;
	}
	
	//method used to set id as an int
	protected void setID(Object val) {
		if (val!=null)
		{
			id = Integer.parseInt("" + val);;
		}
		
	}
	
	//used to read first line from .csv file
	//the first line represents all the attributes for that model type
	//the line is tokenized, and each token becomes an index in the Hashmap fields
	protected void loadFields(String line)
	{
		StringTokenizer st = new StringTokenizer(line,",\r\n");
		int index=0;
	  
		while (st.hasMoreTokens())
		{
			fields.put(index, st.nextToken());
			index++;
		}
	}

	//returns the HashMap, field's, index's, .csv acceptable string
	protected String getFirstLine()
	{
		String firstLine="";
		boolean comma_needed=false;
		for (int i=0;i<fields.keySet().size();i++)
		{
			if (comma_needed)
				firstLine+=",";
    	  
			firstLine+=fields.get(i);
			comma_needed=true;
		}
		return firstLine;
	}

	//deletes the current Model from it's .csv file
	public void delete() throws IOException
	{
		if (!loaded) // element doesn't exist.
			return;

		Model[] all = loadAll();
		PrintStream out = new PrintStream(new FileOutputStream(filename));

		out.println(getFirstLine());

		for (Model m: all)
		{
			if (!m.getID().equals(this.getID()))
			{
				out.println(m.toCSV());
			}
		}
		out.flush();
		out.close();
	}

	//updates it's .csv file with the current model
	public void updateFile() throws IOException
	{	  
		Model[] all = loadAll();
		PrintStream out = new PrintStream(new FileOutputStream(filename));
		out.println(getFirstLine());

		for (Model m: all)
		{
			if (!m.getID().equals(this.getID()))
			{
				out.println(m.toCSV());
			}
			else
			{
				out.println(this.toCSV());
			}
		}
		out.flush();
		out.close();  
	}

	//sets a unique ID
	protected void setUniqueID()
	{
		try
		{
			setUniqueID(loadAll());
		}
		catch (IOException ie)
		{
			ie.printStackTrace();
		}
	}

	//adds the current Model to it's .csv file
	public void addToFile() throws IOException
	{
		
		Model[] all = loadAll();
		PrintStream out = new PrintStream(new FileOutputStream(filename));
		out.println(getFirstLine());

		for (Model m: all)
		{
			out.println(m.toCSV());
		}
		setUniqueID(all);
		out.println(this.toCSV());
		out.flush();
		out.close();  
	}
	//used in checkUnique (String, String)
	//returns a boolean of whether any other Model in it's .csv file has 
	//the same value attribute of the arg input
	public boolean checkUnique(Object input1) throws IOException
	{
		return checkUnique(input1,null,null);
	}
	
	//used in checkUnique (String, String, String)
	//returns a boolean of whether any other Model in it's .csv file has 
	//the same value attribute of the arg input
	public boolean checkUnique(Object input1, Object input2) throws IOException
	{
		return checkUnique(input1,input2,null);
	}

	/*
	 * returns a boolean of whether any other Model in it's .csv file has the same value attribute
	 * 
	 * early arguments must not be null if later arguments are not null
	 * 
	 * invalid example : 	input1 = null
	 * 						input3 = "id"
	 * 
	 * valid example :		input1 = "id"
	 * 						input3 = null
	*/
	
	public boolean checkUnique(Object input1, Object input2, Object input3) throws IOException
	{
		String idCheck = "" + this.get("id");
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
			if (!idCheck.equals("" + m.get("id")))
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

	//returns a Model[] of all Models found in it's .csv file
	public Model[] loadAll() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = br.readLine();
		loadFields(line);
		LinkedList<Model> models = new LinkedList<Model>();
		line = br.readLine();

		// loop through lines.
		while (line!=null)
		{  
			Model m = (Model)this.createNew();
			StringTokenizer st = new StringTokenizer(line,",\r\n");
			if (st.countTokens()!=fields.keySet().size())
				break;

			int index=0;

			// loop through all fields.
			while (st.hasMoreTokens())
			{
				m.set(fields.get(index),st.nextToken());
				index++;  
			}
			m.loaded=true;
			m.fields=this.fields;
			models.add(m);
			line = br.readLine();  
		}	//@Override
		br.close();

		return models.toArray(new Model[models.size()]);
	}

	//obtains values from it's .csv file and loads into this Model
	public void load() throws IOException
	{
		Model[] all = loadAll();

		for (Model m: all)
		{			  
			if (m.getID().equals(this.getID()))
			{
				// copy all attributes from m to this.
				for (String key: fields.values())
				{
					
					this.set(key, m.get(key));
				}

				loaded=true;
				return; // stop loading.
			}
		}
	}

	/**
	 * Return a string safe for CSV files.
	 * */
	public String encodeCSV(Object val)
	{
		return ("" + val).replace(',',' ');
	}

	//codes this Model to a String .csv acceptable file
	public String toCSV()
	{
		String result = "";
		boolean comma_needed=false;
		int len = fields.keySet().size();

		// loop through all fields.
		for (int i=0;i<len;i++)
		{
			if (comma_needed)
				result+=",";

			result+=encodeCSV(this.get(fields.get(i)));
			comma_needed=true;
		}	  

		return result;
	}

	public boolean isLoaded()
	{
		return loaded;
	}

	public String toString()
	{
		return toCSV();
	}

	//method used to determine maximum ID in it's .csv file
	public static int getMaximumID(Model[] models)
	{
		int maxID=0;
		int id;
		
		// find maximum id used by all Models.
		for (Model m: models)
		{
			id = Integer.parseInt("" + m.get("id"));
			if (id>maxID)
				maxID=id;
		}	
		return maxID;
	}
	
	//sets ID that is 1 greater than the current highest ID found in it's .csv file
	public void setUniqueID(Model[] models)
	{
		
		int maxID=Model.getMaximumID(models);
		set("id", "" + (maxID+1));
	}
	
	//Abstract methods that need to be coded in each Model type
	public abstract void set(String key,Object value);
	public abstract Object get(String key);
	protected abstract Model createNew();
}
