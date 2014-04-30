package checkers;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

@SuppressWarnings("serial")
public class CheckerFrame extends JFrame implements ActionListener
{
    //Creates a panel that is initially populated with StartPanel, but later populated with Checkers.
    JPanel gamePanel = new StartPanel(); 
    //Start button located on the bottom center of "this" CheckerFrame.
    JButton startButton = new JButton("Start Game"); 
  
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
           //no need to handle exception as it only affects the appearance.
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
        this.setLayout(null);
        
        //Initializes this CheckerFrame into memory.---------------------------
        this.setIconImage(new ImageIcon(getClass().getResource("/images/icon.jpg")).getImage()); 
        
        this.setResizable(false);
        this.setTitle("Play Checkers");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(508,520);
        this.setLocation(
        			(int)getToolkit().getScreenSize().getWidth()/2-254,
        			(int)getToolkit().getScreenSize().getHeight()/2-310
    				); 
        this.setVisible(true);
        
        gamePanel.setBounds(0,0,508,520);//400,401
        
        //Initializes the start button into memory.----------------------------
        startButton.setHorizontalAlignment(SwingConstants.LEADING); 
        startButton.setIcon(new ImageIcon(getClass().getResource("/images/checkersIcon.jpg")));
        startButton.setBackground(Color.LIGHT_GRAY);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        startButton.setBounds(154, 415, 236, 60);
        startButton.setFont(new Font("Times new roman",Font.BOLD,20));
        startButton.addActionListener(this);
        startButton.setFocusPainted(false);
        
        gamePanel.add(startButton);
        
        this.add(gamePanel);
    }

    /**
     * If an ActionEvent is performed, it checks the 
     * type of Events and executes the respective code.
     * @param e
     * An ActionEvent that is performed.
     */
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getActionCommand().equalsIgnoreCase("Start Game")) //If the startButton is clicked:
        {

            new PlaySound("src/sounds/button.wav").start(); //Plays the sound signifying a new game.

            gamePanel = new Checkers(); //Prepares the JPanel gamePanel with content.
            setupGUI();
            
            
            gamePanel.remove(startButton);
            this.setContentPane(gamePanel); //Adds the JPanel gmP to "this" CheckerFrame.
            
        }
    }
}
