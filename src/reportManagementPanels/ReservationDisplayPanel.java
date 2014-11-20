package reportManagementPanels;

/*Panel added into MainFrame for display Reservation information
 */

import generalPanels.MyPanel;
import javax.swing.*;

import model.Model;
import model.Reservation;
import java.awt.*;
import java.io.IOException;

@SuppressWarnings("serial")
public class ReservationDisplayPanel extends MyPanel
{
	private JLabel current; // holds data
	private JScrollPane myScrollPane; // easier display

	public ReservationDisplayPanel()
	{
		//makes a new jlabel for every reservation
		try
		{
			Model[] all;
			setLayout(new GridLayout(0,1));
			all = (new Reservation()).loadAll();		
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