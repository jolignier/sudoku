package model;

/**
 * @author Ipro
 * Date  20/04/2020
 */

public class SudokuSolver {

    private GameBoard board;

    /**
     * Basic constructor
     * @param board the gameboard to solve
     */
    public SudokuSolver(GameBoard board) {
        this.board = board;
    }

    /**
     * @return the gameboard
     */
    public GameBoard getBoard() {
        return this.board;
    }

    /**
     * Recursive function to solve the gameboard
     * @param orderAsc the order to fill the grid with (true = 0..9, false = 9..0)
     * @return true if the sudoku is solved
     */
    public boolean solve(boolean orderAsc) {
        int row = -1; int col =-1;
        boolean isEmpty = true;
        // get the coordinates of the next empty cell
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++){
                if(board.getCell(i,j) == 0) {
                    row = i;
                    col = j;
                    // If there is missing values
                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty){
                break;
            }
        }
        // no empty space, meaning sudoku is solved
        if (isEmpty) {
            return true;
        }
        // Filling grid cell by testing from 0 to 9
        if (orderAsc) {
            for (int num=1; num <=9; num++) {
                if (board.canPlace(num, row, col)){
                    board.setCell(num, row, col);
                    if (solve(orderAsc)) {
                        return true;
                    }
                    else {
                        board.setCell(0, row, col);
                    }
                }
            }
        } else { // Filling grid cell by testing from 9 to 0
            for (int num=9; num >=1; num--) {
                if (board.canPlace(num, row, col)){
                    board.setCell(num, row, col);
                    if (solve(orderAsc)) {
                        return true;
                    } else {
                        board.setCell(0, row, col);
                    }
                }
            }
        }
        return false;
    }
}