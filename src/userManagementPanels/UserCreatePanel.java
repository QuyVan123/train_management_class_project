package userManagementPanels;

/*Panel added into MainFrame for creating a user
 * 	 	1)Obtain Information from user
 * 		2)User presses Create User
 * 		3)Checks if username unique, if true goes to step 4, if not sends message of error
 * 		4)Save user information into users.csv
 * 
 * User Input : username, password, userType
 * 
 * program : messages
 * 
 */

import generalPanels.BackPanel;
import generalPanels.MyPanel;

import javax.swing.*;

import main.MainFrame;
import model.User;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

@SuppressWarnings("serial")
public class UserCreatePanel extends MyPanel implements ActionListener
{
	//user input fields
	private JTextField usernameInput;
	private JTextField passwordInput;
	
	private JComboBox userTypes;// list of different types of users
	
	private JButton createUser;//button pressed to create user
	
	private JPanel inputArea;//jpanel for easier display
	
		public UserCreatePanel()
		{
			setTitle("Create User");
			//initializing input fields
			usernameInput = new JTextField(30);
			passwordInput = new JTextField(30);
			
			//creating buttons and adding action listeners
			createUser = new JButton("Create User");
			createUser.addActionListener(this);
			
			//initializing combobox and adding items to it
			userTypes = new JComboBox();
			userTypes.addItem("ADMIN");
			userTypes.addItem("MANAGER");
			userTypes.addItem("STAFF");
			
			//initializing other jpanels and setting up layouts
			inputArea = new JPanel();
			setLayout(new BorderLayout());
			inputArea.setLayout(new GridLayout(0,2));
			
			//adding components into container
			inputArea.add(new JLabel("Username"));
			inputArea.add(usernameInput);
			inputArea.add(new JLabel("Password"));
			inputArea.add(passwordInput);
			inputArea.add(new JLabel("User Type"));
			inputArea.add(userTypes);
			
			//adding components into deliverable container
			add(inputArea,BorderLayout.NORTH);
			add(createUser,BorderLayout.SOUTH);
		}
		
		private boolean emptyFields()
		{
			if (usernameInput.getText().equals("") ||
				passwordInput.getText().equals(""))
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
				//loads input fields to create a new user
				//checks if username is unique
				//saves user in users.csv
				if (e.getSource()==createUser)
				{
					if (!emptyFields())
					{
						User newUser = new User();
						newUser.set("username", usernameInput.getText());
						newUser.set("password", passwordInput.getText());
						newUser.set("type", ""+(1+userTypes.getSelectedIndex()));
						
						if (newUser.checkUnique("username"))
						{
							newUser.addToFile();
							JOptionPane.showMessageDialog(this,"Added user to database.");
						}
						else
						{
							JOptionPane.showMessageDialog(this,"Username already exist");
						}
						MainFrame.setAndPaint(new BackPanel());
					}
					else
					{
						JOptionPane.showMessageDialog(this,"All fields must be filled");
					}
					
				}			
			}
			catch (IOException ioe)
			{
				JOptionPane.showMessageDialog(this,"Problem adding user to database.");
				ioe.printStackTrace();
			} catch (InterruptedException ea) {
				// TODO Auto-generated catch block
				ea.printStackTrace();
			}
		}
}
