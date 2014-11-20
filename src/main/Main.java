package main;

import generalPanels.LoginPanel;

//Initiates Program

public class Main 
{
	
	static MainFrame mainFrame;
	public static void main(String[] args) throws InterruptedException
	{
		mainFrame = new MainFrame();
		MainFrame.setAndPaint(new LoginPanel());
	}
}
