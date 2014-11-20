package main;

/*MainFrame for program, acts as the main window, r
 * 		-->has method that removes all content and displays new panel
 *
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import generalPanels.*;
import javax.swing.JButton;
import javax.swing.JFrame;


public class MainFrame implements ActionListener
{
	
	static JFrame mainFrame; // same frame throughout whole program
	
	static JButton back ;// same back button, exists on all panels except login panel
	
	static MyPanel[] pageList = new MyPanel[10]; // used to keep track of pages (for back button)
	
	static int count;//helps to keep track of pages
	
	public static String currentUserName; // current user logged in
	
	public MainFrame()
	{
		//intiializing button and adding action listener
		back = new JButton("BACK");
		back.addActionListener(this);
		
		count = 0; //initializing count
		
		//initializing Frame and setting layout
		mainFrame = new JFrame();
		mainFrame.setSize (400,450);
		mainFrame.setResizable(false);
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	}
	
	//removes all content in JFrame
	//adds back button if it is not the login page
	//adds new MyPanel then validates, and repaints
	public static void paintPanel(MyPanel inPanel) throws InterruptedException
	{

		mainFrame.getContentPane().removeAll();
		
		if (count!=1) // back cannot exist in first frame (loginPanel)
		{
			mainFrame.add(back,BorderLayout.SOUTH);
		}
		mainFrame.add(inPanel,BorderLayout.CENTER);
		mainFrame.getContentPane().validate();
		mainFrame.repaint();
	    
	}
	
	//Used to set title of the frame
	public void setTitle(String input)
	{
		mainFrame.setTitle(input);
	}
	
	//paints the MyPanel input
	//checks if it is the back button, if so, paints the previous panel
	// sets appropriate title of current panel
	//-->count keeps track of position of page List
	public static void setAndPaint(MyPanel input) throws InterruptedException
	{
		if (!input.getBack())
		{
			pageList[count] = input;
			mainFrame.setTitle(input.getTitle());
			count++;
			paintPanel(pageList[count-1]);
		}
		else
		{
			count--;
			paintPanel(pageList[count-1]);
		}
			
	}
	
	public void actionPerformed(ActionEvent e)
	{
		try {
			//initiate back
			if (e.getSource()==back)
			{
				setAndPaint(new BackPanel());
			}
			
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
