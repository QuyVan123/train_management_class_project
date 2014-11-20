package generalPanels;

/*Panel added into MainFrame for Manager Menu
 * 	 	1)user presses any of the available buttons for managers to initiiate appropriate panel
 */

import javax.swing.*;

import reportManagementPanels.ReportManagementPanel;
import reservationManagementPanels.ReservationClientSearchPanel;
import stationManagementPanels.StationManagementPanel;
import trainManagementPanels.TrainManagementPanel;

import clientManagementPanels.ClientManagementPanel;
import employeeManagementPanels.EmployeeManagementPanel;
import main.MainFrame;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class ManagerMenuPanel extends MyPanel implements ActionListener
{
	//buttons to press to initiate their own panels
	private JButton clientManagement;
	private JButton reservationManagement;
	private JButton stationManagement;
	private JButton report;
	private JButton employeeManagement;
	private JButton trainManagement;


	public ManagerMenuPanel()
	{
		setTitle("Manager Menu");
		//initiating buttons and adding action listeners
		clientManagement = new JButton("Clients");
		reservationManagement = new JButton("Reservations");
		stationManagement = new JButton("Stations");
		report = new JButton ("Reports");
		employeeManagement = new JButton("Employees");
		trainManagement = new JButton("Trains");
		clientManagement.addActionListener(this);
		reservationManagement.addActionListener(this);
		stationManagement.addActionListener(this);
		report.addActionListener(this);
		employeeManagement.addActionListener(this);
		trainManagement.addActionListener(this);
		
		//setting up layout
		setLayout(new GridLayout(0,2));
		
		//adding components into final container
		add(clientManagement);
		add(reservationManagement);
		add(stationManagement);
		add(trainManagement);
		add(report);
		add(employeeManagement);
		
	}
	public void actionPerformed(ActionEvent e)
	{
		try {
			//client management
			if (e.getSource()==clientManagement)
			{
				MainFrame.setAndPaint (new ClientManagementPanel());
			}
			//report management
			else if (e.getSource()==report)
			{
				MainFrame.setAndPaint(new ReportManagementPanel());
			}
			//employee management
			else if (e.getSource()==employeeManagement)
			{
				MainFrame.setAndPaint(new EmployeeManagementPanel());
			}
			//train management
			else if (e.getSource()==trainManagement)
			{
				MainFrame.setAndPaint(new TrainManagementPanel());
			}
			//reservation management
			else if (e.getSource()==reservationManagement)
			{
				MainFrame.setAndPaint(new ReservationClientSearchPanel());
			}
			//station management
			else if (e.getSource()==stationManagement)
			{
				MainFrame.setAndPaint(new StationManagementPanel());
			}
			
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
}
