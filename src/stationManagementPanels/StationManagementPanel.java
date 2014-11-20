package stationManagementPanels;

/*Panel added into MainFrame for station management
 * 	 	1)Displays 2 buttons for user: Create station, modify station
 * 		2)User presses any of the buttons, loads the appropriate panel
 */

import generalPanels.MyPanel;

import javax.swing.*;

import main.MainFrame;


import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class StationManagementPanel extends MyPanel implements ActionListener
{
	//buttons pressed to initiate next panel
	private JButton createStation;
	private JButton modifyStation;


	public StationManagementPanel()
	{
		setTitle("Station Management");
		//initializing buttons and adding action listeners
		createStation=new JButton("Create New Station");
		modifyStation=new JButton("Modify Existing Station");
		createStation.addActionListener(this);
		modifyStation.addActionListener(this);
		
		//setting up layout
		setLayout(new FlowLayout());
		
		//adding containers to deliverable container
		add(createStation);
		add(modifyStation);
		
		
	}
	public void actionPerformed(ActionEvent e)
	{
		try 
		{
				//creating station
				if (e.getSource()==createStation)
				{
					MainFrame.setAndPaint(new StationCreatePanel());
				}
				//modifying station
				else if (e.getSource()==modifyStation)
				{
					MainFrame.setAndPaint(new StationModifyPanel());
				}
		}
		 catch (InterruptedException e1) 
		 {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		 }
	}
		
}