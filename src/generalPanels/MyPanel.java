package generalPanels;

/*Extends JPanel, also holds utility methods for program
 * 	
 * 
 */
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;



@SuppressWarnings("serial")
public class MyPanel extends JPanel
{
	protected final DateFormat dateFormatDay = new SimpleDateFormat("dd/MM/yy");
	protected final DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
	private boolean back = false;
	protected static String delim = "-"; // used when displaying in comboBox, and retrieving back the information
	
	private String title;//title of the frame

	protected void setTitle(String input)
	{
		title = input;
	}

	public String getTitle()
	{
		return title;
	}
	
	public boolean getBack()
	{
		return back;
	}
	
	protected void setBack(boolean input)
	{
		back = input;
	}
	
	//a utility method
	//checks if the string contains only numbers
	public static boolean numbersOnly(String input)
	{
		char[] c = input.toCharArray();
		for(int i=0; i < input.length(); i++)
		{
			if ( !Character.isDigit(c[i]))
			{
				return false;
			}
		}
		return true;
	}
	
	//a utility method
	//checks if the string is a double (all numbers, possibly one ".")
	public static boolean doubleOnly(String input)
	{
		char[] c = input.toCharArray();
		boolean dotFlag = false;
		for(int i=0; i < input.length(); i++)
		{
			if (dotFlag == false && c[i] == '.')
			{
				dotFlag = true;
			}
			else if (dotFlag == true && c[i] == '.')
			{
				return false;
			}
			else if ( !Character.isDigit(c[i]))
			{
				return false;
			}
		}
		return true;
	}

}
