package clientManagementPanels;

/*Panel added into MainFrame for modifying a client
 * 		1)Checks if there exists any clients
 * 	 	2)User enters first letter of last name to find list of clients TODO
 * 		3)Information on client is shown on screen
 * 		4)allows user to modify information
 * 		5)user presses save client
 * 		6)checks if birthday and email are unique
 * 		7)checks if phone number is numbers only
 * 		8)reservation pertaining to client should be flagged
 * 		9)do not understand refer to todo in data
 * 
 * User Input : firstName, lastName, address, phone, email, sex, birthday
 *
 * 
 */

import main.MainFrame;
import misc.Reloadable;
import model.*;
import generalPanels.BackPanel;
import generalPanels.MyPanel;
import javax.swing.*;

import java.awt.event.*;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.awt.*;

@SuppressWarnings("serial")
public class ClientModifyPanel extends MyPanel implements ActionListener,Reloadable
{
	
	private JComboBox clients; //used to display list of clients
	
	//user input fields
	private JTextField firstNameInput;	
	private JTextField lastNameInput;	
	private JTextField addressInput;
	private JTextField phoneInput; // integer acceptable only, will check for int only
	private JTextField emailInput;
	private JTextField sexInput;
	
	private static JTextField letterInput;  // input from user, first letter of last name
											//of client wanting to be selected
											//accepts only 1 letter, or warning msg to user
	
	//stuff for birthday spinner
	private JSpinner birthdayInput;
	JSpinner.DateEditor dateEditor;
	private SpinnerDateModel spinnerModel;
	
	private JButton modifyClient; //button to press to save changes to clients.csv
	private JButton update; // button pressed to update client box/info
	
	private static String delim = "-"; // used when displaying in comboBox, and retrieving back the information
	
	//JPanels for easier display
	private JPanel inputArea;
	private JPanel updateArea;
	private JPanel finalUpdateArea;
	
	public ClientModifyPanel() throws IllegalArgumentException
	{
		setTitle("Client Modify");//Title of Frame
		
		//Initializing input fields
		firstNameInput=new JTextField(15);
		lastNameInput=new JTextField(15);
		addressInput=new JTextField(15);
		phoneInput=new JTextField(15);
		emailInput=new JTextField(15);
		sexInput=new JTextField(15);
		letterInput = new JTextField(15);
		
		//initializing birthday stuff
		spinnerModel = new SpinnerDateModel(new Date(), null, null, Calendar.MONTH);
	    birthdayInput = new JSpinner(spinnerModel);
	    dateEditor = new JSpinner.DateEditor(birthdayInput, "dd/MM/yy");
	    birthdayInput.setEditor(dateEditor);
		
		//Initializing other JPanels and setting up layouts
		inputArea = new JPanel();
		updateArea = new JPanel();
		finalUpdateArea = new JPanel();
		setLayout(new BorderLayout());
		updateArea.setLayout(new GridLayout(0,2));
		inputArea.setLayout(new GridLayout(0,2));
		finalUpdateArea.setLayout(new BorderLayout());
		
		initClientsComboBox();//Initializing comboBox
		

		//Initializing JButtons and adding actionListeners
		modifyClient=new JButton("Save Changes to Client");
		modifyClient.addActionListener(this);
		update = new JButton("Update");
		update.addActionListener(this);
		
		//adding components to JPanels
		updateArea.add(new JLabel("Client to Edit"));
		updateArea.add(clients);
		updateArea.add(new JLabel ("First Letter of Last Name"));
		updateArea.add(letterInput);
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
	    finalUpdateArea.add(update,BorderLayout.NORTH);
	    finalUpdateArea.add(updateArea,BorderLayout.SOUTH);
	    
		//adding components into initial deliverable container
		//the other JPanels will be added later on when certain buttons are clicked
		add(modifyClient,BorderLayout.SOUTH);
		add(inputArea,BorderLayout.CENTER);
		add(finalUpdateArea,BorderLayout.NORTH);
		inputArea.setVisible(false);
		modifyClient.setVisible(false);
	}
	
