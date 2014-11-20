package stationManagementPanels;
/*Panel added into MainFrame for creating a station
 * 	 	1)Gets input from user
 * 		2)User presses create button
 * 		3)Checks if name is unique, if not sends an error message
 * 		4)If unique, saves to stations.csv
 * 
 * User Input : name, description, address, phone, activated
 * Program Generated : id (unique), dateJoined
 * 
 */

import main.MainFrame;
import model.*;
import generalPanels.BackPanel;
import generalPanels.MyPanel;

import javax.swing.*;


import java.awt.event.*;
import java.io.*;
import java.awt.*;

@SuppressWarnings("serial")
public class StationCreatePanel extends MyPanel implements ActionListener
{
	//user input fields
	private JTextField nameInput;
	private JTextField descriptionInput;	
	private JTextField addressInput;
	private JTextField phoneNumberInput;
	private JCheckBox activated;
	
	private JButton createStation;//button pressed to create station
	
	
	public StationCreatePanel()
	{
		setTitle("Create Station");
		//initialziing input fields
		nameInput=new JTextField(15);
		addressInput=new JTextField(15);
		phoneNumberInput=new JTextField(15);
		descriptionInput=new JTextField(25);
		activated=new JCheckBox();
		
		//initializing buttons and adding actionlisteners
		createStation=new JButton("Create Station");
		createStation.addActionListener(this);
	
		//layout setup
		setLayout(new GridLayout(0,2));

		//adding containers to deliverable container
		add(new JLabel("Name"));
		add(nameInput);
		add(new JLabel("Address"));
		add(addressInput);
		add(new JLabel("Phone Number"));
		add(phoneNumberInput);
		add(new JLabel("Description"));
		add(descriptionInput);
		add(new JLabel("Activated"));
		add(activated);
		add(createStation);
		

	}

	//checks if it all fields are filled
	private boolean emptyFields()
	{
		if (nameInput.getText().equals("") ||
			addressInput.getText().equals("") ||
			phoneNumberInput.getText().equals("") ||
			descriptionInput.getText().equals(""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			//loads input from user to a new Station
			//checks if name is unique
			//saves station into stations.csv
			if (e.getSource()==createStation)
			{
				if (!emptyFields())
				{
					Station station = new Station();
					station.set("name", nameInput.getText());
					station.set("description", descriptionInput.getText());
					station.set("address", addressInput.getText());
					station.set("phone", phoneNumberInput.getText());
					station.set("activated", ""+activated.isSelected());
					if (MyPanel.numbersOnly((String) station.get("phone")))
					{
						if (station.checkUnique("name"))
						{
							station.addToFile();	
							JOptionPane.showMessageDialog(this,"The Station should be in the file now.");
							MainFrame.setAndPaint(new BackPanel());
						}
						else
						{
							JOptionPane.showMessageDialog(this,"The Station name is not unique");
						}
					}
					else
					{
						JOptionPane.showMessageDialog(this,"The phone number must be numbers");
					}
					
				}
				else
				{
					JOptionPane.showMessageDialog(this,"All fields must be filled");
				}
				
			}
			
			
		}
		catch (IOException ie)
		{
			JOptionPane.showMessageDialog(this,"Unable to create station.");
			ie.printStackTrace();
		} catch (InterruptedException ea) {
			// TODO Auto-generated catch block
			ea.printStackTrace();
		}
	}
	

}
