package reservationManagementPanels;
/*Panel added into MainFrame for managing Reservations
 * 		1)searches for client by last letter of firstname
 * 	 	2)asks user to create or modify
 * 		3) user presses button, appropriate display is shown
 * 		4)appropriate fields are loaded
 * 		5)user saves information... reservation is saved to reservation.csv
 * 
 */
import main.MainFrame;
import misc.Reloadable;
import model.*;
import generalPanels.BackPanel;
import generalPanels.MyPanel;

import javax.swing.*;

import stationManagementPanels.StationModifyPanel;


import java.awt.event.*;
import java.io.*;
import java.text.ParseException;
import java.util.StringTokenizer;
import java.awt.*;

@SuppressWarnings("serial")
public class ReservationManagementPanelOne extends MyPanel implements ActionListener, Reloadable, ItemListener
{
	//user input fields
	private JTextField capacityInput;
	private static JTextField letterInput;
	
	
	private JTextField reservationNumberInput;//reservation number for display only (is set to not editable
	
	//comboboxes for user selection
	private static JComboBox clients;
	private static JComboBox toStations;
	private static JComboBox fromStations;
	private static JComboBox reservationList;
	
	//spinner to choose times
	private JSpinner departureTime;
	private JSpinner arrivalTime;
	
	//buttons to be pressed to initiate certain actions
	private JButton findClient;
	private JButton saveReservation;
	private JButton createReservation;
	private JButton initCreate;
	private JButton initModify;
	private JButton selectClient;
	
	//panels for easier display
	private static JPanel findClientArea;
	private static JPanel findClientAreaFinal;
	private static JPanel inputArea;
	private static JPanel buttonArea;
	
	//used to modify information for respective Model types
	private static Client tempClient;
	private Reservation r;
	private Train temp;
	
	//used throughout program to help modify information
	private int trainID;
	private int newCapacity;

	public ReservationManagementPanelOne() throws IllegalArgumentException
	{
		//initiating fields
		reservationNumberInput = new JTextField();
		capacityInput = new JTextField();
		letterInput = new JTextField();
		
		
		//initiating buttons and action listeners
		createReservation = new JButton("Create Reservation");
		saveReservation = new JButton ("Save Reservation");
		findClient = new JButton("Find Client");
		initCreate = new JButton("Create Reservation");
		initModify = new JButton("Modify Reservation");
		selectClient = new JButton("Select Client");
		findClient.addActionListener(this);
		initModify.addActionListener(this);
		initCreate.addActionListener(this);
		saveReservation.addActionListener(this);
		createReservation.addActionListener(this);
		selectClient.addActionListener(this);
		
		//initiating spinners
		departureTime= new JSpinner ();
		departureTime.setModel(new SpinnerDateModel());
		departureTime.setEditor(new JSpinner.DateEditor(departureTime,"HH:mm"));
		arrivalTime= new JSpinner ();
		arrivalTime.setModel(new SpinnerDateModel());
		arrivalTime.setEditor(new JSpinner.DateEditor(arrivalTime,"HH:mm"));
		
		//initiating comboboxes and listeners
		clients = new JComboBox();
		toStations = new JComboBox();
		fromStations = new JComboBox();
		reservationList = new JComboBox();
		reservationList.addItemListener(this);
		
		//initiating other panels and setting up layout
		findClientArea = new JPanel();
		findClientAreaFinal = new JPanel();
		buttonArea = new JPanel();
		inputArea = new JPanel();
		findClientArea.setLayout(new GridLayout(0,2));
		findClientAreaFinal.setLayout(new BorderLayout());
		inputArea.setLayout(new GridLayout(0,2));
		buttonArea.setLayout(new GridLayout(0,2));
		setLayout(new BorderLayout());
		
		//adding components into jpanels
		buttonArea.add(initCreate);
		buttonArea.add(initModify);
		findClientArea.add(new JLabel("First Letter of Last Name"));
		findClientArea.add(letterInput);
		findClientArea.add(new JLabel ("Clients"));
		findClientArea.add(clients);
		findClientAreaFinal.add(findClient,BorderLayout.NORTH);
		findClientAreaFinal.add(findClientArea, BorderLayout.CENTER);
		findClientAreaFinal.add(buttonArea,BorderLayout.SOUTH);
		inputArea.add(new JLabel ("Capacity Input"));
		inputArea.add(capacityInput);	
		inputArea.add(new JLabel("From Station"));
		inputArea.add(fromStations);		
		inputArea.add(new JLabel ("To Station"));
		inputArea.add(toStations);
		inputArea.add(new JLabel("Departure Time"));
		inputArea.add(departureTime);
		inputArea.add(new JLabel("Arrival Time"));
		inputArea.add(arrivalTime);
		
		//setting reservationnumber for display only
		reservationNumberInput.setEditable(false);
		
		//adding into deliverable container
		add(findClientAreaFinal,BorderLayout.NORTH);
		add(inputArea,BorderLayout.CENTER);
		add(selectClient,BorderLayout.SOUTH);
		
		//so appropriate display is shown
		inputArea.setVisible(false);
		buttonArea.setVisible(false);
		selectClient.setVisible(false);

	}
	
