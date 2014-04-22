package checkers;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

@SuppressWarnings("serial")
public class CheckerFrame extends JFrame implements ActionListener
{
	//Start button located on the bottom center of "this" CheckerFrame.
    JButton stB=new JButton("Start Game"); 
    //Creates a panel that isn't used until the JButton stB is pressed.
    JPanel gmP=new StartPanel(); 
  
    /**
     * When a new CheckerFrame is made, it tries to set 
     * the Look and Feel of the JFrame to Windows. 
     * Then it executes the method setupGUI() and plays a startup sound.
     */
    CheckerFrame()
    {
        try 
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this); //changing the appearence of the frame
        }
        catch (Exception e) 
        {
           //no need to handle exception as it only affect the appearence
        }
        setupGUI();
        //Plays sound when JFrame and its components are done being drawn.
        new PlaySound("src/sounds/Start.wav").start(); 
    }

    /**
     * Initializes the GUI for CheckerFrame.
     */
    private void setupGUI() 
    {    	
        setLayout(null);
        
        gmP.setBounds(0,0,508,401);//400,401
        //gmP.imageUpdate(ne, WIDTH, WIDTH, WIDTH, WIDTH, WIDTH)
        add(gmP);
        
        //Sets the alignment of text in the JButton stB.
        stB.setHorizontalAlignment(SwingConstants.LEADING); 
        stB.setIcon(new ImageIcon(getClass().getResource("/images/checkersIcon.jpg")));
        stB.setBackground(Color.LIGHT_GRAY);
        //Converts the cursor from pointer to finger when moused over.
        stB.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        stB.setBounds(154,420,200,60); //Coordinates and size.
        stB.setFont(new Font("Times new roman",Font.BOLD,20));
        stB.addActionListener(this);
        /*
         * Prevents a focus box to be drawn over the 
         * JButton stB when the program is the focused window.
         */
        stB.setFocusPainted(false);
        add(stB); //Adds the JButton stB to CheckerFrame.

        //Sets "this" CheckerFrame's titlebar image.
        this.setIconImage(new ImageIcon(getClass().getResource("/images/icon.jpg")).getImage()); 

        setSize(508,520); //Sets "this" CheckerFrame's window size.
        //Sets "this" CheckerFrame's position on the screen.
        setLocation(
        			(int)getToolkit().getScreenSize().getWidth()/2-254,
        			(int)getToolkit().getScreenSize().getHeight()/2-310
    				); 
        setResizable(false);
        setVisible(true);
        setTitle("Play Checkers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * If an ActionEvent is performed, it checks the 
     * type of Events and executes the respective code.
     * @param e
     * An ActionEvent that is performed.
     */
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getActionCommand().equalsIgnoreCase("Start Game")) //If the JButton stB is clicked:
        {
        	/*
        	 * This for some reason un-draws the Start Button 
        	 * initially drawn and activates the New Game key?
        	 */
            ((JButton)e.getSource()).setText(null); 
            
            new PlaySound("src/sounds/button.wav").start(); //Plays the sound signifying a new game.
            
            gmP = new Checkers(); //Prepares the JPanel gmP with content.
            gmP.setBounds(0,0,508,401); //Sets coordinates and size.
            
            this.setContentPane(gmP); //Adds the JPanel gmP to "this" CheckerFrame.
        }
    }
}
