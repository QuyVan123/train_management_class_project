package userManagementPanels;

/*Panel added into MainFrame for user management
 * 	 	1)Displays 3 buttons for user: Create user, delete user, change pw
 * 
 * user presses Create User
 * 		1)loads the create user panel
 * 
 * user presses Delete User
 * 		1)ask for username
 * 		2)user inputs username, presses ok
 * 		3)if username exists, deletes the user
 * 			-->if there is a message, user cannot be deleted
 * 			-->if username does not exists, sends error message
 * 
 * user presses Change Password
 * 		1)ask for username
 * 		2)user inputs username and presses ok
 * 		3)if username does not exists, sends error message
 * 		4)if username exists, ask what to change pw to
 * 		5)user inputs, presses ok
 * 		6)password is saved into users.csv
 */

import generalPanels.MyPanel;

import javax.swing.*;



import main.MainFrame;
import model.User;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

@SuppressWarnings("serial")
public class UserManagementPanel extends MyPanel implements ActionListener
{
private JButton createUserBtn;
private JButton deleteUserBtn;
private	 JButton replacePasswordBtn;

	public UserManagementPanel()
	{
		setTitle("User Management");
		 //initializing buttons and loading action listeners
		 createUserBtn = new JButton("Create User");
		 deleteUserBtn = new JButton("Delete User");
		 replacePasswordBtn = new JButton("Change Password");
		 createUserBtn.addActionListener(this);
		 deleteUserBtn.addActionListener(this);
		 replacePasswordBtn.addActionListener(this);
		
		 setTitle("User Management");//setting up window title
		 
		 setLayout(new FlowLayout()); // setting up layout
		 
		 //adding components into deliverable container
		 add(createUserBtn);
		 add(deleteUserBtn);
		 add(replacePasswordBtn);
	}
	 
	 public void actionPerformed(ActionEvent e)
	 {
		 try
		 {
			//goes to user create panel
			 if (e.getSource()==createUserBtn)
			 {
				MainFrame.setAndPaint (new UserCreatePanel());
			 }
			 //obtains username input from user
			 //if user exists, it is deleted
			 else if (e.getSource()==deleteUserBtn)
			 {
				 String username = JOptionPane.showInputDialog("What is the username of the user you want to delete?");
				 User user=null;
				 user = new User(username);
				 if (!user.isLoaded())
				 {
					 JOptionPane.showMessageDialog(this, "That user doesn't exist.");
				 }
				 else
				 {
					 if (user.get("message").equals("-1"))
					 {
						 user.delete();
						 JOptionPane.showMessageDialog(this,"User deleted.");
					 }
					 else
					 {
						 JOptionPane.showMessageDialog(this,"User still has messages.");
					 }
					
				 }
			 }
			
			//obtains username input from user
			 //if user exists, asks for new password
			 //saves password to users.csv
			 else if (e.getSource()==replacePasswordBtn)
			 {
				 String username = JOptionPane.showInputDialog("What is the username of the user you want to change password?");
				 User user=null;
				 user = new User(username);
	
				 if (!user.isLoaded())
				 {
					 JOptionPane.showMessageDialog(this, "That user doesn't exist.");
				 }
				 else
				 {
					 String newpassword = JOptionPane.showInputDialog("What is the new password?");
					 user.set("password",newpassword);
					 user.updateFile();
					 
					 JOptionPane.showMessageDialog(this,"User updated.");
				 }
			 }
		 }
		 catch (InterruptedException e1) 
		 {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		 } catch (IOException ea) {
			// TODO Auto-generated catch block
			ea.printStackTrace();
		}
	}
}
