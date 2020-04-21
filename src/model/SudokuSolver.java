package model;

/**
 * @author Ipro
 * Date  20/04/2020
 */

public class SudokuSolver {

    private GameBoard board;

    public SudokuSolver(GameBoard board) {
        this.board = board;
    }

    public GameBoard getBoard() {
        return this.board;
    }

    public boolean solve(boolean orderAsc) {
        int row = -1; int col =-1;
        boolean isEmpty = true;

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
        } else {
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