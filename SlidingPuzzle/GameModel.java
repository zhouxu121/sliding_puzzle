
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

    /**
     * Initializes the board in its solved state.
     * 
     * The numbers are stored in ascending order from left to right
     * and from top to bottom. The final field in the bottom-right corner is empty.
     */
    private void initializeSolvedBoard() {
        int number = 0;

        for (int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                board[row][col] = number;
                number++;
            }
        }
        
        // Place the empty field in the bottom-right corner.
        emptyRow = size - 1;
        emptyCol = size - 1;
        board[emptyRow][emptyCol] = EMPTY;
    }
    
    /**
     * Shuffles the puzzle by performing a large number
     * of valid random moves.
     * 
     * The method does not randomly rearrange all numbers.
     * Using valid moves guarantees that the resulting puzzle can always be solved.
     */
    private void shuffle() {
        /**
         * Possible movement directions:
         * 
         * index 0: up
         * index 1: down
         * index 2: left
         * index 3: right
         */
        int[] rowDirections = {-1, 1, 0, 0};
        int[] colDirections = {0, 0, -1, 1};
        
        /**
         * Stores the previous position of the empty field.
         * Ths prevents the next random move from immediately
         * undoing the previous move.
         */
        int previousEmptyRow = -1;
        int previousEmptyCol = -1;
        
        int shuffleMoves = size * size * 100;
        int performedMoves = 0;
        
        while(performedMoves < shuffleMoves) {
            int direction = (int) (Math.random() * 4);
            
            /**
             * A tile at this target positio would move
             * into the current empty field.
             */
            int targetRow = emptyRow + rowDirections[direction];
            int targetCol = emptyCol + colDirections[direction];
            
            // Ignore positions outside the board.
            if(!isInsideBorad(targetRow, targetCol)) {
                continue;
            }
            
            /**
             * Avoid immediately reversing the previous move.
             * This produces a better shuffled board.
             */
            if(targetRow == previousEmptyRow && targetCol == previousEmptyCol) {
                continue;
            }
            
            int oldEmptyRow = emptyRow;
            int oldEmptyCol = emptyCol;
            
            move(targetRow, targetCol);
            
            previousEmptyRow = oldEmptyRow;
            previousEmptyCol = oldEmptyCol;
            
            performedMoves++;
        }
        
        /**
         * In the unlikely case that the board returns to 
         * the solved state, shuffle it again.
         */
        if(isSolved()) {
            shuffle();
        }
    }
    
    
    /**
     * Moves the tile at the given position into the empty field.
     * 
     * If the selected tile is not next to the empty field,
     * the board remains unchanged.
     * 
     * @param row row position of the selected tile
     * @param col colum position of the selected tile
     * @return true if the tile was moved, otherwisse false
     */
    public boolean move(int row, int col) {
        if(!canMove(row, col)) {
            return false;
        }
        
        board[emptyRow][emptyCol] = board[row][col];
        board[row][col] = EMPTY;
        
        // Update the stored position of the empty field
        emptyRow = row;
        emptyCol = col;
        
        return true;
    }
    
    /**
     * Checks whether the tile at the given position can move
     * into the empty field.
     * 
     * A title can only move if it is directly above, below, left or right of the empty feild.
     * 
     * @param row row position of the selected tile
     * @param col colum position of the selected tile
     * @return true if the tile can move, otherwise false
     */
    public boolean canMove(int row, int col) {
        if(!isInsideBorad(row, col)) {
            return false;
        }
        
        // The empty field itself cannot be moved.
        if(board[row][col] == EMPTY) {
            return false;
        }
        
        int rowDistance = Math.abs(row - emptyRow);
        int colDistance = Math.abs(col - emptyCol);
        
        /**
         * A Manhattan distance of exactly one means that
         * he tile is directly next to the empty field.
         */
        return rowDistance + colDistance == 1;
    }
    
    /**
     * Checks whether a position is located inside the board.
     */
    private boolean isInsideBorad(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }
    
    /**
     * Checks whether all tiles are in the correct order.
     * 
     * @return true if the puzzle is solved, otherwise false
     */
    public boolean isSolved() {
        int expectedNumber = 0;
        
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                boolean isLastField = row == size - 1 && col == size - 1;
                
                // The final field must be empty in the solved state.
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
    
    /**
     * Returns the value stored at a specific board position.
     * 
     * @param row requested row
     * @param col requested column
     * @return the number stored at the position, or EMPTY
     * @throws IndexOutOfBoundsException if the position is invalid
     */
    public int getValueAt(int row, int col) {
        if(!isInsideBorad(row, col)) {
            throw new IndexOutOfBoundsException("Invalid board position.");
        }
        
        return board[row][col];
    }
    
    /**
     * Checks whether the specified field is empty
     */
    public boolean isEmpty(int row, int col) {
        return getValueAt(row, col) == EMPTY;
    }
    
    /**
     * Returns the size of the square board.
     */
    public int getSize() {
        return size;
    }
}