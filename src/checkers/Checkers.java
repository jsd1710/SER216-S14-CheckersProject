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

        newGameButton.setFocusPainted(false);
        undoButton.setFocusPainted(false);
        redRadioButton.setFocusPainted(false);
        yellowRadioButton.setFocusPainted(false);
        p1RadioButton.setFocusPainted(false);
        p2RadioButton.setFocusPainted(false);
        helpButton.setFocusPainted(false);
        soundButton.setFocusPainted(false);

        difficultyLabel.setFont(new Font("SansSerif",Font.PLAIN,11));
        colorLabel.setFont(new Font("SansSerif",Font.PLAIN,11));
        modeLabel.setFont(new Font("SansSerif",Font.PLAIN,11));
        redRadioButton.setFont(new Font("SansSerif",Font.PLAIN,11));
        yellowRadioButton.setFont(new Font("SansSerif",Font.PLAIN,11));
        p1RadioButton.setFont(new Font("SansSerif",Font.PLAIN,11));
        p2RadioButton.setFont(new Font("SansSerif",Font.PLAIN,11));
        newGameButton.setFont(new Font("SansSerif",Font.BOLD,11));
        undoButton.setFont(new Font("SansSerif",Font.BOLD,11));
        helpButton.setFont(new Font("SansSerif",Font.PLAIN,11));
        soundButton.setFont(new Font("SansSerif",Font.PLAIN,11));
        messageBar.setFont(new Font("SansSerif",Font.PLAIN,11)); 

        newGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        undoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        helpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        soundButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newGameButton.addActionListener(this);
        undoButton.addActionListener(this);
        helpButton.addActionListener(this);
        soundButton.addActionListener(this);
        newGameButton.setBounds(405,70,95,25);//297
        this.add(newGameButton);
        undoButton.setBounds(405,100,95,25);
        this.add(undoButton);
        helpButton.setBounds(415,10,25,25);
        this.add(helpButton);
        soundButton.setBounds(460,10,25,25);
        this.add(soundButton);

        modeLabel.setBounds(420,260,80,25);
        this.add(modeLabel);
        p1RadioButton.addActionListener(this);
        p2RadioButton.addActionListener(this);
        p1RadioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        p2RadioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        playerButtons.add(p1RadioButton);
        playerButtons.add(p2RadioButton);
        p1RadioButton.setBounds(415,290,80,25);
        p2RadioButton.setBounds(415,318,80,25);
        this.add(p1RadioButton);
        this.add(p2RadioButton);

        colorLabel.setBounds(420,350,80,25);
        this.add(colorLabel);
        redRadioButton.addActionListener(this);
        yellowRadioButton.addActionListener(this);
        redRadioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        yellowRadioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        colorButtons.add(redRadioButton);
        colorButtons.add(yellowRadioButton);
        redRadioButton.setBounds(415,380,80,25);
        yellowRadioButton.setBounds(415,408,80,25);
        this.add(redRadioButton);
        this.add(yellowRadioButton);

        //Initializes the ComboBox with various difficulty levels.
        difficultyLevelComboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        difficultyLevelComboBox.addItemListener(this);
        difficultyLevelComboBox.addItem("Easy");
        difficultyLevelComboBox.addItem("Medium");
        difficultyLevelComboBox.addItem("Hard");
        difficultyLevelComboBox.addItem("Advanced");
        difficultyLevelComboBox.addItem("Expert");
        difficultyLevelComboBox.setSelectedIndex(2);
        difficultyLevelComboBox.setBounds(415,200,80,25);
        this.add(difficultyLevelComboBox);

        difficultyLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        difficultyLabel.setBounds(415,170,100,25);
        this.add(difficultyLabel);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        messageBar.setBounds(0,405,400,20);
        messageBar.setEnabled(false);
        this.add(messageBar);

        redPieceIcon.setBounds(10, 425, 50, 50);
        redPieceIcon.setIcon(redNormalIcon);
        this.add(redPieceIcon);
        redPieceText.setBounds(2, 468, 60, 20);
        this.add(redPieceText);

        yellowPieceIcon.setBounds(122, 425, 50, 50);
        yellowPieceIcon.setIcon(yellowNormalIcon);
        this.add(yellowPieceIcon);
        yellowPieceText.setBounds(108, 468, 98, 20);
        this.add(yellowPieceText);

        redKingPieceIcon.setBounds(234, 425, 50, 50);
        redKingPieceIcon.setIcon(redKingIcon);
        this.add(redKingPieceIcon);
        redKingPieceText.setBounds(230, 468, 60, 20);
        this.add(redKingPieceText);

        yellowKingPieceIcon.setBounds(346, 425, 50, 50);
        yellowKingPieceIcon.setIcon(yellowKingIcon);
        this.add(yellowKingPieceIcon);
        yellowKingPieceText.setBounds(334, 468, 100, 20);
        this.add(yellowKingPieceText);
        
        //g=getGraphics();
        //g.drawImage(redN.getImage(),30,450,this);

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
            new PlaySound("src/sounds/button.wav").start();
            newGame();
        }
        if(e.getActionCommand().equalsIgnoreCase("Undo") && undoCount>3)
        {
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
                Thread.sleep(150);
            } 
            catch(InterruptedException e) 
            {
                e.printStackTrace();
            }
            new GameWin("Yellow",this.getLocationOnScreen());
            won=1;
            undoCount=0;
            newGame();
        }
        else if (loser == YELLOWNORMAL && won==0)
        {
            messageBar.setText("Red Wins!");
            try 
            {
                Thread.sleep(150);
            } 
            catch(InterruptedException e) 
            {
                e.printStackTrace();
            }            
            new GameWin("Red",this.getLocationOnScreen());
            won=1;
            undoCount=0;
            newGame();            
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