	//loads all information of client into panel
	//used in updateInputs()
	private void loadInputsFrom(Client s)
	{
		
		try {
			firstNameInput.setText((String)s.get("firstName"));
			lastNameInput.setText((String)s.get("lastName"));
			addressInput.setText((String)s.get("address"));
			phoneInput.setText("" + s.get("phone"));
			emailInput.setText((String)s.get("email"));
			sexInput.setText((String)s.get("sex"));
			spinnerModel.setValue(dateFormatDay.parse((String)s.get("birthday")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	//gets the ID from selected item in comboBox
	//used in getSelectedIDFrom()
	public int getSelectedIDFrom(JComboBox box)
	{
		if (box.getItemAt(0)!=null)
		{
			inputArea.setVisible(true);
			modifyClient.setVisible(true);
			String selectedText = ""+box.getItemAt(box.getSelectedIndex());
			String idParts[] = selectedText.split("-");
			// idParts = ["3 ", " Client Name"]
			String idStr = idParts[0].trim(); 
			// id = "3"
			int id = Integer.parseInt(idStr);
			return id;	
		}
		else
		{
			inputArea.setVisible(false);
			modifyClient.setVisible(false);
			return -1;
		}
	}
	
	///gets the ID from selected item in comboBox
	//used in updateInputs()
	private int getSelectedID()
	{
		return getSelectedIDFrom(ClientModifyPanel.this.clients);
	}
	
	//Updates the input containers with client information
	//used throughout class
	public void updateInputs()
	{
		int id=getSelectedID();
		Client s;

		try
		{
			if (id!=-1)
			{
				 s = new Client(id);
				 loadInputsFrom(s);
			}
			else
			{
				JOptionPane.showMessageDialog(
						this,
						"Failed finding any client(s)");
			}
				
		}
		catch (IOException ie)
		{
			JOptionPane.showMessageDialog(
				this,
				"Trouble loading from file.");
		}
	}
	
	//fill comboBox with models (for the purpose of clients)
	//used in initClientsComboBox
	public static void fillIDItems(JComboBox box,Model [] models)
	{
		fillIDItems(box,models,null);
	}
	
	
	//fill comboBox with model information (for the purpose of clients)
	//ID, firstName, lastName are displayed in comboBox
	//used in fillIDItems
	public static void fillIDItems(JComboBox box,Model [] models,Reloadable r)
	{
		box.removeAllItems();	
		for (Model m: models)
		{
			if ((letterInput.getText().charAt(0)) == (((String)m.get("lastName")).charAt(0)))
			{
				box.addItem(""+m.get("id")+ delim +m.get("firstName")+ delim +m.get("lastName"));
			}
		}
	}
	
	// load options in combo box.
	// used throughout class
	private void initClientsComboBox()
	{
		try
		{
			if (this.clients==null)
				this.clients = new JComboBox();
			if (!letterInput.getText().equals(""))
			{
				fillIDItems(this.clients,new Client().loadAll());
			}
		}
		catch (IOException ie)
		{
			ie.printStackTrace();
		}
	}
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
	
	public void actionPerformed(ActionEvent e) throws IllegalArgumentException
	{
		try
		{
			//checks if email and birthday are unique
			//checks if phone number is numbers only
			//updates clients.csv with new modified information
			//Frame goes back 1 panel
			
			if (e.getSource()==modifyClient)
			{
				if (!emptyFields())
				{
					Client client = new Client(); // used to hold selected client information
					if (clients!=null)
						client.set("id", ""+getSelectedID());
					
					// selected client information is uploaded
					client.set("firstName", firstNameInput.getText());
					client.set("lastName", lastNameInput.getText());
					client.set("address", addressInput.getText());
					client.set("phone", phoneInput.getText());
					client.set("email", emailInput.getText());
					client.set("sex", sexInput.getText());
					client.set("birthday", dateFormatDay.format(birthdayInput.getValue()));
					if (client.checkUniqueClient())
					{
						if(MyPanel.numbersOnly(phoneInput.getText()))
						{
							client.updateFile();
							MainFrame.setAndPaint(new BackPanel());
							JOptionPane.showMessageDialog(this,"The Client should be saved");
						}
						else
						{
							JOptionPane.showMessageDialog(this,"The Phone Number must be numbers only");
						}
						
					}
					else
					{
						JOptionPane.showMessageDialog(this,"The Birthday and Email is not unique");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this,"All fields must be filled");
				}
				
			}
			//updates comboBox with information in letterInput
			else if (e.getSource()==update)
			{	
				if ((letterInput.getText()).length()==1)
				{
			    	clients.removeAllItems();
			    	initClientsComboBox(); 
			    	updateInputs();
				}
				else
				{
					JOptionPane.showMessageDialog(this,"Please enter 1 character only");
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

}
