package Default;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JPanel implements MouseListener{
	/**There was an error"added this field to fix an
	 error "The serializable class GameMain does not declare a static final serialVersionUID field of type long"
	 * so I added the field bellow to fix it
	 */
	private static final long serialVersionUID = 1L; 
	/* 
	*/
	//Constants for game 
	// number of ROWS by COLS cell constants 
	public static final int ROWS = 3;     
	public static final int COLS = 3;  
	public static final String TITLE = "Tic Tac Toe";

	//constants for dimensions used for drawing
	//cell width and height
	public static final int CELL_SIZE = 100;
	//drawing canvas
	public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
	public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
	//Noughts and Crosses are displayed inside a cell, with padding from border
	public static final int CELL_PADDING = CELL_SIZE / 6;    
	public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;    
	public static final int SYMBOL_STROKE_WIDTH = 8;
	
	/*declare game object variables*/
	// the game board 
	private Board board;
	private GameState currentState; 
	private enum GameState {
	    Playing, Draw, Cross_won, Nought_won // enumeration created for the variable GameState
	}

	// the current player
	private Player currentPlayer; 
	// for displaying game status message
	private JLabel statusBar;       
	

	/** Constructor to setup the UI and game components on the panel */
	public GameMain() {        
		addMouseListener(this); //added mouse listener
	    
		// Setup the status bar (JLabel) to display status message       
		statusBar = new JLabel("         ");       
		statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));       
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));       
		statusBar.setOpaque(true);       
		statusBar.setBackground(Color.LIGHT_GRAY);  
		
		//layout of the panel is in border layout
		setLayout(new BorderLayout());       
		add(statusBar, BorderLayout.SOUTH);
		// account for statusBar height in overall height
		setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT + 30));
		
		board = new Board(); // Create a new instance of the Board class
		initGame(); // Initialise the game board

		
		//TODO: call the method to initialise the game board
		if (currentPlayer == Player.Cross) {
		    statusBar.setText("X's Turn");
		} else {
		    statusBar.setText("O's Turn");
		}

	}
	
	public static void main(String[] args) {
		    // Run GUI code in Event Dispatch thread for thread safety.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
				//create a main window to contain the panel
				JFrame frame = new JFrame(TITLE);
			 
				 // Create a new GameMain panel and add it to the frame
	            GameMain gameMainPanel = new GameMain();
	            frame.getContentPane().add(gameMainPanel);		
			 
	            // Set the default close operation of the frame to exit_on_close
	            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
				
				frame.pack();             
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
	         }
		 });
	}
	/** Custom painting codes on this JPanel */
	public void paintComponent(Graphics g) {
	    // Fill background and set colour to white
	    super.paintComponent(g);
	    setBackground(Color.WHITE);
	    
	    // Ask the game board to paint itself
	    board.paint(g);
	    
	    // Set status bar message based on current game state
	    if (currentState == GameState.Playing) {          
	        // Set text color to black
	        statusBar.setForeground(Color.BLACK);          
	        
	        // Check current player's turn and update status bar message accordingly
	        if (currentPlayer == Player.Cross) {
	            statusBar.setText("X's Turn");
	        } else {
	            statusBar.setText("O's Turn");
	        }  
	    } else if (currentState == GameState.Draw) {          
	        // Set text color to red
	        statusBar.setForeground(Color.RED);          
	        // Set draw message
	        statusBar.setText("It's a Draw! Click to play again.");       
	    } else if (currentState == GameState.Cross_won) {          
	        // Set text color to red
	        statusBar.setForeground(Color.RED);          
	        // Set X won message
	        statusBar.setText("'X' Won! Click to play again.");       
	    } else if (currentState == GameState.Nought_won) {          
	        // Set text color to red
	        statusBar.setForeground(Color.RED);          
	        // Set O won message
	        statusBar.setText("'O' Won! Click to play again.");       
	    }
	}

		
	
	  /** Initialise the game-board contents and the current status of GameState and Player) */
	public void initGame() {
		for (int row = 0; row < ROWS; ++row) {          
			for (int col = 0; col < COLS; ++col) {  
				// all cells empty
				board.cells[row][col].content = Player.Empty;           
			}
		}
		currentState = GameState.Playing;
		currentPlayer = Player.Cross;//X's starts the game
		}
		
		
		/**After each turn check to see if the current player hasWon by putting their symbol in that position, 
		 * If they have the GameState is set to won for that player
		 * If no winner then isDraw is called to see if deadlock, if not GameState stays as PLAYING
		 *   
		 */
	public void updateGame(Player thePlayer, int row, int col) {
			//check for win after play
			if(board.hasWon(thePlayer, row, col)) {
				
				// TODO: check which player has won and update the currentstate to the appropriate gamestate for the winner
				if (thePlayer == Player.Cross) {
		            currentState = GameState.Cross_won;
		        } else {
		            currentState = GameState.Nought_won;
		        }
				
			} else if (board.isDraw ()) {
			// Set the current state to the draw game state
		        currentState = GameState.Draw;
			}
			//otherwise no change to current state of playing
			 
		}
		/** Event handler for the mouse click on the JPanel. If selected cell is valid and Empty then current player is added to cell content.
		 *  UpdateGame is called which will call the methods to check for winner or Draw. if none then GameState remains playing.
		 *  If win or Draw then call is made to method that resets the game board.  Finally a call is made to refresh the canvas so that new symbol appears*/
	
	public void mouseClicked(MouseEvent e) {  
	    // get the coordinates of where the click event happened            
		int mouseX = e.getX();             
		int mouseY = e.getY();             
		// Get the row and column clicked             
		int rowSelected = mouseY / CELL_SIZE;
		int colSelected = mouseX / CELL_SIZE; 
		if (currentState == GameState.Playing) {                
			if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS && board.cells[rowSelected][colSelected].content == Player.Empty) {
				// move  
				board.cells[rowSelected][colSelected].content = currentPlayer; 
				// update currentState                  
				updateGame(currentPlayer, rowSelected, colSelected); 
				// Switch player
				currentPlayer = (currentPlayer == Player.Cross) ? Player.Nought : Player.Cross; // Switching player after valid move
				}
			} else {        
			// game over and restart              
			initGame();            
		}            
			repaint();//repainted graphics.
	}
		
	
	@Override
	public void mousePressed(MouseEvent e) {
		//  Auto-generated, event not used
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		//  Auto-generated, event not used
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// Auto-generated,event not used
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// Auto-generated, event not used
		
	}

}

