package employeeManagementPanels;

/*Panel added into MainFrame for modifying employees
 * 	 	1)User enters firstname and lastname
 * 		2)user pressed update, comboBox is filled if firstname and lastname are found
 * 		3)all user input fields for employee are displayed
 * 		4)user modifies the input fields
 * 		5) user presses save
 * 		6)if national insurance number is unique, it is saved
 */

import main.MainFrame;
import misc.Reloadable;
import model.*;
import generalPanels.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.awt.*;

@SuppressWarnings("serial")
public class EmployeeModifyPanel extends MyPanel implements ActionListener, Reloadable, ItemListener
{
	private JComboBox employees;//used to display list of employees

	private static JButton modifyEmployee; // button pressed to delete employee
	private JButton update; // button pressed to update comboBox with
							//information found in firstNameInput and lastNameInput
	
	// jpanel for easier display
	private JPanel searchArea; 
	private JPanel updateArea;
	private static JPanel inputArea;
	
	//stuff for birthday spinner
	private JSpinner birthdayInput;
	JSpinner.DateEditor dateEditor;
	private SpinnerDateModel spinnerModel;
	
	//user input fields
	private static JTextField firstNameInput;	//user input is used to fill comboBox, check fillIDItems
	private static JTextField lastNameInput;	//user input is used to fill comboBox, check fillIDItems
	private JTextField addressInput;
	private JTextField titleInput;
	private JTextField phoneInput;
	private JTextField salaryInput;
	private JTextField emailInput;
	private JTextField sexInput;
	private JTextField nationalInsuranceNumberInput;									
	
	private static String delim = "-"; // used when displaying in comboBox, and retrieving back the information
	
	
	
