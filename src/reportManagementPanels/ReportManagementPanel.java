package reportManagementPanels;

/*Panel added into MainFrame for display Report information
 * 
 * 		1)user presses 1 of 3 buttons, and the correct panel is opened
 */



import generalPanels.MyPanel;

import javax.swing.*;

import main.MainFrame;
import model.Employee;
import model.Reservation;
import model.Station;


import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

@SuppressWarnings("serial")
public class ReportManagementPanel extends MyPanel implements ActionListener
{
	private JButton booking;
	private JButton deactivated;
	private JButton stations;
	private JButton employees;

	public ReportManagementPanel()
	{
		booking=new JButton("Train Booking Report");
		stations = new JButton ("Stations Report");
		employees = new JButton ("Employees Report");
		
		setLayout(new FlowLayout());
		add(booking);
		add(stations);
		add(employees);
		
		booking.addActionListener(this);
		stations.addActionListener(this);
		employees.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e)
	{

			
		try 
		{
			if (e.getSource()==booking)
			{
				MainFrame.setAndPaint(new ReservationDisplayPanel());
			}
			else if (e.getSource()==employees)
			{
				MainFrame.setAndPaint(new EmployeeDisplayPanel());
			}
			else if (e.getSource()==stations)
			{
				MainFrame.setAndPaint(new StationDisplayPanel());
			}
		} 
		catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}