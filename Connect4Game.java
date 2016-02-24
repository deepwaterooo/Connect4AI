/**
 * The data structure for a game of Connect 4.
 * 
 * Connect4Game is made of a certain number of Connect4Columns. Each column represents
 * a column from the current state of the game.
 * 
 * You should not modify this class, but you will need to use methods within it.
 */

public class Connect4Game {
    private Connect4Column[] columns;
    private boolean redPlayedFirst;
    
    public Connect4Game(int numCols, int numRows) {
        columns = new Connect4Column[numCols];
        for(int i = 0; i < numCols; i++) {
            columns[i] = new Connect4Column(numRows);
        }
    }

    public Connect4Game(Connect4Game game) {
        columns = new Connect4Column[game.getColumnCount()];
        for (int i = 0; i < game.getColumnCount(); i++) {
            columns[i] = new Connect4Column(game.getColumn(i));
        }
    }

    public Connect4Column getColumn(int i) {
        if (i < columns.length && i >= 0) {
            return columns[i];
        } else {        
            return null;
        }
    }
    
    /**
     * Returns the index of the top empty slot in a particular column, or -1 if the column is already full.
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++) {
            if (!column.getSlot(i).getIsFilled()) {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }
    
    /** 
     * Another simplified getLowestEmptyIndex.
     * Returns the index of the top empty slot in a particular column, or -1 if the column is already full.
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column
     */
    public int getLowestEmptyIndex(int columnIndex) {
        Connect4Column column = getColumn(columnIndex);
        return column.getLowestEmptyIndex(column);
    }

    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     * @param columnNumber The column into which to drop the token.
     */ 
    public void moveOnColumn(int columnNumber, boolean iAmRed) {
        // Find the top empty slot in the column
        Connect4Column column = getColumn(columnNumber);
        int lowestEmptySlotIndex = column.getLowestEmptyIndex(column);  
        // If the column is full, lowestEmptySlot will be -1
        if (lowestEmptySlotIndex > -1) {
            Connect4Slot lowestEmptySlot = getColumn(columnNumber).getSlot(lowestEmptySlotIndex);  
            if (iAmRed) {
                lowestEmptySlot.addRed(); 
            } else {
                lowestEmptySlot.addBlue();
            }
        }
    } 

    public int getColumnCount() {
        return columns.length;
    }

    public int getRowCount() {
        return columns[0].getRowCount();
    }

    public int getRowCount(int col) {
        return columns[col].getRowCount();
    }
    
    public void clearBoard() {
        for (int i = 0; i < getColumnCount(); i++) {
            for (int j = 0; j < getRowCount(); j++) {
                getColumn(i).getSlot(j).clear();
            }
        }
    }

    public char[][] getBoardMatrix() {
        char[][] board = new char[getRowCount()][getColumnCount()];
        for (int i = 0; i < getColumnCount(); i++) {
            for(int j = 0; j < getRowCount(); j++)  {
                if (getColumn(i).getSlot(j).getIsFilled()) {
                    if (getColumn(i).getSlot(j).getIsRed()) {
                        board[j][i] = 'R';
                    } else {
                        board[j][i] = 'B'; // Y => B
                    }
                } else {
                    board[j][i] = 'Y'; // B => Y
                }
            }
        }
        return board;
    }

    public boolean boardFull() {
        char[][] board = getBoardMatrix();
        for (int i = 0; i < getColumnCount(); i++) {
            for (int j = 0; j < getRowCount(); j++) {
                if (board[j][i] == 'Y') {
                    return false;
                }
            }
        }
        return true;
    }