	public EmployeeModifyPanel() throws IllegalArgumentException
	{
		setTitle("Employee Modify");
		
		//initializing birthday stuff
		spinnerModel = new SpinnerDateModel(new Date(), null, null, Calendar.MONTH);
	    birthdayInput = new JSpinner(spinnerModel);
	    dateEditor = new JSpinner.DateEditor(birthdayInput, "dd/MM/yy");
	    birthdayInput.setEditor(dateEditor);
	    
		//initializing input containers
		firstNameInput=new JTextField(15);
		lastNameInput=new JTextField(15);
		addressInput=new JTextField(15);
		titleInput = new JTextField(15);
		phoneInput=new JTextField(15);
		salaryInput=new JTextField(15);
		emailInput=new JTextField(15);
		sexInput=new JTextField(15);
		nationalInsuranceNumberInput = new JTextField(15);
		
		//initializing buttons and adding action listeners
		update = new JButton("Update");
		update.addActionListener(this);
		modifyEmployee=new JButton("Modify Employee");
		modifyEmployee.addActionListener(this);
		
		//initializing other jpanels and adding action listeners
		setLayout(new BorderLayout());
		updateArea = new JPanel();
		updateArea.setLayout(new BorderLayout());
		searchArea = new JPanel();
		searchArea.setLayout(new GridLayout(0,2));
		inputArea = new JPanel();
		inputArea.setLayout(new GridLayout(0,2));
		
		initEmployeesComboBox(); //initializing comboBox
		employees.addItemListener(this);

		
		//adding containers into JPanels
		searchArea.add(new JLabel("Possible Employees"));
		searchArea.add(employees);
		searchArea.add(new JLabel("Enter First Name"));
		searchArea.add(firstNameInput);
		searchArea.add(new JLabel("Enter Last Name"));
		searchArea.add(lastNameInput);
		
		inputArea.add(new JLabel("Address"));
		inputArea.add(addressInput);
		inputArea.add(new JLabel("Title"));
		inputArea.add(titleInput);
		inputArea.add(new JLabel("Salary"));
		inputArea.add(salaryInput);
		inputArea.add(new JLabel("Phone Number"));
		inputArea.add(phoneInput);
		inputArea.add(new JLabel("Email"));
		inputArea.add(emailInput);
		inputArea.add(new JLabel("Sex"));
		inputArea.add(sexInput);
		inputArea.add(new JLabel("Birthday (dd/MM/yy)"));
		inputArea.add(birthdayInput);
		inputArea.add(new JLabel("National Insurance Number"));
		inputArea.add(nationalInsuranceNumberInput);
		updateArea.add(update,BorderLayout.NORTH);
		updateArea.add(searchArea,BorderLayout.CENTER);
		
		//adding all containers into deliverable container
		add(updateArea,BorderLayout.NORTH);
		add(inputArea,BorderLayout.CENTER);
		add(modifyEmployee,BorderLayout.SOUTH);
		inputArea.setVisible(false);
		modifyEmployee.setVisible(false);
		
	}
	//loads all information of Employee into panel
	//used in updateInputs()
	private void loadInputsFrom(Employee s)
	{
		
		try 
		{
			firstNameInput.setText("" + s.get("firstName"));
			lastNameInput.setText("" + s.get("lastName"));
			addressInput.setText("" + s.get("address"));
			titleInput.setText("" + s.get("title"));
			salaryInput.setText("" + s.get("salary"));
			phoneInput.setText("" + s.get("phone"));
			emailInput.setText("" + s.get("email"));
			sexInput.setText("" + s.get("sex"));
			spinnerModel.setValue(dateFormatDay.parse((String)s.get("birthday")));
			nationalInsuranceNumberInput.setText("" + s.get("nationalInsuranceNumber"));
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//gets the ID from selected item in comboBox
	//used in getSelectedIDFrom()
	public static int getSelectedIDFrom(JComboBox box)
	{

		if (box.getItemAt(0)!=null)
		{
			inputArea.setVisible(true);
			modifyEmployee.setVisible(true);
			String selectedText = ""+box.getItemAt(box.getSelectedIndex());
			String idParts[] = selectedText.split("-");
			// idParts = ["3 ", " Employee Name"]
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
	
	//gets the ID from selected item in comboBox
	//used in updateInputs()
	private int getSelectedID()
	{
		return getSelectedIDFrom(this.employees);
	}
	
	//Updates the input containers with employee information
	//used throughout class
	public void updateInputs()
	{
		int id=getSelectedID();
		Employee s;

		try
		{
			if (id!=-1)
			{
				 s = new Employee(id);
				 loadInputsFrom(s);
			}
			else
			{
				JOptionPane.showMessageDialog(
						this,
						"Failed finding any employee(s)");
				inputArea.setVisible(false);
				modifyEmployee.setVisible(false);
			}
				
		}
		catch (IOException ie)
		{
			JOptionPane.showMessageDialog(
				this,
				"Trouble loading from file.");
		}
	}
	
	//fill comboBox with models (for the purpose of employees)
	//used in initClientsComboBox()
	public static void fillIDItems(JComboBox box,Model [] models)
	{
		fillIDItems(box,models,null);
	}
	
	//fill comboBox with model information (for the purpose of employees)
	//only uses information found in firstName field and lastName field  when the update button was pressed
	//ID, firstName, lastName are displayed in comboBox
	//used in fillIDItems(JComboBox, Model[])
	public static void fillIDItems(JComboBox box,Model [] models,Reloadable r)
	{
		box.removeAllItems();
		for (Model m: models)
		{
			if (firstNameInput.getText().equals(m.get("firstName")) && lastNameInput.getText().equals(m.get("lastName")))
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
	
	// load options in comboBox.
	// obtains Employees with lastNames that start with letterInput
	// used throughout class
	private void initEmployeesComboBox()
	{
		try
		{
			if (this.employees==null)
				this.employees = new JComboBox();
			if (!firstNameInput.getText().equals("") && !lastNameInput.getText().equals(""))
			{
				fillIDItems(this.employees,new Employee().loadAll(),this);
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
			titleInput.getText().equals("") ||
			phoneInput.getText().equals("") ||
			emailInput.getText().equals("") ||
			salaryInput.getText().equals("") ||
			sexInput.getText().equals("") ||
			nationalInsuranceNumberInput.getText().equals(""))
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
			//modifies employee from employees.scv
			//checks if National Insurance Number is unique
			//goes back 1 panel
			if (e.getSource()==modifyEmployee)
			{		
				if (!emptyFields())
				{
					if (titleInput.getText().equals("staff") || titleInput.getText().equals("manager"))
					{
						Employee employee = new Employee(); // used to hold selected, and modified client information
						if (employees!=null)
							employee.set("id", ""+getSelectedID());
						
						// selected client information is uploaded
						employee.set("firstName", firstNameInput.getText());
						employee.set("lastName", lastNameInput.getText());
						employee.set("address", addressInput.getText());
						employee.set("title", titleInput.getText());
						employee.set("phone", phoneInput.getText());
						employee.set("salary",salaryInput.getText());
						employee.set("email", emailInput.getText());
						employee.set("sex", sexInput.getText());
						employee.set("birthday", dateFormatDay.format(birthdayInput.getValue()));
						employee.set("nationalInsuranceNumber", nationalInsuranceNumberInput.getText());
						if (MyPanel.numbersOnly("" + employee.get("phone")))
						{
							if (MyPanel.numbersOnly("" + employee.get("nationalInsuranceNumber")))
							{
								if (employee.checkUnique("nationalInsuranceNumber"))
								{
									employee.updateFile();
									MainFrame.setAndPaint(new BackPanel());
									JOptionPane.showMessageDialog(this,"The Employee should be saved");
								}
								else
								{
									JOptionPane.showMessageDialog(this,"The National Insurance Number is not unique");
								}
							}
							else
							{
								JOptionPane.showMessageDialog(this,"National Insurance Number should be numbers only");
							}
						}
						else
						{
							JOptionPane.showMessageDialog(this,"Phone number should be numbers only");
						}
					}
					else
					{
						JOptionPane.showMessageDialog(this,"Title must be either \"staff\" or \"manager\"");
					}
				}
					
				else
				{
					JOptionPane.showMessageDialog(this,"All fields must be filled");
				}
				
				
			}
			
			//updates comboBox with information found in firstNameInput and lastNameInput
			else if (e.getSource()==update)
			{
				employees.removeAllItems();
		    	initEmployeesComboBox();
				updateInputs();
		    	
			}
		}
		catch (IOException ie)
		{
			JOptionPane.showMessageDialog(this,"Unable to create employee.");
			ie.printStackTrace();
		} catch (InterruptedException ea) {
			// TODO Auto-generated catch block
			ea.printStackTrace();
		}
	}
	public void itemStateChanged(ItemEvent e)
	{
		if (e.getSource()==employees)
		{
			updateInputs();
		}
	}
}
