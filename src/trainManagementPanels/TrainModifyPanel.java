package trainManagementPanels;

/*Panel added into MainFrame for modifying a client
 * 		1) user selects from list of trains
 * 		2)displays information about train, some information is editable, some are not
 * 		3)if the train has a reservation, it cannot be deactivated
 *
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
import java.awt.*;


@SuppressWarnings("serial")
public class TrainModifyPanel extends MyPanel implements ActionListener,Reloadable, ItemListener
{
	//user input fields
	private JTextField nameInput;
	private JTextField fareInput;
	private JTextField capacityInput;
	private JCheckBox activatedInput;
	
	//comboBoxes
	private JComboBox trains; // list of trains
	private JComboBox fromBox; // list of stations
	private JComboBox toBox; //list of stations
	
	private JSpinner departureTimeInput;
	private JSpinner arrivalTimeInput;

	private static JButton saveTrain; // button pressed to modify train
	
	private static boolean trainUseFlag; // used so that other panels can use filliditems without causing error
	
	private static JPanel inputArea;
	
	private String beforeActivated;
	
	private Reservation res;
	
	private boolean reservedFlag;
	
	public TrainModifyPanel( )
	{
		setTitle("Train Modify");
		//initializing input fields
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
		
		//initiating comboBoxes and filling
		initTrainBox();
		toBox=new JComboBox();
		fromBox=new JComboBox();
		fillFromTo();
		
		trains.addItemListener(this);
		
		
		//initiating buttons and adding actionListeners
		// set size of capacity spinner.
		saveTrain=new JButton("Save Changes to Train");
		saveTrain.addActionListener(this);
		
		//initializing other JPanels and setting up layouts
		inputArea = new JPanel();
		inputArea.setLayout(new GridLayout(0,2));
		setLayout(new BorderLayout());
		
		//adding components into jpanels
		inputArea.add(new JLabel("Train"));
		inputArea.add(trains);
		inputArea.add(new JLabel("Name"));
		inputArea.add(nameInput);
		inputArea.add(new JLabel("From"));
		inputArea.add(fromBox);
		inputArea.add(new JLabel("To"));
		inputArea.add(toBox);
		inputArea.add(new JLabel("Departure Time"));
		inputArea.add(departureTimeInput);
		inputArea.add(new JLabel("Arrival Time"));
		inputArea.add(arrivalTimeInput);
		inputArea.add(new JLabel("Fare"));
		inputArea.add(fareInput);
		inputArea.add(new JLabel("Capacity"));
		inputArea.add(capacityInput);
		inputArea.add(new JLabel("Activated"));
		inputArea.add(activatedInput);

		//adding components into deliverable container
		add(inputArea,BorderLayout.NORTH);
		add(saveTrain,BorderLayout.SOUTH);
		
		//Blocking out input fields so that user cannot modify
		nameInput.setEditable(false);
		capacityInput.setEditable(false);
		inputArea.setVisible(false);
		saveTrain.setVisible(false);
		trainUseFlag = true;
		updateInputs();
		
	}
	
	//fills in the from and to stations
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
	
	///gets the ID from selected item in comboBox
	//used in updateInputs()
	private int getSelectedID()
	{
		return getSelectedIDFrom(this.trains);
	}
	
	
	public void updateInputs()
	{
		int id=StationModifyPanel.getSelectedIDFrom(trains);
		try
		{
			if (id!=-1)
			{
				inputArea.setVisible(true);
				saveTrain.setVisible(true);
				Train t = new Train(id);
				
				nameInput.setText((String)t.get("name"));
				fromBox.setSelectedIndex(StationModifyPanel.getBestIndex(fromBox,(String)t.get("from")));
				toBox.setSelectedIndex(StationModifyPanel.getBestIndex(toBox,(String)t.get("to")));
				departureTimeInput.setValue(dateFormatTime.parseObject((String)t.get("departureTime")));
				arrivalTimeInput.setValue(dateFormatTime.parseObject((String)t.get("arrivalTime")));
				fareInput.setText((String)t.get("fare"));
				capacityInput.setText((String)t.get("capacity"));
				activatedInput.setSelected(t.isActivated());
				beforeActivated = (t.isActivated() + "");
			}
			else
			{
				JOptionPane.showMessageDialog(
						this,
						"Failed finding any Trains(s)");
				inputArea.setVisible(false);
				saveTrain.setVisible(false);
			}
			

		}
		catch (IOException ie)
		{
			ie.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void initTrainBox()
	{
		if (trains==null)
			trains=new JComboBox();

		try
		{
			//SHOULD NOT BE STATION PANEL, should be train
			fillIDItems(trains,(new Train()).loadAll(),this);
		}
		catch (IOException ie)
		{
			ie.printStackTrace();
		}
	}
	
	//fill comboBox with model information (for the purpose of Trains)
	//ID, firstName, lastName are displayed in comboBox
	//used in fillIDItems
	public static void fillIDItems(JComboBox box,Model [] models,Reloadable r)
	{
		box.removeAllItems();
		
		for (Model m: models)
		{
			box.addItem(""+m.get("id")+" - "+m.get("name"));
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
			//saves the modified train
			if (e.getSource()==saveTrain)
			{
				if (!emptyFields())
				{
					if (MyPanel.numbersOnly(capacityInput.getText()))
					{
						if (!(StationModifyPanel.getSelectedIDFrom(fromBox) == StationModifyPanel.getSelectedIDFrom(toBox)))
						{
							if (MyPanel.doubleOnly(fareInput.getText()))
							{
								Train train = new Train(); //used to hold selected train information
								if (trains!=null)
									train.set("id", ""+getSelectedID());
								
								//selected clientInformation
								train.set("name", nameInput.getText());
								train.set("from",""+ StationModifyPanel.getSelectedIDFrom(fromBox));
								train.set("to",""+ StationModifyPanel.getSelectedIDFrom(toBox));
								train.set("departureTime", dateFormatTime.format(departureTimeInput.getValue()));
								train.set("arrivalTime", dateFormatTime.format(arrivalTimeInput.getValue()));
								train.set("capacity", capacityInput.getText());
								train.set("fare", fareInput.getText());
								train.set("activated", ""+activatedInput.isSelected());
								reservedFlag = false;
								if ((beforeActivated).equals("true") && (train.get("activated") + "").equals("false") )
								{
									
									res = new Reservation();
									Model[] all = res.loadAll();
									for (Model m: all)
									{	
										if ((m.get("train") + "").equals(train.get("id") + ""))
										{
											reservedFlag = true;
										}
									}
									
								}
								if(reservedFlag == false)
								{
									train.updateFile();
									JOptionPane.showMessageDialog(this,"The Train should be in the file now.");
									MainFrame.setAndPaint(new BackPanel());
								}
								else
								{
									JOptionPane.showMessageDialog(this,"The Train has a reservation and cannot be deactivated");
								}
								
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
						JOptionPane.showMessageDialog(this,"Invalid Capacity");
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
	public void itemStateChanged(ItemEvent e)
	{
		if (e.getSource()==trains)
		{
			updateInputs();
		}
	}
}
