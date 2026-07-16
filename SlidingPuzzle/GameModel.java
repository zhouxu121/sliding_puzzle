
/**
 * Represents the game logic of a sliding puzzle.
 * 
 * This class does not contain any Swing or GUI code.
 */
public class GameModel
{
    // Interval value used to represent the empty field.
    private static final int EMPTY = -1;

    // Number of rows and colums of the square board.
    // Valid values are 3, 4, 5.
    private int size;

    // Stores the values of all puzzle field.
    private int[][] board;

    // current row position of the empty field.
    private int emptyRow;

    // current colum position of the empty field.
    private int emptyCol;

    /**
     * Create a new Sliding puzzle with the given size.
     * 
     * The board is first initialized in the solved state
     * and then shuffled using valid puzzle moves.
     * 
     * @param size the number of rows and colums
     * @throws IllegalArgumentException if the size is not 3, 4 or 5
     */

    public GameModel(int size) {
        if(size < 3 || size > 5) {
            throw new IllegalArgumentException("Size must be 3, 4 or 5.");
        }

        this.size = size;
        board = new int[size][size];

        initializeSolvedBoard();
        shuffle();
    }

    
    private void initializeSolvedBoard() {
        int number = 0;

        for (int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                board[row][col] = number;
                number++;
            }
        }
        
        emptyRow = size - 1;
        emptyCol = size - 1;
        board[emptyRow][emptyCol] = EMPTY;
    }
    
    private void shuffle() {
        
    }
    
    public boolean move(int row, int col) {
        if(!canMove(row, col)) {
            return false;
        }
        
        board[emptyRow][emptyCol] = board[row][col];
        board[row][col] = EMPTY;
        
        emptyRow = row;
        emptyCol = col;
        
        return true;
    }
    
    public boolean canMove(int row, int col) {
        if(!isInsideBorad(row, col)) {
            return false;
        }
        
        if(board[row][col] == EMPTY) {
            return false;
        }
        
        int rowDistance = Math.abs(row - emptyRow);
        int colDistance = Math.abs(col - emptyCol);
        
        return rowDistance + colDistance == 1;
    }
    
    private boolean isInsideBorad(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }
    
    public boolean isSolved() {
        int expectedNumber = 0;
        
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                boolean isLastField = row == size - 1 && col == size - 1;
                
                if(isLastField) {
                    return board[row][col] == EMPTY;
                }
                
                if(board[row][col] != expectedNumber) {
                    return false;
                }
                
                expectedNumber++;
            }
        }
        
        return true;
    }
}