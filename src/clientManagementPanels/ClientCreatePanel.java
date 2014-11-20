package clientManagementPanels;
/*Panel added into MainFrame for creating a client
 * 	 	1)Obtain Information from user
 * 		2)User presses Create User
 * 		3)Validates Input from user, if not validated, will not go to step 4
 * 		4)Save client information into clients.csv
 * 
 * User Input : firstName, lastName, address, phone, email, sex, birthday
 * Program Generated : id (unique), dateJoined, listOfReservations
 * 
 *  checks if any fields are empty
	checks if phone number is number only
	checks if email and birthday are unique
	Saves all the user input into the clients.csv file if valid
	goes back one panel
 */

import main.MainFrame;
import model.*;
import generalPanels.BackPanel;
import generalPanels.MyPanel;
import javax.swing.*;

import java.awt.event.*;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.awt.*;

@SuppressWarnings("serial")
public class ClientCreatePanel extends MyPanel implements ActionListener
{
	//user input fields
	private JTextField firstNameInput;
	private JTextField lastNameInput;
	private JTextField addressInput;
	private JTextField phoneInput;
	private JTextField emailInput;
	private JTextField sexInput;
	
	//stuff for birthday spinner
	private JSpinner birthdayInput;
	JSpinner.DateEditor dateEditor;
	
	private JButton createClient; // button pressed to save the client
	
	private JPanel inputArea;//jpanel used for easier  display

	
	public ClientCreatePanel() throws IllegalArgumentException
	{
		
		setTitle("Client Create");//Title of Frame
		
		
		//initializing user input fields
		firstNameInput=new JTextField(15);
		lastNameInput=new JTextField(15);
		addressInput=new JTextField(15);
		phoneInput=new JTextField(15);
		emailInput=new JTextField(15);
		sexInput=new JTextField(15);
		
		//initializing birthday stuff
	    birthdayInput = new JSpinner(new SpinnerDateModel(new Date(), null, null,
	        Calendar.MONTH));
	    dateEditor = new JSpinner.DateEditor(birthdayInput, "dd/MM/yy");
	    birthdayInput.setEditor(dateEditor);

		//initializing buttons and adding action listeners
		createClient=new JButton("Create Client");
		createClient.addActionListener(this);
		
		//initializing other Jpanels and setting up layouts
		setLayout(new BorderLayout());
		
		inputArea = new JPanel();
		inputArea.setLayout(new GridLayout(0,2));
		
		//adding components to JPanels
		inputArea.add(new JLabel("First Name"));
		inputArea.add(firstNameInput);
		inputArea.add(new JLabel("Last Name"));
		inputArea.add(lastNameInput);
		inputArea.add(new JLabel("Address"));
		inputArea.add(addressInput,BorderLayout.EAST);
		inputArea.add(new JLabel("Phone Number"));
		inputArea.add(phoneInput,BorderLayout.EAST);
		inputArea.add(new JLabel("Email"));
		inputArea.add(emailInput);
		inputArea.add(new JLabel("Sex"));
		inputArea.add(sexInput);
		inputArea.add(new JLabel("Birthday (dd/MM/yy"));
	    inputArea.add(birthdayInput);
		
		//adding all components into deliverable container
		add(inputArea,BorderLayout.CENTER);
		add(createClient,BorderLayout.SOUTH);	
	}

	public void actionPerformed(ActionEvent e) throws IllegalArgumentException
	{
		try
		{
			//checks if any fields are empty
			//checks if phone number is number only
			//checks if email and birthday are unique
			//Saves all the user input into the clients.csv file if valid
			//goes back one panel
			
			if (e.getSource()==createClient)
			{
				if (!emptyFields())
				{
					if (MyPanel.numbersOnly(phoneInput.getText()))
					{
						Client client = new Client(); // holds client information inputted by user
						client.set("firstName", firstNameInput.getText());
						client.set("lastName", lastNameInput.getText());
						client.set("address", addressInput.getText());
						client.set("phone", phoneInput.getText());
						client.set("email", emailInput.getText());
						client.set("sex", sexInput.getText());
						client.set("birthday", dateFormatDay.format(birthdayInput.getValue()));
						if (client.checkUniqueClient())
						{
								client.addToFile();
								JOptionPane.showMessageDialog(this,"The Client should be in the file now.");
								MainFrame.setAndPaint(new BackPanel());
								
						}
						else
						{
							JOptionPane.showMessageDialog(this,"The Birthday and Email is not unique");
						}
					}
					else
					{
						JOptionPane.showMessageDialog(this,"The Phone Number must be numbers only");
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
			JOptionPane.showMessageDialog(this,"Unable to create client.");
			ie.printStackTrace();
		} catch (InterruptedException ea) {
			ea.printStackTrace();
		} 
	}
	
	//checks if the any fields are empty
	private boolean emptyFields()
	{
		if (firstNameInput.getText().equals("") ||
			lastNameInput.getText().equals("") ||
			addressInput.getText().equals("") ||
			phoneInput.getText().equals("") ||
			emailInput.getText().equals("") ||
			sexInput.getText().equals(""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
