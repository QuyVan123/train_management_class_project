package employeeManagementPanels;

/*Panel added into MainFrame for deleteing employees
 * 	 	1)Receives firstNameInput and lastNameInput from user
 * 		2)User pressed Update button
 * 		3)comboBox is filled with employees with the same firstName and lastName
 * 		4)user pressed delete button
 * 		5)check if there are unread messages, if so cannot be deleted TODO ...switched to users
 * 		6)employee is removed from employees.scv
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
public class EmployeeDeletePanel extends MyPanel implements ActionListener
{
	private JComboBox employees;//used to display list of employees

	private JButton deleteEmployee; // button pressed to delete employee
	private JButton update; // button pressed to update comboBox with
							//information found in firstNameInput and lastNameInput
	
	private JPanel inputArea; // jpanel for easier display
	
	//user input fields
	private static JTextField firstNameInput;	//user input is used to fill comboBox, check fillIDItems
	private static JTextField lastNameInput;	//user input is used to fill comboBox, check fillIDItems								
	
	private static String delim = "-"; // used when displaying in comboBox, and retrieving back the information
	
	
	
	public EmployeeDeletePanel() throws IllegalArgumentException
	{
		setTitle("Employee Delete");
		
		//initializing input containers
		firstNameInput=new JTextField(15);
		lastNameInput=new JTextField(15);
		
		//initializing buttons and adding action listeners
		update = new JButton("Update");
		update.addActionListener(this);
		deleteEmployee=new JButton("Delete Employee");
		deleteEmployee.addActionListener(this);
		
		//initializing other jpanels and adding action listeners
		setLayout(new BorderLayout());
		inputArea = new JPanel();
		inputArea.setLayout(new GridLayout(0,2));
		
		initEmployeesComboBox(); //initializing comboBox
		
		//adding containers into JPanels
		inputArea.add(new JLabel("Enter First Name"));
		inputArea.add(firstNameInput);
		inputArea.add(new JLabel("Enter Last Name"));
		inputArea.add(lastNameInput);
		inputArea.add(new JLabel("Possible Employees"));
		inputArea.add(employees);
		
		//adding all containers into deliverable container
		add(update,BorderLayout.NORTH);
		add(inputArea,BorderLayout.CENTER);
		add(deleteEmployee,BorderLayout.SOUTH);
		
	}
	
	//gets the ID from selected item in comboBox
	//used in getSelectedIDFrom()
	public static int getSelectedIDFrom(JComboBox box)
	{

		if (box.getItemAt(0)!=null)
		{
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
				fillIDItems(this.employees,new Employee().loadAll());
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
			//deletes employee from employees.scv
			//goes back 1 panel
			if (e.getSource()==deleteEmployee)
			{
				index = employees.getSelectedItem().toString().indexOf(delim);
				Employee employee = new Employee(Integer.parseInt(employees.getSelectedItem().toString().substring(0,index)));
				employee.delete();
				MainFrame.setAndPaint(new BackPanel());
				JOptionPane.showMessageDialog(this,"The Employee should be deleted now.");
			}
			//updates comboBox with information found in firstNameInput and lastNameInput
			else if (e.getSource()==update)
			{
				employees.removeAllItems();
		    	initEmployeesComboBox(); 
		    	
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
}
