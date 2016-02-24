import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * The main driver of the Connect4Game, as well as the visualization of it.
 */
public class Connect4Frame extends JFrame {
    Connect4Panel myPanel;  // the panel storing the visual of the game itself
    Connect4Game myGame;    // the game itself
    Agent redPlayer, bluePlayer;       // the two players playing the game, blue is AI
    boolean redPlayerturn, gameActive; // booleans controlling whose turn it is and whether a game is ongoing
    JButton newGameButton, nextMoveButton, playToEndButton; // the buttons controlling the game

    JLabel updateLabel; // the status label describing the events of the game
    Random r;           // a random number generator to randomly decide who plays first

    private static int x, y; // for mouse location x, y

    /**
     * Creates a new Connect4Frame with a given game and pair of players.
     * @param game the game itself.
     * @param redPlayer the agent playing as the red tokens.
     * @param bluePlayer the agent playing as the blue tokens.
     */
    public Connect4Frame(Connect4Game game, Agent redPlayer, Agent bluePlayer) {
        super();
        this.myGame = game;           // stores the game itself
        this.redPlayer = redPlayer;   // stores the red player
        this.bluePlayer = bluePlayer; //stores the blue player, AI
        gameActive = false;           // initially sets that no game is active
        r = new Random();             // creates the random number generator
        myPanel = new Connect4Panel(game); // creates the panel for displaying the game

        // DOUBLE check if I can make this mouseListener part smarter and slightly easier !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        setMouseListener(this);
        Container contentPanel = this.getContentPane();
        Component[] comps = contentPanel.getComponents();
        for (Component c : comps) {
            setMouseListener(c);
            if (c instanceof JPanel) {
                Component[] compsInPanel = ((JPanel) c).getComponents();
                for (Component cc : compsInPanel) {
                    setMouseListener(cc);
                }
            }
        }

        newGameButton = new JButton("Start a New Game");         // creates the button for starting a new game
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT); // center-aligns the new game button
        newGameButton.addActionListener(new ActionListener() {   // connects the new game button to its buttonPressed method
                public void actionPerformed(ActionEvent e) {
                    newGameButtonPressed();
                }
            });
        nextMoveButton = new JButton("Play Next Move"); // creates the button for playing the next move
        nextMoveButton.setEnabled(false);   // disables the button until a game is started
        nextMoveButton.setAlignmentX(Component.CENTER_ALIGNMENT);   // centers the button
        nextMoveButton.addActionListener(new ActionListener() { // connects the play next move button to its buttonPressed method
                public void actionPerformed(ActionEvent e) {                
                    nextMoveButtonPressed();
                }
            });
        playToEndButton = new JButton("Play to End"); // creates the button for finishing the game
        playToEndButton.setEnabled(false);            // disables the button until a game is started
        playToEndButton.setAlignmentX(Component.CENTER_ALIGNMENT); // centers the button
        playToEndButton.addActionListener(new ActionListener() {   //connects the play to end button to its buttonPressed method
                public void actionPerformed(ActionEvent e) {
                    playToEndButtonPressed();
                }
            });

