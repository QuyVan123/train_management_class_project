package trainManagementPanels;

/*Panel added into MainFrame for creating a train
 * 	 	1)Obtain Information from user
 * 		2)User presses Create Train
 * 		4)Save station information into stations.csv
 * 
 * User Input : firstName, lastName, address, phone, email, sex, birthday, capacity, activated
 * Program Generated : id (unique), dateJoined
 * 
 */

import main.MainFrame;
import model.*;

import generalPanels.BackPanel;
import generalPanels.MyPanel;

import javax.swing.*;

import stationManagementPanels.StationModifyPanel;

import java.awt.event.*;
import java.io.*;
import java.awt.*;


@SuppressWarnings("serial")
public class TrainCreatePanel extends MyPanel implements ActionListener
{
	//user input fields
	private JTextField nameInput;
	private JTextField fareInput;
	private JTextField capacityInput;
	private JCheckBox activatedInput;
	
	private JComboBox fromBox; // list of stations
	private JComboBox toBox; // list of stations
	
	private JSpinner departureTimeInput;
	private JSpinner arrivalTimeInput;
	
	
	private JButton createTrain; // button used to create train
	
	public TrainCreatePanel( )
	{
		setTitle("Create Train");
		//initializing user input fields
		nameInput = new JTextField(10);
		fareInput=new JTextField(10);
		capacityInput=new JTextField(10);
		activatedInput=new JCheckBox();
		
		departureTimeInput = new JSpinner ();
		departureTimeInput.setModel(new SpinnerDateModel());
		departureTimeInput.setEditor(new JSpinner.DateEditor(departureTimeInput,"HH:mm"));
		
		arrivalTimeInput = new JSpinner ();
		arrivalTimeInput .setModel(new SpinnerDateModel());
		arrivalTimeInput .setEditor(new JSpinner.DateEditor(arrivalTimeInput ,"HH:mm"));
		
		
		//initializing buttons and adding action listeners
		createTrain=new JButton("Create Train");
		createTrain.addActionListener(this);
		
		//initializing comboBoxes
		toBox=new JComboBox();
		fromBox=new JComboBox();
		
		//setting up layouts
		setLayout(new GridLayout(0,2));
		
		fillFromTo(); // fills the from and to comboBoxes
		
		//adding components into deliverable container
		add(new JLabel("Name"));
		add(nameInput);
		add(new JLabel("From"));
		add(fromBox);
		add(new JLabel("To"));
		add(toBox);
		add(new JLabel("Departure Time (HH,mm)"));
		add(departureTimeInput);
		add(new JLabel("Arrival Time (HH,mm)"));
		add(arrivalTimeInput);
		add(new JLabel("Fare"));
		add(fareInput);
		add(new JLabel("Capacity"));
		add(capacityInput);
		add(new JLabel("Activated"));
		add(activatedInput);
		add(createTrain);
	}
	
	//fills the from and to comboBoxes
	public void fillFromTo()
	{
		try
		{
			StationModifyPanel.fillIDItems(fromBox,(new Station()).loadAll());
			StationModifyPanel.fillIDItems(toBox,(new Station()).loadAll());
		}
		catch (IOException ie)
		{
			ie.printStackTrace();
		}
	}
	
	private boolean emptyFields()
	{
		if (nameInput.getText().equals("") ||
			fareInput.getText().equals("") ||
			capacityInput.getText().equals(""))
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
			if (e.getSource()==createTrain)
			{
				if (!emptyFields())
				{
					if (MyPanel.numbersOnly(capacityInput.getText()))
					{
						if (!(StationModifyPanel.getSelectedIDFrom(fromBox) == StationModifyPanel.getSelectedIDFrom(toBox)))
						{
							if (MyPanel.doubleOnly(fareInput.getText()))
							{
								Train t = new Train();
								
								t.set("name", nameInput.getText());
								t.set("from",""+ StationModifyPanel.getSelectedIDFrom(fromBox));
								t.set("to",""+ StationModifyPanel.getSelectedIDFrom(toBox));
								t.set("departureTime", dateFormatTime.format(departureTimeInput.getValue()));
								t.set("arrivalTime", dateFormatTime.format(arrivalTimeInput.getValue()));
								t.set("capacity", capacityInput.getText());
								t.set("fare", fareInput.getText());
								t.set("activated", ""+activatedInput.isSelected());
								
								t.addToFile();
							
								JOptionPane.showMessageDialog(this,
										"The Train should be in the file now.");
								MainFrame.setAndPaint(new BackPanel());
							}
							else
							{
								JOptionPane.showMessageDialog(this,"Fare must be a double");
							}
						}
						else
						{
							JOptionPane.showMessageDialog(this,"The from station cannot be the same as the to station");
						}
						
					}
					else
					{
						JOptionPane.showMessageDialog(this,"Invalid capacity, must input a number");
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
			JOptionPane.showMessageDialog(this,"Unable to create train.");
			ie.printStackTrace();
		} catch (InterruptedException ea) {
			// TODO Auto-generated catch block
			ea.printStackTrace();
		}
		
		
	}
	
}
