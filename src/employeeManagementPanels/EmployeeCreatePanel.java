package employeeManagementPanels;

/*Panel added into MainFrame for creating a client
 * 	 	1)Obtain Information from user
 * 		2)User pressed Create Employee
 * 		3)Checks if national insurance number is unique, if not sends message of error
 * 		4)Save client information into clients.csv <-- IF UNIQUE
 * 
 * User Input : firstName, lastName, address, phone, email, sex, birthday
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
import java.util.Calendar;
import java.util.Date;
import java.awt.*;

@SuppressWarnings("serial")
public class EmployeeCreatePanel extends MyPanel implements ActionListener
{
	//user input fields
	private JTextField firstNameInput;
	private JTextField lastNameInput;
	private JTextField addressInput;
	private JTextField titleInput;
	private JTextField phoneInput;
	private JTextField salaryInput;
	private JTextField emailInput;
	private JTextField sexInput;
	private JTextField nationalInsuranceNumberInput;
	
	//stuff for birthday spinner
	private JSpinner birthdayInput;
	JSpinner.DateEditor dateEditor;
	
	
	private JButton createEmployee; // button pressed to save employee to employee.scv
	
	private JPanel inputArea; //jpanel for easier display
	
	public EmployeeCreatePanel() throws IllegalArgumentException
	{
		setTitle("Employee Create");
		
		//initializing input fields
		firstNameInput=new JTextField(15);
		lastNameInput=new JTextField(15);
		addressInput=new JTextField(15);
		titleInput = new JTextField(15);
		salaryInput = new JTextField(15);
		phoneInput=new JTextField(15);
		emailInput=new JTextField(15);
		sexInput=new JTextField(15);
		nationalInsuranceNumberInput = new JTextField(15);

		//initializing birthday stuff
	    birthdayInput = new JSpinner(new SpinnerDateModel(new Date(), null, null,
	        Calendar.MONTH));
	    dateEditor = new JSpinner.DateEditor(birthdayInput, "dd/MM/yy");
	    birthdayInput.setEditor(dateEditor);
		
		//initializing JPanels and setting up layouts
		setLayout(new BorderLayout());
		inputArea = new JPanel();
		inputArea.setLayout(new GridLayout(0,2));
		
		// initializing buttons and adding actionlisteners
		createEmployee=new JButton("Create Employee"); 
		createEmployee.addActionListener(this);
		
		//adding components to container
		inputArea.add(new JLabel("First Name"));
		inputArea.add(firstNameInput);
		inputArea.add(new JLabel("Last Name"));
		inputArea.add(lastNameInput);
		inputArea.add(new JLabel("Address"));
		inputArea.add(addressInput);
		inputArea.add(new JLabel ("Title"));
		inputArea.add(titleInput);
		inputArea.add(new JLabel ("SalaryInput"));
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
		
		//adding all components into deliverable container
		add(inputArea,BorderLayout.CENTER);
		add(createEmployee,BorderLayout.SOUTH);
	}

	
	public void actionPerformed(ActionEvent e) throws IllegalArgumentException
	{
		try
		{
			//checks if national insurance number is unique
			//saves employee input into employees.scv
			//goes back 1 panel
			if (e.getSource()==createEmployee)
			{
				if (!emptyFields())
				{
					if (titleInput.getText().equals("staff") || titleInput.getText().equals("manager"))
					{
						if (MyPanel.doubleOnly(salaryInput.getText()))
						{
							if (MyPanel.numbersOnly(phoneInput.getText()))
							{
								if (MyPanel.numbersOnly(nationalInsuranceNumberInput.getText()))
								{
									Employee employee = new Employee();	//holds employee information inputted by user
									
									employee.set("firstName", firstNameInput.getText());
									employee.set("lastName", lastNameInput.getText());
									employee.set("address", addressInput.getText());
									employee.set("title", titleInput.getText());
									employee.set("phone", phoneInput.getText());
									employee.set("email", emailInput.getText());
									employee.set("salary",salaryInput.getText());
									employee.set("sex", sexInput.getText());
									employee.set("birthday", dateFormatDay.format(birthdayInput.getValue()));
									employee.set("nationalInsuranceNumber", nationalInsuranceNumberInput.getText());
									if (employee.checkUnique("nationalInsuranceNumber"))
									{
										employee.addToFile();
										MainFrame.setAndPaint(new BackPanel());
										JOptionPane.showMessageDialog(this,"The Employee should be in the file now.");
									}
									else
									{
										JOptionPane.showMessageDialog(this,"National Insurance Number is not unique");
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
							JOptionPane.showMessageDialog(this,"Salary must be a double");
						}
					}
					else
					{
						JOptionPane.showMessageDialog(this,"Title must be manager or staff");
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
			JOptionPane.showMessageDialog(this,"Unable to create employee.");
			ie.printStackTrace();
		} catch (InterruptedException ea) {
			// TODO Auto-generated catch block
			ea.printStackTrace();
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
	
}

			
			
			
			