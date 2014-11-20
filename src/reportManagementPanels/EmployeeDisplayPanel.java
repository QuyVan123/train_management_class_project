package reportManagementPanels;

/*Panel added into MainFrame for display Employee information
 */

import generalPanels.MyPanel;
import javax.swing.*;

import model.Employee;
import model.Model;
import java.awt.*;
import java.io.IOException;

@SuppressWarnings("serial")
public class EmployeeDisplayPanel extends MyPanel
{
	private JLabel current; // holds the data
	private JScrollPane myScrollPane; // make it more accessible

	public EmployeeDisplayPanel()
	{
		//makes a new jlabel for every employee
		try
		{
			Model[] all;
			setLayout(new GridLayout(0,1));
			all = (new Employee()).loadAll();		
			for (Model m: all)
			{
				current = new JLabel (m.toString());
				myScrollPane = new JScrollPane(current);
				add (myScrollPane);
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
		
}