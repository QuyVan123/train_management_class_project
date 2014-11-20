package generalPanels;

/*Panel added into MainFrame for logging in
 * 	 	1)User inputs username and pw, pw is not visible
 * 		2)presses login, appropriate menu is displayed (staff,manager,admin)
 *		3)msg is displayed if user has 1
 */

import javax.imageio.*;
import javax.swing.*;
import main.MainFrame;
import model.Model;
import model.User;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.File;
import java.util.StringTokenizer;

@SuppressWarnings("serial")
public class LoginPanel extends MyPanel implements ActionListener
{
	//user input fields
	private JTextField usernameInput;
	private JPasswordField passwordInput;
	
	//jpanel for easier display
	private JPanel imageDisplay;
	private JPanel p;
	
	private JButton submitBtn;//user to attempt to login
	
	private User user; // holds information on the current user
	
	
	//used to tokenize message, to display messages in an easier view
	private StringTokenizer st; 
	private String line;
	
	public LoginPanel()
	{
		setTitle("Login");
		
		//initializing input fields
		usernameInput = new JTextField();
        passwordInput = new JPasswordField();
        
       
        
        //initializing other panels and setting up layouts
        p = new JPanel();
        p.setLayout(new GridLayout (2,2));
        setLayout(new BorderLayout());
        imageDisplay = new ImagePanel();
        
        //initializing buttons and adding action listeners
        submitBtn = new JButton("Sign In");
        submitBtn.addActionListener(this);

        //placing components into panels
        p.add(new JLabel ("Username"));
        p.add(usernameInput);
        p.add(new JLabel ("Password"));
        p.add(passwordInput);
       
        //adding components into deliverable container
        add(p,BorderLayout.CENTER);
        add(submitBtn,BorderLayout.SOUTH);
        add(imageDisplay,BorderLayout.NORTH);
		
	}
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e)
	{
		  String username=usernameInput.getText();
		  String password=passwordInput.getText();
		  
		  try{
			  user = new User(username);
			  if (!user.isLoaded())
				  JOptionPane.showMessageDialog(this, "That username doesn't exist.");
			  else if (user.get("password").equals(password))
			  {
				  MainFrame.currentUserName=username;
				  
				  //checks for messages
				  if (!((user.get("message") + "").equals("-1")))
				  {
					  	line = user.get("message") + "";
						st = new StringTokenizer(line,"-");
						while (st.hasMoreTokens())
						{
							JOptionPane.showMessageDialog(this, st.nextToken());
						}
						user.set("message", "-1");
						user.updateFile();
				  }
				  
				  //Admin menu
				  if (user.isAdmin())
				  {
					  MainFrame.setAndPaint (new AdminMenuPanel());
				  }
				  //User Menu
				  else if (user.isManager())
				  {
					  MainFrame.setAndPaint (new ManagerMenuPanel());
				  }
				  //Staff Menu
				  else if (user.isStaff())
				  {
					  MainFrame.setAndPaint(new StaffMenuPanel());
				  }
				  else
				  {
					  JOptionPane.showMessageDialog(this,
							  "Authenticated!!! you are who you say you are.");
				  }
			  }
			  else
			  {
				  JOptionPane.showMessageDialog(null, "Wrong password!");
			  }
		  }
		  catch (IOException ioe)
		  {
			  ioe.printStackTrace();
		  } 
		  catch (InterruptedException ie) 
		  {
			// TODO Auto-generated catch block
			ie.printStackTrace();
		}
	}

	//used to display image  
	static class ImagePanel extends JPanel
	{
		private Image image;

		public ImagePanel()
		 {
			 try
			 {
				 image = ImageIO.read(new File("data/shinkansen1.jpg"));
			 }
			 catch (Exception e)
			 {
				 e.printStackTrace();
			 }
			 
			 setSize(450,300);
			 
		 }
		 
		 /*
		  * The following methods were used to keep the image the right size.
		  * */
	    @Override
	    public Dimension getMinimumSize() {
	        return new Dimension(400,274);
	    }
		 
	    @Override
	    public Dimension getPreferredSize() {
	        return getMinimumSize();
	    }
	    
	    public void paint(Graphics g)
		 {
			 if (g!=null && image!=null)
				 g.drawImage(image,0,0,null);
		 }
	}
}
