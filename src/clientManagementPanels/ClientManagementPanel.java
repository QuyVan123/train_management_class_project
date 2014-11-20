package clientManagementPanels;

/*Panel added into MainFrame for client management
 * 	 	1)Displays 3 buttons for user: Create Client, Edit Client, Delete Client
 * 		2)User presses any of the buttons, loads the appropriate panel
 */

import generalPanels.MyPanel;
import javax.swing.*;
import main.MainFrame;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class ClientManagementPanel extends MyPanel implements ActionListener
{
	//buttons pressed to initiate next panel
	private JButton createClient;
	private JButton editClient;
	private JButton deleteClient;

	public ClientManagementPanel()
	{
		setTitle("Client Management");//Title of Frame
		
		//initializing buttons and adding action listeners
		createClient=new JButton("Create New Client");
		editClient=new JButton("Edit Existing Client");
		deleteClient = new JButton("Delete Client");
		createClient.addActionListener(this);
		editClient.addActionListener(this);
		deleteClient.addActionListener(this);
		
		//setting up layout
		setLayout(new FlowLayout());
		
		//adding components to deliverable container  
		add(createClient);
		add(editClient);
		add (deleteClient);
		
	}
	
	//Handling of when buttons are pressed
	public void actionPerformed(ActionEvent e)
	{
		try 
		{
				//Goes to Creating Client Panel
				if (e.getSource()==createClient)
				{
					MainFrame.setAndPaint(new ClientCreatePanel());
				}
				//Goes to Editing Client Panel
				else if (e.getSource()==editClient)
				{
					MainFrame.setAndPaint(new ClientModifyPanel());
				}
				//Goes to Deleting Client Panel
				else if (e.getSource()==deleteClient)
				{
					MainFrame.setAndPaint(new ClientDeletePanel());
				}
		}
		 catch (InterruptedException e1) 
		 {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		 }
	}
		
}