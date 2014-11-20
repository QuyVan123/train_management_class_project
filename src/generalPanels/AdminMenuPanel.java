package generalPanels;

/*Panel added into MainFrame for Admin Menu
 * 	 	1)user presses any of the available buttons for admin to initiate appropriate panel
 */

import javax.swing.*;

import userManagementPanels.UserManagementPanel;
import main.MainFrame;
import java.awt.event.*;

@SuppressWarnings("serial")
public class AdminMenuPanel extends MyPanel implements ActionListener
{
	JButton userMngBtn;
	
	public AdminMenuPanel()
	{
		setTitle("Admin Menu");
		//initiating button and action listener
		userMngBtn=new JButton("Manage Users"); // button used to activate manager menu
		userMngBtn.addActionListener(this); 
		
		//add component to deliverable container
		add(userMngBtn);
}
	
	public void actionPerformed(ActionEvent e)
	{
		try {
			if (e.getSource()==userMngBtn)
			{
				MainFrame.setAndPaint (new UserManagementPanel());
			}
			
		} catch (InterruptedException ea) {
			// TODO Auto-generated catch block
			ea.printStackTrace();
		}
		
	}
 
}
