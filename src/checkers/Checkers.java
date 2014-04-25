package checkers;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Checkers 
	extends JPanel 
	implements ActionListener, ItemListener, MouseMotionListener, MouseListener 
{

    Graphics g;

    JTextArea messageBar = new JTextArea("To begin, click the 'New Game' button.");
    
    //Buffered images for future use as icons.
    ImageIcon redNormalIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/red_normal.jpg")).getImage());
    ImageIcon yellowNormalIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/yellow_normal.jpg")).getImage());
    ImageIcon redKingIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/red_king.jpg")).getImage());
    ImageIcon yellowKingIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/yellow_king.jpg")).getImage());
    ImageIcon helpIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/help.jpg")).getImage());
    ImageIcon soundButtonIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/sound.jpg")).getImage());
    ImageIcon muteButtonIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/mute.jpg")).getImage());

    //Buttons for setting configurations.
    JButton newGameButton = new JButton("New Game");
    JButton undoButton = new JButton("Undo");
    JButton helpButton = new JButton(helpIcon);
    JButton soundButton = new JButton(soundButtonIcon);

    //Buttons for selecting single-player or two-player modes.
    JLabel modeLabel = new JLabel("Mode");
    ButtonGroup playerButtons = new ButtonGroup();
    JRadioButton p1RadioButton = new JRadioButton("1-Player", true);
    JRadioButton p2RadioButton = new JRadioButton("2-Player", false);

    //Buttons for selecting what color player would like to be in single-player mode.
    JLabel colorLabel = new JLabel("Color");
    ButtonGroup colorButtons = new ButtonGroup();
    JRadioButton redRadioButton = new JRadioButton("Red", false);
    JRadioButton yellowRadioButton = new JRadioButton("Yellow", true);

    //Prepares the help menu (this is set visible once the help button has been clicked).
    Help helpMenu = new Help();
    GameWin winner;

    //ComboBox for selecting the different difficulty levels.
    JLabel difficultyLabel = new JLabel("Difficulty Level");
    JComboBox<String> difficultyLevelComboBox = new JComboBox<String>();
    
    JLabel redPieceIcon = new JLabel();
    JLabel redPieceText = new JLabel("Red Piece");
    
    JLabel yellowPieceIcon = new JLabel();
    JLabel yellowPieceText = new JLabel("Yellow Piece");
    
    JLabel redKingPieceIcon = new JLabel();
    JLabel redKingPieceText = new JLabel("Red King");
    
    JLabel yellowKingPieceIcon = new JLabel();
    JLabel yellowKingPieceText = new JLabel("Yellow King");

    String selectedColor;
    int selectedMode;
    int difficulty;

    static final int REDNORMAL = 1;
	static final int YELLOWNORMAL = 2;
	static final int REDKING = 3;
	static final int YELLOWKING = 4;
	static final int EMPTY = 0;

    int currType;
    boolean movable;

    int[][] board = new int[8][8];

    int [][] preBoard1= new int[8][8];                 //for undo
    int preToMove1;
    int [][] preBoard2= new int[8][8];
    int preToMove2;
    int [][] preBoard3= new int[8][8];
    int preToMove3;

    int startX,startY,endX,endY;
    boolean incomplete=false;
    boolean highlight=false;

    int toMove = REDNORMAL;
	int loser = EMPTY;

    static boolean silent=false;

    int undoCount;

    int won=0;

    Point winPoint;

    /**
     * When Checkers is initialized, it generates the GUI inside of the CheckerFrame.
     */
    Checkers()
    {
        setupGUI();
    }

    /**
     * Initializes the GUI for Checkers within CheckerFrame.
     */
    private void setupGUI()
    {
        setLayout(null);

        //Initializes the newGameButton into memory.---------------------------
        newGameButton.setFocusPainted(false);
        newGameButton.setFont(new Font("SansSerif",Font.BOLD,11));
        newGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newGameButton.addActionListener(this);
        newGameButton.setBounds(405,70,95,25);
        
        //Initializes the undoButton into memory.------------------------------
        undoButton.setFocusPainted(false);
        undoButton.setFont(new Font("SansSerif",Font.BOLD,11));
        undoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        undoButton.addActionListener(this);
        undoButton.setBounds(405,100,95,25);
        
        //Initializes the 'choose color' section into memory.------------------
        //Label
        colorLabel.setFont(new Font("SansSerif",Font.PLAIN,11));
        colorLabel.setBounds(420,350,80,25);
        //Red button
        redRadioButton.setFocusPainted(false);
        redRadioButton.setFont(new Font("SansSerif",Font.PLAIN,11));
        redRadioButton.addActionListener(this);
        redRadioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //Yellow button
        yellowRadioButton.setFocusPainted(false);
        yellowRadioButton.setFont(new Font("SansSerif",Font.PLAIN,11));
        yellowRadioButton.addActionListener(this);
        yellowRadioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //Color buttons group
        colorButtons.add(redRadioButton);
        colorButtons.add(yellowRadioButton);
        //Positioning
        redRadioButton.setBounds(415,380,80,25);
        yellowRadioButton.setBounds(415,408,80,25);
        
        //Initializes the 'chose mode' section into memory.--------------------
        //Label
        modeLabel.setFont(new Font("SansSerif",Font.PLAIN,11));
        modeLabel.setBounds(420,260,80,25);
        //Player 1 button
        p1RadioButton.setFocusPainted(false);
        p1RadioButton.setFont(new Font("SansSerif",Font.PLAIN,11));
        p1RadioButton.addActionListener(this);
        p1RadioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //Player 2 button
        p2RadioButton.setFocusPainted(false);
        p2RadioButton.setFont(new Font("SansSerif",Font.PLAIN,11));
        p2RadioButton.addActionListener(this);
        p2RadioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //Player buttons group
        playerButtons.add(p1RadioButton);
        playerButtons.add(p2RadioButton);
        //Positioning
        p1RadioButton.setBounds(415,290,80,25);
        p2RadioButton.setBounds(415,318,80,25);
        
        //Initializes the help button into memory.-----------------------------
        helpButton.setFocusPainted(false);
        helpButton.setFont(new Font("SansSerif",Font.PLAIN,11));
        helpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        helpButton.addActionListener(this);
        helpButton.setBounds(415,10,25,25);
        
        //Initializes the sound button into memory.----------------------------
        soundButton.setFocusPainted(false);
        soundButton.setFont(new Font("SansSerif",Font.PLAIN,11));
        soundButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        soundButton.addActionListener(this);
        soundButton.setBounds(460,10,25,25);
        
        //Initializes the ComboBox with various difficulty levels.-------------
        //Label
        difficultyLabel.setFont(new Font("SansSerif",Font.PLAIN,11));
        difficultyLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        difficultyLabel.setBounds(415,170,100,25);
        //ComboBox
        difficultyLevelComboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        difficultyLevelComboBox.addItemListener(this);
        difficultyLevelComboBox.addItem("Easy");
        difficultyLevelComboBox.addItem("Medium");
        difficultyLevelComboBox.addItem("Hard");
        difficultyLevelComboBox.addItem("Advanced");
        difficultyLevelComboBox.addItem("Expert");
        difficultyLevelComboBox.setSelectedIndex(2);
        difficultyLevelComboBox.setBounds(415,200,80,25);
        
        //Initializes the message bar into memory.-----------------------------
        messageBar.setFont(new Font("SansSerif",Font.PLAIN,11));
        messageBar.setBounds(0,405,400,20);
        messageBar.setEnabled(false);
        
        //Initializes the 'red piece' image into memory.-----------------------
        //Icon
        redPieceIcon.setBounds(10, 425, 50, 50);
        redPieceIcon.setIcon(redNormalIcon);
        //Text
        redPieceText.setBounds(2, 468, 60, 20);
        
        //Initializes the 'yellow piece' image into memory.--------------------
        //Icon
        yellowPieceIcon.setBounds(122, 425, 50, 50);
        yellowPieceIcon.setIcon(yellowNormalIcon);
        //Text
        yellowPieceText.setBounds(108, 468, 98, 20);
        
        //Initializes the 'red king piece' image into memory.------------------
        //Icon
        redKingPieceIcon.setBounds(234, 425, 50, 50);
        redKingPieceIcon.setIcon(redKingIcon);
        //Text
        redKingPieceText.setBounds(230, 468, 60, 20);
        
        //Initializes the 'yellow king piece' image into memory.---------------
        //Icon
        yellowKingPieceIcon.setBounds(346, 425, 50, 50);
        yellowKingPieceIcon.setIcon(yellowKingIcon);
        //Text
        yellowKingPieceText.setBounds(334, 468, 100, 20);
        
        //Commits the initializes objects to THIS JPanel.----------------------
        this.add(messageBar);
        //Options buttons
        this.add(newGameButton);        
        this.add(undoButton);        
        this.add(helpButton);
        this.add(soundButton);
        //Mode select
        this.add(modeLabel);        
        this.add(p1RadioButton);
        this.add(p2RadioButton);
        //Color select
        this.add(colorLabel);
        this.add(redRadioButton);
        this.add(yellowRadioButton);
        //Difficulty select
        this.add(difficultyLabel);
        this.add(difficultyLevelComboBox);
        //Red piece image
        this.add(redPieceIcon);
        this.add(redPieceText);
        //Yellow piece image
        this.add(yellowPieceIcon);
        this.add(yellowPieceText);
        //Red king image
        this.add(redKingPieceIcon);
        this.add(redKingPieceText);
        //Yellow king image
        this.add(yellowKingPieceIcon);
        this.add(yellowKingPieceText);
        
        //Tells this JPanel to listen to mouse input.--------------------------
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        //g=getGraphics();

    }

    public void paintComponent(Graphics g)	
    {
		super.paintComponent(g);
        g.setColor(new Color(0,0,0));

        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                g.fillRect(100*j,100*i,50,50);
            }
        }
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                g.fillRect(50+100*j,50+100*i,50,50);
            }
        }
        g.drawLine(0,400,400,400);
        g.drawLine(400, 0, 400, 400);
        drawCheckers();
    }
    

    public void actionPerformed(ActionEvent e) 
    {
        if(e.getActionCommand().equalsIgnoreCase("1-Player"))
        {
            new PlaySound("src/sounds/option.wav").start();
            colorLabel.setEnabled(true);
            colorLabel.setVisible(true);
            difficultyLabel.setEnabled(true);
            difficultyLabel.setVisible(true);
            redRadioButton.setEnabled(true);
            redRadioButton.setVisible(true);
            yellowRadioButton.setEnabled(true);
            yellowRadioButton.setVisible(true);
            difficultyLevelComboBox.setEnabled(true);
            difficultyLevelComboBox.setVisible(true);
        }
        if(e.getActionCommand().equalsIgnoreCase("2-Player"))
        {
            new PlaySound("src/sounds/option.wav").start();
            colorLabel.setEnabled(false);
            colorLabel.setVisible(false);
            difficultyLabel.setEnabled(false);
            difficultyLabel.setVisible(false);
            redRadioButton.setEnabled(false);
            redRadioButton.setVisible(false);
            yellowRadioButton.setEnabled(false);
            yellowRadioButton.setVisible(false);
            difficultyLevelComboBox.setEnabled(false);
            difficultyLevelComboBox.setVisible(false);
            yellowRadioButton.setSelected(true);
        }
        if(e.getActionCommand().equalsIgnoreCase("red"))
        {
            new PlaySound("src/sounds/option.wav").start();
        }
        if(e.getActionCommand().equalsIgnoreCase("yellow"))
        {
        	new PlaySound("src/sounds/option.wav").start();
        }
        if(e.getActionCommand().equalsIgnoreCase("New Game"))
        {
        	if (winner != null)
    		{
    			winner.dispose();
    		}
            new PlaySound("src/sounds/button.wav").start();
            newGame();
        }
        if(e.getActionCommand().equalsIgnoreCase("Start New Game"))
        {
        	if (winner != null)
        	{
        		winner.dispose();
        	}
        	new PlaySound("src/sounds/button.wav").start();
            newGame();
        }
        if(e.getActionCommand().equalsIgnoreCase("Undo Previous Game") && undoCount > 3 && difficulty != 4)
        {
        	if (winner != null)
        	{
        		winner.dispose();
        	}
        	won = 0;
        	loser = EMPTY;
        	new PlaySound("src/sounds/button.wav").start();
        	undo();

        }
        if(e.getActionCommand().equalsIgnoreCase("Undo") && undoCount>3)
        {
        	if (winner != null)
        	{
        		winner.dispose();
        	}
        	won = 0;
        	loser = EMPTY;
        	new PlaySound("src/sounds/button.wav").start();
            undo();
        }
        if(e.getSource()==helpButton)
        {
            new PlaySound("src/sounds/button.wav").start();
            helpMenu.setVisible(true);
        }
        if(e.getSource()==soundButton)
        {
            if(silent)
            {
                soundButton.setIcon(soundButtonIcon);
                silent=false;
                new PlaySound("src/sounds/button.wav").start();
            }
            else
            {
                soundButton.setIcon(muteButtonIcon);
                silent=true;
            }
        }
    }
    /**
     * 
     */
    public void newGame()	
    {                            //creates a new game

        //Yellow takes the first move in both modes
        //If someone wants to move secondly, red has to be selected
        //Yellow is always at the bottom of the board

        selectedColor = redRadioButton.isSelected() ? "red" : "yellow";
        selectedMode = p1RadioButton.isSelected() ? 1 : 2;
        difficulty = difficultyLevelComboBox.getSelectedIndex();

        undoButton.setEnabled(false);

        won=0;

        undoCount=0;


        highlight = false;
		incomplete = false;

        loser=EMPTY;

        for (int i=0; i<8; i++)                                  //applies values to the board
		{
			for (int j=0; j<8; j++)
				board[i][j] = EMPTY;

			for (int j=0; j<3; j++)
			    if ( isPossibleSquare(i,j) )
				    board[i][j] =  REDNORMAL;

			for (int j=5; j<8; j++)
			    if ( isPossibleSquare(i,j) )
				    board[i][j] =  YELLOWNORMAL;
		}

        toMove = YELLOWNORMAL;

        for(int i=0;i<8;i++)
        {
            System.arraycopy(board[i],0,preBoard1[i],0,8);                       //for undo
            System.arraycopy(preBoard1[i],0,preBoard2[i],0,8);
            System.arraycopy(preBoard2[i],0,preBoard3[i],0,8);
            preToMove3=preToMove2=preToMove1=toMove;
        }

        if (selectedMode == 1 && selectedColor.equalsIgnoreCase("yellow"))
		{
            this.toMove = REDNORMAL;
            play();
		}
		else if (selectedMode==1 && selectedColor.equalsIgnoreCase("red"))
		{
           this.toMove = REDNORMAL;
            play();
		}

        update(getGraphics());
        drawCheckers();
        showStatus();
    }

    public void drawCheckers()
    {                   //paint checkers on the board
       g=getGraphics();

        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(board[i][j]==REDNORMAL)
                    g.drawImage(redNormalIcon.getImage(),i*50,j*50,this);
                else if(board[i][j]==YELLOWNORMAL)
                    g.drawImage(yellowNormalIcon.getImage(),i*50,j*50,this);
                else if(board[i][j]==REDKING)
                    g.drawImage(redKingIcon.getImage(),i*50,j*50,this);
                else if(board[i][j]==YELLOWKING)
                    g.drawImage(yellowKingIcon.getImage(),i*50,j*50,this);
            }
        }
    }

    public void undo()
    {            //undo function
        undoCount=1;
        for(int i=0;i<8;i++)
        {
            System.arraycopy(preBoard3[i],0,board[i],0,8);              //copies previous board
        }
        toMove=preToMove3;
        drawCheckers();
        update(g);

        if(selectedMode==1)
        {
            play();
        }
    }

    public void play()	
    {

        undoCount++;

        if(undoCount>3)
        {
            if(selectedMode==1 && difficulty!=4)
                undoButton.setEnabled(true);
            else if(selectedMode==2)
                undoButton.setEnabled(true);
        }
        
        for(int i=0;i<8;i++)
        {
            System.arraycopy(preBoard2[i],0,preBoard3[i],0,8);
            System.arraycopy(preBoard1[i],0,preBoard2[i],0,8);
            System.arraycopy(board[i],0,preBoard1[i],0,8);
        }
        preToMove3=preToMove2;
        preToMove2=preToMove1;
        preToMove1=toMove;                                                                                  
		int tempScore;
		int[] result = new int[4];
		int[] counter = new int[1];

		counter[0]=0;
        
		if (this.toMove == YELLOWNORMAL && selectedMode==1 && selectedColor.equalsIgnoreCase("yellow"))
		{
			this.toMove = REDNORMAL;
			showStatus();
			tempScore = GameEngine.MinMax(board,0,difficulty+2,result,this.toMove,counter);

			if (result[0] == 0 && result[1] == 0)
				loser = REDNORMAL;
			else
			{
                CheckerMove.moveComputer(board, result);

                if (loser == EMPTY)
                {
                    new PlaySound("src/sounds/comPlay.wav").start();
                    play();
                }
                this.toMove = YELLOWNORMAL;
			}
		}

		else if (this.toMove == REDNORMAL && selectedMode==1 && selectedColor.equalsIgnoreCase("red"))
		{
			this.toMove = YELLOWNORMAL;
			showStatus();
			tempScore = GameEngine.MinMax(board,0,difficulty+2,result,this.toMove,counter);

			if (result[0] == 0 && result[1] == 0)
				loser = YELLOWNORMAL;
			else
			{
                CheckerMove.moveComputer(board, result);
                if (loser == EMPTY)
                {
                	new PlaySound("src/sounds/comPlay.wav").start();
                    play();
                }

				this.toMove = REDNORMAL;
			}
		}
		else
		{
            if (this.toMove == REDNORMAL)
				this.toMove = YELLOWNORMAL;
			else
				this.toMove = REDNORMAL;
        }
		if (CheckerMove.noMovesLeft(board,this.toMove))  //
		{
			if (this.toMove == REDNORMAL)
				loser = REDNORMAL;
			else
				loser = YELLOWNORMAL;
		}

        showStatus();
	}

    private boolean isPossibleSquare(int i, int j) 
    {
		return (i+j)%2 == 1;
    }

    public void itemStateChanged(ItemEvent e) 
    {         
    	if (e.getStateChange() != difficulty && difficulty != 0)
    	{
    		messageBar.setText("Click 'New Game' to apply settings.");
    	}
    }

    public void mousePressed(MouseEvent e) 
    {

        int x=e.getX();
        int y=e.getY();
        int [] square=new int[2];

        if(x>=0 && x<=500 && y<=500 && y>=0)
            square= CheckerMove.getIndex(x,y);
        
        if (toMove == Checkers.REDNORMAL &&	(board[square[0]][square[1]] == Checkers.REDNORMAL ||
		board[square[0]][square[1]] == Checkers.REDKING)|| toMove == Checkers.YELLOWNORMAL &&
		(board[square[0]][square[1]] == Checkers.YELLOWNORMAL || board[square[0]][square[1]] == Checkers.YELLOWKING))
		{

			// we don't want to lose the incomplete move info:
			// only set new start variables if !incomplete
			if (!incomplete)
			{
				highlight = true;
				startX = square[0];
				startY = square[1];
                update(g);
                g=getGraphics();
                g.setColor(new Color(255,100,30));
                g.fillRect(50*square[0],50*square[1],50,50);                 
                drawCheckers();
                new PlaySound("src/sounds/clickChecker.wav").start();
            }
		}
		else if ( highlight  && (float)(square[0]+square[1]) / 2 != (square[0]+square[1]) / 2)
		{
			endX = square[0];
			endY = square[1];
			int status = CheckerMove.ApplyMove(board,startX,startY,endX,endY);
			switch (status)
			{
			case CheckerMove.LEGALMOVE:
				incomplete = false;
				highlight = false;
				play();
                update(g);
                drawCheckers();
                break;
			case CheckerMove.incompleteMove:
				incomplete = true;
				highlight = true;
				// the ending square is now starting square for the next capture
				startX = square[0];
				startY = square[1];
                update(g);
                g=getGraphics();
                g.setColor(new Color(255,100,30));
                g.fillRect(50*square[0],50*square[1],50,50);
                drawCheckers();
                break;
			}
        }
	}

    public void mouseReleased(MouseEvent e) 
    {
    }

    public void mouseDragged(MouseEvent e) 
    {
    }

    public void mouseMoved(MouseEvent e) 
    {
    }

    public void mouseClicked(MouseEvent e) 
    {
    }

    public void mouseEntered(MouseEvent e) 
    {
    }

    public void mouseExited(MouseEvent e) 
    {
    }

    private void showStatus() 
    {       //prints msgs to the statuss bar
        if (this.toMove == REDNORMAL)
        {
            messageBar.setText("Red to move");
        }
        else
        {
            messageBar.setText("Yellow to move");
        }

        if (loser == REDNORMAL && won==0)
        {
            messageBar.setText("Yellow Wins!");
            try 
            {
                Thread.sleep(150*3);
            } 
            catch(InterruptedException e) 
            {
                e.printStackTrace();
            }
            winner = new GameWin("Red",this.getLocationOnScreen());
            
            JButton newGameWinButton = new JButton("New Game");
            newGameWinButton.setActionCommand("Start New Game");
            newGameWinButton.setFocusPainted(false);
            newGameWinButton.setFont(new Font("SansSerif",Font.BOLD,11));
            newGameWinButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            newGameWinButton.addActionListener(this);
            newGameWinButton.setBounds(2, 80, 92, 30);
            
            JButton undoPreviousGameButton = new JButton("Undo");
            undoPreviousGameButton.setActionCommand("Undo Previous Game");
            undoPreviousGameButton.setFocusPainted(false);
            undoPreviousGameButton.setFont(new Font("SansSerif",Font.BOLD,11));
            undoPreviousGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            undoPreviousGameButton.addActionListener(this);
            undoPreviousGameButton.setBounds(100, 80, 92, 30);
            
            winner.add(newGameWinButton);
            winner.add(undoPreviousGameButton);
            won=1;
            //undoCount=0;
            //newGame();
        }
        else if (loser == YELLOWNORMAL && won==0)
        {
            messageBar.setText("Red Wins!");
            try 
            {
                Thread.sleep(150*3);
            } 
            catch(InterruptedException e) 
            {
                e.printStackTrace();
            }            
            winner = new GameWin("Red",this.getLocationOnScreen());
            JButton newGameWinButton = new JButton("New Game");
            newGameWinButton.setActionCommand("Start New Game");
            newGameWinButton.setFocusPainted(false);
            newGameWinButton.setFont(new Font("SansSerif",Font.BOLD,11));
            newGameWinButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            newGameWinButton.addActionListener(this);
            newGameWinButton.setBounds(2, 80, 92, 30);
            
            JButton undoPreviousGameButton = new JButton("Undo");
            undoPreviousGameButton.setActionCommand("Undo Previous Game");
            undoPreviousGameButton.setFocusPainted(false);
            undoPreviousGameButton.setFont(new Font("SansSerif",Font.BOLD,11));
            undoPreviousGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            undoPreviousGameButton.addActionListener(this);
            undoPreviousGameButton.setBounds(100, 80, 92, 30);
            
            
            winner.add(newGameWinButton);
            winner.add(undoPreviousGameButton);
            won=1;
            //undoCount=0;
            //newGame();            
        }
    }
   // The AWT invokes the update() method in response to the repaint() method
   // calls that are made as a checker is dragged. The default implementation
   // of this method, which is inherited from the Container class, clears the
   // applet's drawing area to the background color prior to calling paint().
   // This clearing followed by drawing causes flicker. CheckerDrag overrides
   // update() to prevent the background from being cleared, which eliminates
   // the flicker.
    @Override
    public void update(Graphics g)
    {                                                                                                     
        paint(g);
    }
}