        updateLabel = new JLabel(redPlayer.toString() + " vs. " + bluePlayer.toString()); // creates the status label
        updateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);                            // centers the status label
        JPanel buttonPane = new JPanel();                                  // creates a pane for the buttons
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS)); // sets the button pane to be horizontally oriented
        // adding and spacing out the buttons
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(newGameButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10,0)));
        buttonPane.add(nextMoveButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10,0)));
        buttonPane.add(playToEndButton);
        buttonPane.add(Box.createHorizontalGlue());

        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS)); // sets the overall pane to be vertically oriented
        this.add(updateLabel); // adds the update label
        this.add(myPanel);     // adds the visual of the game board
        this.add(buttonPane);  // adds the pane containing the buttons
        this.pack();           // shrinks the window to the appropriate size
        this.setResizable(false); // makes the window not resizable
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close the application when the window is closed
        this.setVisible(true);    // show the window

    }

    public void setMouseListener(Component c) {
        c.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    x = e.getX();
                    y = e.getY();
                    //System.out.println("x: " + x);
                    //System.out.println("y: " + y);

                    //while (myGame.getRowCount(col) == 6) {} // should be a necessary checking step for full columns (but so far leave it here)

                    // make one nextMove for redPlayerturn
                    Connect4Game oldBoard = new Connect4Game(myGame);   // store the old board for validation
                    if(redPlayerturn) { // if it's the RED player's turn, run their move 
                        redPlayer.move(x, y);
                        alert(bluePlayer.toString() + " plays next...");
                    } 
                    String validateResult = oldBoard.validate(myGame); // check & make sure it is a valid next move for the board
                    if (validateResult.length() > 0) { // if there was a validation error, show it and cancel the game, instead of cancel, I should do more
                        alert(validateResult);// show the error
                        disableButtons();     // stop the game
                        gameActive = false;
                    }
                    redPlayerturn = !redPlayerturn; // switch whose turn it is
                    validateResult();
                }
            });
    }
    
    public void alert(String text) {
        updateLabel.setText(text);
        this.repaint();
    }

    private void nextMove() {
        Connect4Game oldBoard = new Connect4Game(myGame);   // store the old board for validation
        if(redPlayerturn) { // if it's the RED player's turn, run their move 
            redPlayer.move();
            alert(bluePlayer.toString() + " plays next...");
        } else {            // if it's the BLUE player's turn, run their move 
            bluePlayer.move();
            alert(redPlayer.toString() + " plays next...");
        }
        String validateResult = oldBoard.validate(myGame); // check & make sure it is a valid next move for the board
        if(validateResult.length() > 0) { // if there was a validation error, show it and cancel the game 
            alert(validateResult);// show the error
            disableButtons();     // stop the game
            gameActive = false;
        }
        redPlayerturn = !redPlayerturn; // switch whose turn it is

        validateResult();
        this.repaint();
    }

    private void validateResult() {
        char won = myGame.gameWon();    // check if the game has been won
        if (won != 'N') {     // if the game has been won... 
            disableButtons(); // disable the buttons
            gameActive = false;
            if (myGame.gameWon() == 'R') {        // if RED won, say so 
                alert(redPlayer.toString() + " wins!");
            } else if (myGame.gameWon() == 'B') { // if BLUE won, say so for AI
                alert(bluePlayer.toString() + " wins! AI is smart~!");
            }
        } else if (myGame.boardFull()) { // if the board is full... 
            disableButtons();   // disable the buttons
            alert("The game ended in a draw!"); // announce the draw
            gameActive = false;
        }
    }
    
    private void newGame() {
        myGame.clearBoard();
        enableButtons();
        gameActive = true;
        redPlayerturn = r.nextBoolean();
        if (redPlayerturn) {
            alert(redPlayer.toString() + " plays first!");
            myGame.setRedPlayedFirst(true);
        } else {
            alert(bluePlayer.toString() + " plays first!");
            myGame.setRedPlayedFirst(false);
        }
        this.repaint();
    }

    private void playToEnd() {
        while (gameActive) { // keep playing the next move until the game ends {
            nextMove();
        }
        char won = myGame.gameWon();
        if (won != 'N') {    // when it ends, announce how it ended: win or draw 
            disableButtons();
            if (myGame.gameWon() == 'R') {
                alert(redPlayer.toString() + " wins!");
            } else if (myGame.gameWon() == 'B') {
                alert(bluePlayer.toString() + " wins!");
            }
        } else if (myGame.boardFull()) {
            disableButtons();
            alert("The game ended in a draw!");
        } else { // if it didn't end in a win or draw, leave the error message u 
            disableButtons();
        }
    }

    public void newGameButtonPressed() {
        newGame();
    }

    public void nextMoveButtonPressed() {
        nextMove();
    }

    public void playToEndButtonPressed() {
        playToEnd();
    }

    private void disableButtons() {
        nextMoveButton.setEnabled(false);
        playToEndButton.setEnabled(false);
    }

    private void enableButtons() {
        nextMoveButton.setEnabled(true);
        playToEndButton.setEnabled(true);
    }
}
