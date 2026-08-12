import java.util.Objects;
import java.util.Random;

/**
 * Represents the game logic of a sliding puzzle.
 * 
 * This class does not contain any Swing or GUI code.
 * It only stores the board state and implements the rules of the puzzle.
 * Keeping the model separate from the GUI makes the logic easier to test and reuse.
 */
public class GameModel
{
    // Sentinel value used to represent the empty field.
    private static final int EMPTY = -1;

    // Number of rows and columns of the square board.
    // Valid values are 3, 4, 5.
    private final int size;

    // Stores the values of every puzzle field. EMPTY marks the blank position.
    private final int[][] board;

    // Random source used to choose valid shuffle moves.
    private final Random random;

    // current row position of the empty field.
    private int emptyRow;

    // current column position of the empty field.
    private int emptyCol;

    /**
     * Create a new Sliding puzzle with the given size.
     * 
     * The board is first initialized in the solved state
     * and then shuffled using valid puzzle moves.
     * 
     * @param size the number of rows and columns
     * @throws IllegalArgumentException if the size is not 3, 4 or 5
     */

    public GameModel(int size) {
        this(size, new Random());
    }

    /**
     * Creates a new puzzle with a supplied random source.
     *
     * Keeping this constructor package-private makes the model deterministic
     * in automated tests without exposing randomness to the user interface.
     *
     * @param size the number of rows and columns
     * @param random random source used while shuffling
     */
    GameModel(int size, Random random) {
        if(size < 3 || size > 5) {
            throw new IllegalArgumentException("Size must be 3, 4 or 5.");
        }

        this.size = size;
        this.random = Objects.requireNonNull(random, "Random source must not be null.");
        this.board = new int[size][size];

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
        /*
         * Four possible directions around the empty field:
         * up, down, left and right.
         */
        int[] rowDirections = {-1, 1, 0, 0};
        int[] colDirections = {0, 0, -1, 1};

        do {
            // Avoid immediately reversing the previous valid move.
            int previousEmptyRow = -1;
            int previousEmptyCol = -1;
            int shuffleMoves = size * size * 100;
            int performedMoves = 0;

            while(performedMoves < shuffleMoves) {
                // Randomly choose one of the four neighboring directions.
                int direction = random.nextInt(4);
                int targetRow = emptyRow + rowDirections[direction];
                int targetCol = emptyCol + colDirections[direction];

                // Ignore positions outside the board.
                if(!isInsideBoard(targetRow, targetCol)) {
                    continue;
                }

                if(targetRow == previousEmptyRow && targetCol == previousEmptyCol) {
                    continue;
                }

                /*
                 * Remember the old empty position so the next shuffle step
                 * does not immediately undo this move.
                 */
                int oldEmptyRow = emptyRow;
                int oldEmptyCol = emptyCol;
                move(targetRow, targetCol);

                previousEmptyRow = oldEmptyRow;
                previousEmptyCol = oldEmptyCol;
                
                
                performedMoves++;
            }
        } while(isSolved());
    }
    
    
    /**
     * Moves the tile at the given position into the empty field.
     * 
     * If the selected tile is not next to the empty field,
     * the board remains unchanged.
     * 
     * @param row row position of the selected tile
     * @param col column position of the selected tile
     * @return true if the tile was moved, otherwise false
     */
    public boolean move(int row, int col) {
        if(!canMove(row, col)) {
            return false;
        }
        
        /*
         * Copy the selected tile into the empty position, then mark the
         * selected tile's old position as the new empty field.
         */
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
     * A tile can only move if it is directly above, below, left or right of the empty field.
     * 
     * @param row row position of the selected tile
     * @param col column position of the selected tile
     * @return true if the tile can move, otherwise false
     */
    public boolean canMove(int row, int col) {
        if(!isInsideBoard(row, col)) {
            return false;
        }
        
        // The empty field itself cannot be moved.
        if(board[row][col] == EMPTY) {
            return false;
        }
        
        /*
         * Manhattan distance is used to check whether the selected tile
         * is orthogonally adjacent to the empty field.
         */
        int rowDistance = Math.abs(row - emptyRow);
        int colDistance = Math.abs(col - emptyCol);
        
        /**
         * A Manhattan distance of exactly one means that
         * the tile is directly next to the empty field.
         */
        return rowDistance + colDistance == 1;
    }
    
    /**
     * Checks whether a position is located inside the board.
     * 
     * @param row row to validate
     * @param col column to validate
     * @return true if the position belongs to the board
     */
    private boolean isInsideBoard(int row, int col) {
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
        if(!isInsideBoard(row, col)) {
            throw new IndexOutOfBoundsException("Invalid board position.");
        }
        
        return board[row][col];
    }
    
    /**
     * Checks whether the specified field is empty
     * 
     * @param row requested row
     * @param col requested column
     * @return true if the position contains the EMPTY marker
     */
    public boolean isEmpty(int row, int col) {
        return getValueAt(row, col) == EMPTY;
    }
    
    /**
     * Returns the size of the square board.
     * 
     * @return number of rows and columns
     */
    public int getSize() {
        return size;
    }
}
