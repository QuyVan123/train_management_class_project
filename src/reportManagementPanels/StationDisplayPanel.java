package reportManagementPanels;

/*Panel added into MainFrame for display Employee information
 */

import generalPanels.MyPanel;
import javax.swing.*;

import model.Model;
import model.Station;

import java.awt.*;
import java.io.IOException;

@SuppressWarnings("serial")
public class StationDisplayPanel extends MyPanel
{
	private JLabel current; //holds data
	private JScrollPane myScrollPane; // easier display

	public StationDisplayPanel()
	{
		//new jlabel for every station
		try
		{
			Model[] all;
			setLayout(new GridLayout(0,1));
			all = (new Station()).loadAll();		
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