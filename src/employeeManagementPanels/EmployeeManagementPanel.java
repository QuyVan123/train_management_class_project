package employeeManagementPanels;

/*Panel added into MainFrame for client management
 * 	 	1)Displays 3 buttons for user: Create Client, Edit Client, Delete Client
 * 		2)User presses any of the buttons, loads the appropriate panel
 */

import generalPanels.MyPanel;
import javax.swing.*;

import main.MainFrame;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class EmployeeManagementPanel extends MyPanel implements ActionListener
{
	//buttons pressed to intiiate next panel
	private JButton createEmployee;
	private JButton deleteEmployee;
	private JButton modifyEmployee;

	public EmployeeManagementPanel()
	{
		setTitle("Employee Management");
		
		//initializing buttons and adding action listeners
		createEmployee=new JButton("Create New Employee");
		deleteEmployee=new JButton("Delete Employee");
		modifyEmployee = new JButton("Modify Employee");
		createEmployee.addActionListener(this);
		deleteEmployee.addActionListener(this);
		modifyEmployee.addActionListener(this);
		
		
		setLayout(new FlowLayout());//setting up layout
		
		//adding components into deliverable container
		add(createEmployee);
		add (modifyEmployee);
		add(deleteEmployee);
		
		
	}
	public void actionPerformed(ActionEvent e)
	{
		try 
		{
				//Goes to CreateEmployeePanel
				if (e.getSource()==createEmployee)
				{
					MainFrame.setAndPaint(new EmployeeCreatePanel());
				}
				//Goes to DeleteEmployeePanel
				else if (e.getSource()==deleteEmployee)
				{
					MainFrame.setAndPaint(new EmployeeDeletePanel());
				}
				//Goes to ModifyEmployeePanel
				else if (e.getSource()==modifyEmployee)
				{
					MainFrame.setAndPaint(new EmployeeModifyPanel());
				}
		}
		 catch (InterruptedException e1) 
		 {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		 }
	}
		
}