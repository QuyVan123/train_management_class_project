package generalPanels;

/*Panel added into MainFrame for Staff Menu
 * 	 	1)user presses any of the available buttons for staff to initiate appropriate panel
 */

import javax.swing.*;

import reservationManagementPanels.ReservationClientSearchPanel;
import clientManagementPanels.ClientManagementPanel;
import main.MainFrame;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class StaffMenuPanel extends MyPanel implements ActionListener
{
	//buttons to press to initiate appropriate panels
	private JButton clientManagement;
	private JButton reservationManagement;

	public StaffMenuPanel()
	{
		setTitle("Staff Menu");
		//initiating buttons and actionListeners
		clientManagement = new JButton("Clients");
		reservationManagement = new JButton("Reservations");
		clientManagement.addActionListener(this);
		reservationManagement.addActionListener(this);
		
		//setting up layout
		setLayout(new FlowLayout());
		
		//adding components into final container
		add(clientManagement);
		add(reservationManagement);	
		
	}
	public void actionPerformed(ActionEvent e)
	{
		try {
			//Client Management
			if (e.getSource()==clientManagement)
			{
				MainFrame.setAndPaint (new ClientManagementPanel());
			}
			//Reservation Management
			else if (e.getSource()==reservationManagement)
			{
				MainFrame.setAndPaint(new ReservationClientSearchPanel());
			}
			
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
