package checkers;

public class IntelliChecker 
{
	//This is where the Checkers Game is executed.
	/**
	 * Main method that executes the IntelliChecker program.
	 * @param args
	 * Takes in arguments from the Console.
	 * @throws Exception
	 * Not sure why, but this throwing of Exception prevents 
	 * drawing issues after a game is finished.
	 */
    public static void main(String args[]) throws Exception
    {
    	/*
    	 * This creates an event that can only be 
    	 * run when the run() method is called?
    	 */
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                new CheckerFrame(); //Initializes the Checkers JFrame and the mechanics within it.
        	}
        }
        );
    }
}