	//fills client combo box
	private void initClientComboBox()
	{
		try
		{
			if (!letterInput.getText().equals(""))
			{
				fillIDItemsClients(clients,new Client().loadAll(),this);
			}
		}
		catch (IOException ie)
		{
			ie.printStackTrace();
		}
	}
	
	//fills fromStation and toStation combobox with only activated stations
	private void fillFromTo()
	{
		try
		{
			StationModifyPanel.fillIDItems(fromStations,(new Station()).loadAllActivated());
			StationModifyPanel.fillIDItems(toStations,(new Station()).loadAllActivated());
		}
		catch (IOException ie)
		{
			ie.printStackTrace();
		}
	}
	
	//fills reservations combobox with reservations
	private void initReservationComboBox()
	{
		fillIDItemsReservations();
	}
	
	//loads the current reservation onto screen
	private void loadInputsFrom(Reservation s)
	{
		try {
			reservationNumberInput.setText("" + s.get("reservationNumber"));
			fromStations.setSelectedIndex(StationModifyPanel.getBestIndex(fromStations,(String)s.get("from")));
			toStations.setSelectedIndex(StationModifyPanel.getBestIndex(toStations,(String)s.get("to")));
			capacityInput.setText("" + s.get("capacity"));
			departureTime.setValue(dateFormatTime.parseObject((String)s.get("departureTime")));
			arrivalTime.setValue(dateFormatTime.parseObject((String)s.get("arrivalTime")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	//gets the selected id from the clients comboBox
	public static int getSelectedIDFromClients()
	{

		String selectedText = ""+clients.getItemAt(clients.getSelectedIndex());
		String idParts[] = selectedText.split("-");
		// idParts = ["3 ", " Reservation Name"]
		String idStr = idParts[0].trim(); 
		// id = "3"
		int id = Integer.parseInt(idStr);
		return id;	
	}
	
	//gets the selected id from the to combo box
	public static int getSelectedIDFromTo()
	{

		String selectedText = ""+toStations.getItemAt(toStations.getSelectedIndex());
		String idParts[] = selectedText.split("-");
		// idParts = ["3 ", " Reservation Name"]
		String idStr = idParts[0].trim(); 
		// id = "3"
		int id = Integer.parseInt(idStr);
		return id;	

	}
	
	//gets the selected id from the from combo box
	public static int getSelectedIDFromFrom()
	{

		String selectedText = ""+fromStations.getItemAt(fromStations.getSelectedIndex());
		String idParts[] = selectedText.split("-");
		// idParts = ["3 ", " Reservation Name"]
		String idStr = idParts[0].trim(); 
		// id = "3"
		int id = Integer.parseInt(idStr);
		return id;	

	}
	
	//gets the selected id from the reservation combo box
	public static int getSelectedIDFromReservations()
	{

		if (reservationList.getItemAt(0)!=null)
		{
			return Integer.parseInt("" + reservationList.getItemAt(reservationList.getSelectedIndex()));
		}
		else
		{
			return -1;
		}

	}

	//updates reservation information on screen
	public void updateInputs()
	{
		int id=getSelectedIDFromReservations();
		if (id!=-1)
		{
			try
			{
				Reservation s = new Reservation(id);
				
				loadInputsFrom(s);
			}
			catch (IOException ie)
			{
				JOptionPane.showMessageDialog(this,"Trouble loading from file.");
			}
		}
	}
	
	//fills clients comboBox
	public static void fillIDItemsClients(JComboBox box,Model [] models)
	{
		fillIDItemsClients(box,models,null);
	}
	
	//fills clients comboBox
	public static void fillIDItemsClients(JComboBox box,Model [] models,Reloadable r)
	{
		box.removeAllItems();
		for (Model m: models)
		{
			if ((letterInput.getText().charAt(0)) == (("" + m.get("lastName")).charAt(0)))
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
		
	//fills reservations comboBox
	public static void fillIDItemsReservations()
	{
		reservationList.removeAllItems();
		
		String reservationListClient = tempClient.get("listOfReservations") + "";
		StringTokenizer st = new StringTokenizer(reservationListClient,".");
		while (st.hasMoreTokens())
		{
			reservationList.addItem(st.nextToken());
		}
		
	}
	
	public void actionPerformed(ActionEvent e) throws IllegalArgumentException
	{
		try
		{
			//if the user has inputed into the letterInput field, will display list of possible clients
			//else else error msg
			if (e.getSource()==findClient)
			{
				if ((letterInput.getText()).length()==1)
				{
					clients.removeAllItems();
			    	initClientComboBox(); 
			    	if (clients.getItemCount()>0)
			    	{
			    		selectClient.setVisible(true);
			    		
			    	}
			    	else
			    	{
			    		JOptionPane.showMessageDialog(this,"No Clients Found");
			    	}
				}
				else
				{
					JOptionPane.showMessageDialog(this,"Please enter exactly 1 character");
				}
			}
			
			//initiates the create display
			else if (e.getSource()==initCreate)
			{
				add(createReservation,BorderLayout.SOUTH);
				fillFromTo();
				inputArea.setVisible(true);
				buttonArea.setVisible(false);
			}
			
			//attempts to create reservation from user inputs
			//if there is an error, appropriate display is shown, reservation is not created
			//also modifies capacity of the train
			else if (e.getSource()==createReservation )
			{
				if (MyPanel.numbersOnly(capacityInput.getText()))
				{
					if (getSelectedIDFromTo()!=getSelectedIDFromFrom())
					{
						r = new Reservation();
						
						temp = new Train();
						temp.set("to", "" + getSelectedIDFromTo());
						temp.set("from", "" + getSelectedIDFromFrom());
						temp.set("departureTime", "" + dateFormatTime.format(departureTime.getValue()));
						temp.set("arrivalTime", "" + dateFormatTime.format(arrivalTime.getValue()));
						trainID = temp.getUniqueTrainIDActivated("" + getSelectedIDFromFrom(),"" + getSelectedIDFromTo(), 
								dateFormatTime.format(departureTime.getValue()), dateFormatTime.format(arrivalTime.getValue()));
						if (trainID != -1)
						{
							temp = new Train(trainID);
							newCapacity = Integer.parseInt(temp.get("capacity") + "") - 
									Integer.parseInt(capacityInput.getText());
							if (newCapacity >= 0)
							{	
								r.set("clientID", getSelectedIDFromClients());
								r.set("to", "" + getSelectedIDFromTo());
								r.set("from","" + getSelectedIDFromFrom());
								r.set("capacity", capacityInput.getText());
								r.set("departureTime", dateFormatTime.format(departureTime.getValue()));
								r.set("arrivalTime", dateFormatTime.format(arrivalTime.getValue()));
								r.set("train", "" + trainID );
								r.set("userName", MainFrame.currentUserName);
								r.set("fare", Integer.parseInt("" + temp.get("fare")) * Integer.parseInt("" + r.get("capacity")));
								r.addToFile();
								temp.set("capacity", newCapacity + "");
								temp.updateFile();
								if((tempClient.get("listOfReservations") + "").equals("-1"))
								{
									
									tempClient.set("listOfReservations", r.get("id") + ".");
								}
								else
								{
									tempClient.set("listOfReservations", tempClient.get("listOfReservations") + r.get("id") + ".");
								}
								
								tempClient.updateFile();
								
								JOptionPane.showMessageDialog(this,"The total cost is $" + r.get("fare"));
								MainFrame.setAndPaint(new BackPanel());
							}
							else
							{
								JOptionPane.showMessageDialog(this,"There is not enough space on the train");
							}
							
						}
						else
						{
							JOptionPane.showMessageDialog(this,"No Activate or Existing Trains for your Voyage");
						}
					}
					else
					{
						JOptionPane.showMessageDialog(this,"The From and To Station cannot be the same");
					}
					
				}
				else
				{
					JOptionPane.showMessageDialog(this,"Capacity must be a number");
				}
			}
			
			//user has selected a client, will initiate buttons display
			else if (e.getSource()==selectClient)
			{
				clients.removeAll();
	    		buttonArea.setVisible(true);
	    		findClient.setVisible(false);
	    		findClientArea.setVisible(false);
	    		selectClient.setVisible(false);
	    		tempClient = new Client(getSelectedIDFromClients());
			}
			
			//initiates modifying client display
			else if (e.getSource()==initModify)
			{
				tempClient = new Client(getSelectedIDFromClients());
				
				if (!(tempClient.get("listOfReservations") + "").equals("-1"))
				{
					fillFromTo();
					r=new Reservation (getSelectedIDFromReservations());
					initReservationComboBox();
					
					
					add(saveReservation,BorderLayout.SOUTH);
					
					
					inputArea.setVisible(true);
					buttonArea.setVisible(false);
					inputArea.add(new JLabel("Reservation Number"));
					inputArea.add(reservationNumberInput);
					inputArea.add(new JLabel("Reservations: "));
					inputArea.add(reservationList);

				}
				else
				{
					JOptionPane.showMessageDialog(this,"No existing reservations");
					MainFrame.setAndPaint(new BackPanel());
				}
				
			}
			
			//saves the modified reservation
			//if there is an error, appropriate msg is displayed, reservation is not saved
			//also modifies capacity of the train
			else if (e.getSource()==saveReservation)
			{
				if (MyPanel.numbersOnly(capacityInput.getText()))
				{
					if (getSelectedIDFromTo()!=getSelectedIDFromFrom())
					{
						
						Train temp = new Train();
						temp.set("to", "" + getSelectedIDFromTo());
						temp.set("from", "" + getSelectedIDFromFrom());
						temp.set("departureTime", "" + dateFormatTime.format(departureTime.getValue()));
						temp.set("arrivalTime", "" + dateFormatTime.format(arrivalTime.getValue()));
						trainID = temp.getUniqueTrainIDActivated("" + getSelectedIDFromFrom(),"" + getSelectedIDFromTo(), 
								dateFormatTime.format(departureTime.getValue()), dateFormatTime.format(arrivalTime.getValue()));
						if (trainID != -1)
						{
							temp = new Train(trainID);
							newCapacity = Integer.parseInt(temp.get("capacity") + "") - 
									Integer.parseInt(capacityInput.getText());
							if (newCapacity >= 0)
							{	
								//update reservation
								r = new Reservation(getSelectedIDFromReservations());
								System.out.println(r.get("id") + "");
								r.set("clientID", getSelectedIDFromClients());
								r.set("to", "" + getSelectedIDFromTo());
								r.set("from","" + getSelectedIDFromFrom());
								r.set("capacity", capacityInput.getText());
								r.set("departureTime", dateFormatTime.format(departureTime.getValue()));
								r.set("arrivalTime", dateFormatTime.format(arrivalTime.getValue()));
								r.set("train", "" + trainID );
								r.set("userName", MainFrame.currentUserName);
								r.set("fare", Integer.parseInt("" + temp.get("fare")) * Integer.parseInt("" + r.get("capacity")));
								r.updateFile();
								
								//update train
								temp.set("capacity", newCapacity + "");
								temp.updateFile();
								
								
								JOptionPane.showMessageDialog(this,"The total cost is $" + r.get("fare"));
								MainFrame.setAndPaint(new BackPanel());
							}
							else
							{
								JOptionPane.showMessageDialog(this,"There is not enough space on the train");
							}
							
						}
						else
						{
							JOptionPane.showMessageDialog(this,"No Activate or Existing Trains for your Voyage");
						}
					}
					else
					{
						JOptionPane.showMessageDialog(this,"The From and To Station cannot be the same");
					}
					
				}
				else
				{
					JOptionPane.showMessageDialog(this,"Capacity must be a number");
				}
			}
		}
		catch (IOException ie)
		{
			JOptionPane.showMessageDialog(this,"Unable to create train.");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void itemStateChanged(ItemEvent e)
	{

		if (e.getSource()==reservationList)
		{
			updateInputs();
		}
		
	}

}
	