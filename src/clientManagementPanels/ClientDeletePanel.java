package clientManagementPanels;
/*Panel added into MainFrame for deleting a client
 * 
 * 	 	1)Gets user to input first letter of last name...will only read first letter
 * 		2)Waits for user to press update to fill comboBox
 * 		3)user presses update, comboBox is filled
 * 		4)User selects client in comboBox
 * 		5)User presses Delete Client
 * 		6)Client is removed from clients.csv
 * 		7)Flag all of client's reservations TODO
 * 		8)Something that I don't understand, check to do file in data TODO
 * 
 * User Input : first letter of lastName
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
import java.util.Calendar;
import java.util.Date;
import java.awt.*;

@SuppressWarnings("serial")
public class ClientDeletePanel extends MyPanel implements ActionListener
{
	private JComboBox clients; // used to display list of clients
	
	private JButton update; // button pressed to update comboBox
	private JButton deleteClient; // button pressed to delete client
	
	private static JTextField letterInput; // input from user, first letter of last name
											//of client wanting to be selected
											//accepts only 1 letter, or warning msg to user

	private JPanel inputArea; // JPanel used for easier display
	
	
	//stuff for birthday spinner
	private JSpinner birthdayInput;
	JSpinner.DateEditor dateEditor;
	
	private static String delim = "-"; // used when displaying in comboBox, and retrieving back the information
	
	public ClientDeletePanel() throws IllegalArgumentException
	{
		setTitle("Client Delete"); // Title of Frame
		
		letterInput = new JTextField(15);// initializing field
		
		//initializing birthday stuff
	    birthdayInput = new JSpinner(new SpinnerDateModel(new Date(), null, null,
	        Calendar.MONTH));
	    dateEditor = new JSpinner.DateEditor(birthdayInput, "dd/MM/yy");
	    birthdayInput.setEditor(dateEditor);
		
		//initializing buttons and actionlisteners
		update = new JButton("Update");	
		update.addActionListener(this);
		deleteClient=new JButton("Delete Client");
		deleteClient.addActionListener(this);
		
		//initializing other JPanels and setting up layouts
		setLayout(new BorderLayout());
		inputArea = new JPanel();
		inputArea.setLayout(new GridLayout(0,2));
		
		initClientsComboBox();//Initializing comboBox
		
		//adding components to JPanels
		inputArea.add(new JLabel("Enter First letter of last name"));
		inputArea.add(letterInput);
		inputArea.add(new JLabel("Possible Clients"));
		inputArea.add(clients);
		
		//adding components into deliverable container
		add(update,BorderLayout.NORTH);
		add(inputArea,BorderLayout.CENTER);
		add(deleteClient,BorderLayout.SOUTH);
		
	}
	
	//gets the ID from selected item in comboBox
	//used in getSelectedIDFrom()
	public static int getSelectedIDFrom(JComboBox box)
	{

		if (box.getItemAt(0)!=null)
		{
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

			return -1;
		}
	}

	
	//fill comboBox with models (for the purpose of clients)
	//used in initClientsComboBox()
	public static void fillIDItems(JComboBox box,Model [] models)
	{
		fillIDItems(box,models,null);
	}
	
	//fill comboBox with model information (for the purpose of clients)
	//ID, firstName, lastName are displayed in comboBox
	//used in fillIDItems(JComboBox, Model[])
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
		if (r!=null)
		{
			class UpdateInputsItemListener implements ItemListener
			{
				private Reloadable r;
				
				public UpdateInputsItemListener(Reloadable r)
				{
					this.r=r;
				}
				
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED)
					{
						r.updateInputs();
					}
				}
				
			};
			
			box.addItemListener(new UpdateInputsItemListener(r));
		}
		
	}
	
	// load options in combo box.
	// obtains Clients with lastNames that start with letterInput
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
	
	
	public void actionPerformed(ActionEvent e) throws IllegalArgumentException
	{
		int index;
		try
		{
			//deletes client from csv file
			//goes back 1 panel
			if (e.getSource()==deleteClient)
			{
				index = clients.getSelectedItem().toString().indexOf(delim);
				
				//holds client information of the selected client
				Client client = new Client(Integer.parseInt(clients.getSelectedItem().toString().substring(0,index)));
				
				client.delete();
				MainFrame.setAndPaint(new BackPanel());
				JOptionPane.showMessageDialog(this,"The Client should be deleted now.");
			}
			
			//updates comboBox with information in letterInput
			else if (e.getSource()==update)
			{	
				if ((letterInput.getText()).length()==1)
				{
					clients.removeAllItems();
			    	initClientsComboBox(); 
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