    public char gameWon() {
        char[][] board = getBoardMatrix();
        for (int i = 0; i < getColumnCount(); i++) {
            for (int j = 0; j < getRowCount(); j++) {
                if(board[j][i] != 'Y') { 
                    if (j + 3 < getRowCount()) { // 4 in a Column
                        if(board[j][i] == board[j + 1][i] && board[j][i] == board[j + 2][i] && board[j][i] == board[j + 3][i]) {
                            highlightSlot(j, i);
                            highlightSlot(j + 1, i);
                            highlightSlot(j + 2, i);
                            highlightSlot(j + 3, i);
                            return board[j][i];
                        }
                    }
                    if (i + 3 < getColumnCount()) { // 4 in a Row
                        if (board[j][i] == board[j][i + 1] && board[j][i] == board[j][i + 2] && board[j][i] == board[j][i + 3]) {
                            highlightSlot(j, i);
                            highlightSlot(j, i + 1);
                            highlightSlot(j, i + 2);
                            highlightSlot(j, i + 3);
                            return board[j][i];
                        }
                    }
                    if (i + 3 < getColumnCount() && j + 3 < getRowCount()) { // 4 in '/' diagonal
                        if(board[j][i] == board[j + 1][i + 1] && board[j][i] == board[j + 2][i + 2] && board[j][i] == board[j + 3][i + 3]) {
                            highlightSlot(j, i);
                            highlightSlot(j + 1, i + 1);
                            highlightSlot(j + 2, i + 2);
                            highlightSlot(j + 3,i + 3);
                            return board[j][i];
                        }
                    }
                    if (i > 2 && j + 3 < getRowCount()) { // 4 in '\' diagonal
                        if (board[j][i] == board[j + 1][i - 1] && board[j][i] == board[j + 2][i - 2] && board[j][i] == board[j + 3][i - 3]) {
                            highlightSlot(j, i);
                            highlightSlot(j + 1, i - 1);
                            highlightSlot(j + 2, i - 2);
                            highlightSlot(j + 3, i - 3);
                            return board[j][i];
                        }
                    }
                }
            }
        }
        return 'N';
    }

    /**
     * Validate that the given board is a valid next state following this board.
     * Your agent will not need to use this method.
     * @param after the next board state.
     */
    public String validate(Connect4Game after) {
        int filledSlots = 0, redSlots = 0, blueSlots = 0;
        for (int i = 0; i < getColumnCount(); i++) {
            for (int j = 0; j < getRowCount(); j++) {
                if (after.getColumn(i).getSlot(j).getIsFilled()) {
                    if (after.getColumn(i).getSlot(j).getIsRed()) {
                        redSlots++;
                    } else {
                        blueSlots++;
                    }
                }
                if (!getColumn(i).getSlot(j).getIsFilled() && after.getColumn(i).getSlot(j).getIsFilled()) {
                    filledSlots++;
                }
                if (getColumn(i).getSlot(j).getIsFilled() && !after.getColumn(i).getSlot(j).getIsFilled()) {
                    return "Invalid move: a token was removed at column " + i + ", row " + j + ".";
                }
                if (getColumn(i).getSlot(j).getIsFilled() && (getColumn(i).getSlot(j).getIsRed() != after.getColumn(i).getSlot(j).getIsRed())) {
                    return "Invalid move: a token was changed at column " + i + ", row " + j + ".";
                }
                if (j < getRowCount() - 1 && after.getColumn(i).getSlot(j).getIsFilled() && !after.getColumn(i).getSlot(j+1).getIsFilled()) {
                    return "Invalid move: a token in column " + i + " was not placed in the highest open slot.";
                }
            }
        }
        if (filledSlots > 1) {
            return "Invalid move: more than one token was placed.";
        }
        if (filledSlots == 0) {
            return "Invalid move: no move was made.";
        }
        if (after.getRedPlayedFirst()) {
            if (redSlots < blueSlots) {
                return "Invalid move: blue moved during red's turn.";
            } else if (redSlots > blueSlots + 1) {
                return "Invalid move: red moved during blue's turn.";
            }
        } else {
            if (blueSlots < redSlots) {
                return "Invalid move: red moved during blue's turn.";
            } else if (blueSlots > redSlots + 1) {
                return "Invalid move: blue moved during red's turn.";
            }
        }
        return "";
    }

    public void highlightSlot(int row, int column) {
        getColumn(column).getSlot(row).highlight();
    }
    
    public boolean getRedPlayedFirst() {
        return redPlayedFirst;
    }
    
    public void setRedPlayedFirst(boolean redPlayedFirst) {
        this.redPlayedFirst = redPlayedFirst;
    }
}
