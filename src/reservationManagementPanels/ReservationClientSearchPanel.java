package reservationManagementPanels;

/*Panel added into MainFrame for Choosing option to search for client
 * 	 	1)user presses any of the 2 buttons, and the appropriate panel is loaded
 * 
 */

import generalPanels.MyPanel;

import javax.swing.*;

import main.MainFrame;


import java.awt.event.*;

@SuppressWarnings("serial")
public class ReservationClientSearchPanel extends MyPanel implements ActionListener
{
	private JButton option1;
	private JButton option2;


	public ReservationClientSearchPanel()
	{
		option1 = new JButton("Search First Letter of Last Name");
		option2 = new JButton ("Search by Client Name");

		add(option1);
		add(option2);
		
		option1.addActionListener(this);
		option2.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e)
	{
		try 
		{
				if (e.getSource()==option1)
				{
					MainFrame.setAndPaint(new ReservationManagementPanelOne());
				}
				else if (e.getSource()==option2)
				{
					MainFrame.setAndPaint(new ReservationManagementPanelTwo());
				}
		}
		 catch (InterruptedException e1) 
		 {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		 }
	}
		
}