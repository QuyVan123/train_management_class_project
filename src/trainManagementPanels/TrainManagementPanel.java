package trainManagementPanels;

//COMPLETE 100%
/*Panel added into MainFrame for train management
 * 	 	1)Displays 2 buttons for user: Create train, modify train
 * 		2)User presses any of the buttons, loads the appropriate panel
 */

import generalPanels.MyPanel;
import javax.swing.*;
import main.MainFrame;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class TrainManagementPanel extends MyPanel implements ActionListener
{
	//buttons pressed to intiiate next panel
	private JButton createTrain;
	private JButton modifyTrain;

	public TrainManagementPanel()
	{
		setTitle("Train Management");
		//initializing buttons and adding action listeners
		createTrain=new JButton("Create New Train");
		modifyTrain=new JButton("Modify Existing Train");
		createTrain.addActionListener(this);
		modifyTrain.addActionListener(this);
		
		//setting up layout
		setLayout(new FlowLayout());
		
		//adding components into deliverable container
		add(createTrain);
		add(modifyTrain);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		try 
		{
			//creating a train
			if (e.getSource()==createTrain)
			{
				MainFrame.setAndPaint(new TrainCreatePanel());
			}
			//modifying a train
			else if (e.getSource()==modifyTrain)
			{
				MainFrame.setAndPaint(new TrainModifyPanel());
			}
		}
		 catch (InterruptedException e1) 
		 {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		 }
	}
		
}