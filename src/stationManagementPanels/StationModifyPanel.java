package stationManagementPanels;

/*Panel added into MainFrame for modifying a station
 * 		1) enter name of station for the specific station, or enter nothing for list of stations
 * 		2)loads selected station, allows user to modify information
 * 		3)if everything is valid, saves the station
 * 		4)if its deactivated, msg is appropriately sent
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
import java.awt.*;

@SuppressWarnings("serial")
public class StationModifyPanel extends MyPanel implements ActionListener,Reloadable
{
	//user input fields
	private static JTextField nameInput;
	private JTextField descriptionInput;	
	private JTextField addressInput;
	private JTextField phoneNumberInput;
	private static JTextField findNameInput;
	private JCheckBox activated;
	
	@SuppressWarnings("rawtypes")
	private static JComboBox stations;//list of stations
	private static JLabel stationsList = new JLabel("Stations List");
	
	private static JButton saveStation; // button pressed to save changes
	private JButton find; // button used to find station
	
	private static JPanel inputArea; // JPanel used for main input area
	private JPanel findArea; // JPanel used to find station
	private JPanel findAreaFinal;
	
	private String beforeActivated;
	
	private Reservation res;
	
	private boolean findClicked;
	private static boolean stationUseFlag = false; // used so that Train can use fillidItems
	
	public StationModifyPanel()
	{
		setTitle("Station Modify");
		//initialzing input fields
		nameInput=new JTextField(15);
		addressInput=new JTextField(15);
		phoneNumberInput=new JTextField(15);
		descriptionInput=new JTextField(15);
		activated=new JCheckBox();
		findNameInput = new JTextField(15);
	
		//initializing buttons and adding actionlisteners
		saveStation=new JButton("Save Changes to Station");
		saveStation.addActionListener(this);
		find = new JButton("Find station");
		find.addActionListener(this);
		
		
		//initializing other Jpanels and setting up layouts
		inputArea=new JPanel();
		findArea = new JPanel();
		findAreaFinal = new JPanel();
		findArea.setLayout(new GridLayout(0,2));
		findAreaFinal.setLayout(new BorderLayout());
		setLayout(new BorderLayout());
		inputArea.setLayout(new GridLayout(0,2));
		
		findArea.add(new JLabel("Search for:"));
		findArea.add(findNameInput);
		
		findAreaFinal.add(find,BorderLayout.NORTH);
		findAreaFinal.add(findArea,BorderLayout.SOUTH);
		
		//adding components into deliverable container
		add(findAreaFinal,BorderLayout.NORTH);
			
		inputArea.setVisible(false);
		saveStation.setVisible(false);
		

		findClicked = false;
	}

	//loads all information of station into panel
	//used in updateInputs()
	private void loadInputsFrom(Station s)
	{
		nameInput.setText((String)s.get("name"));
		addressInput.setText((String)s.get("address"));
		descriptionInput.setText((String)s.get("description"));
		phoneNumberInput.setText((String)s.get("phone"));
		activated.setSelected(s.isActivated());
	}
	
	//gets the ID from selected item in comboBox
	//used in getSelectedIDFrom()
	@SuppressWarnings("rawtypes")
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
	
	///gets the ID from selected item in comboBox
	//used in updateInputs()
	private int getSelectedID()
	{
		return getSelectedIDFrom(stations);
	}
	
	//Updates the input containers with station information
	//used throughout class
	public void updateInputs()
	{
		int id=getSelectedID();
		try
		{
			if (id!=-1)
			{
				inputArea.setVisible(true);
				saveStation.setVisible(true);
				Station s = new Station(id);
				beforeActivated = "" + s.get("activated");
				loadInputsFrom(s);
			}
			else
			{
				JOptionPane.showMessageDialog(
						this,
						"Failed finding any station(s)");
				inputArea.setVisible(false);
				saveStation.setVisible(false);
			}
		}
		catch (IOException ie)
		{
			JOptionPane.showMessageDialog(
				this,
				"Trouble loading from file.");
		}
	}
	
	//fill comboBox with models (for the purpose of stations)
	//used in initClientsComboBox
	@SuppressWarnings("rawtypes")
	public static void fillIDItems(JComboBox box,Model [] models)
	{
		fillIDItems(box,models,null);
	}
	
	//fill comboBox with model information (for the purpose of station)
	//ID, firstName, lastName are displayed in comboBox
	//used in fillIDItems
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void fillIDItems(JComboBox box,Model [] models,Reloadable r)
	{
		box.removeAllItems();
		if (stationUseFlag == true)
		{
			if (findNameInput.getText().equals(""))
			{
				for (Model m: models)
				{
					stations.setVisible(true);
					stationsList.setVisible(true);
					box.addItem(""+m.get("id")+" - "+m.get("name"));
				}
			}
			else
			{
				for (Model m: models)
				{
					stations.setVisible(false);
					stationsList.setVisible(false);
					if (findNameInput.getText().equals("" + m.get("name")))
					{
						box.addItem(""+m.get("id")+" - "+m.get("name"));
					}
						
					
				}
			}
		}
		else
		{
			for (Model m: models)
			{
				box.addItem(""+m.get("id")+" - "+m.get("name"));
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
	// used throughout class
	@SuppressWarnings("rawtypes")
	private void initStationsComboBox()
	{
		// load options in combo box.
		try
		{
			if (stations==null)
				stations = new JComboBox();
			fillIDItems(stations,new Station().loadAll(),this);
			
		}
		catch (IOException ie)
		{
			ie.printStackTrace();
		}
	}
	
	//checks if all fields are filled
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
			//saves station to stations.csv
			if (e.getSource()==saveStation)
			{
				if (!emptyFields())
				{
					Station station = new Station(); //used to hold selected station information
					if (stations!=null)
						station.set("id", ""+getSelectedID());
					
					//selected clientInformation
					station.set("address", addressInput.getText());
					station.set("phone", phoneNumberInput.getText());
					station.set("name", nameInput.getText());
					station.set("description", descriptionInput.getText());
					station.set("activated", ""+activated.isSelected());
					if (station.checkUnique("name"))
					{
						if (MyPanel.numbersOnly((String) station.get("phone")))
						{
							if ((beforeActivated).equals("true") && (station.get("activated") + "").equals("false") )
							{
								res = new Reservation();
								Model[] all = res.loadAll();
								for (Model m: all)
								{	
									if ((m.get("from") + "").equals(station.get("id") + "") || (m.get("to") + "").equals(station.get("id") + ""))
									{
										User tempUser = new User(m.get("userName") + "");
										if ((tempUser.get("message")).equals("-1"))
										{
											tempUser.set("message", "Station " + station.get("id") + " was deactivated for Reservation " + m.get("id") + "-");
										}
										else
										{
											tempUser.set("message", tempUser.get("message") + "Station " + station.get("id") + " was deactivated for Reservation " + m.get("id") + "-");
										}
										tempUser.updateFile();
									}
								}
							}
							station.updateFile();
							JOptionPane.showMessageDialog(this,"The Station should be in the file now.");
							MainFrame.setAndPaint(new BackPanel());
						}
						else
						{
							JOptionPane.showMessageDialog(this,"The phone number must be numbers");
						}
						
					}
					else
					{
						JOptionPane.showMessageDialog(this,"The Station name is not unique");
					}				
				}
				else
				{
					JOptionPane.showMessageDialog(this,"All fields must be filled");
				}	
			}
			
			//loads the appropriate station(s)
			if (e.getSource()==find)
			{
				stationUseFlag = true;
				if (findClicked == false)
				{	
					initStationsComboBox();
					findClicked = true;
					//adding components into containers
					inputArea.add(stationsList);
					inputArea.add(stations);	
					inputArea.add(new JLabel("Name"));
					inputArea.add(nameInput);
					inputArea.add(new JLabel("Address"));
					inputArea.add(addressInput);
					inputArea.add(new JLabel("Phone Number"));
					inputArea.add(phoneNumberInput);
					inputArea.add(new JLabel("Description"));
					inputArea.add(descriptionInput);
					inputArea.add(new JLabel("Activated"));
					inputArea.add(activated);
					add(inputArea,BorderLayout.CENTER);
					add(saveStation,BorderLayout.SOUTH);
					inputArea.setVisible(true);
					saveStation.setVisible(true);
				}
				else
				{
					stations.removeAllItems();
					initStationsComboBox();
					inputArea.setVisible(true);
					saveStation.setVisible(true);
				}
				
				updateInputs();
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
	@SuppressWarnings("rawtypes")
	public static int getBestIndex(JComboBox box,String id)
	{
		for (int i=0;i<box.getItemCount();i++)
		{
			Object obj = box.getItemAt(i);
			if ((""+obj).startsWith(id+" -"))
				return i;
		}
		return 0;
	}
}
