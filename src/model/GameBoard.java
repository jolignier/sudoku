package model;

import java.util.ArrayList;

/**
 * Date  20/04/2020 <br>
 * Represents a sudoku gameboard with a unique solution. <br>
 * The gameboard have two different board, representing each state :
 * <ul>
 *     <li>"Current" : represent the current game with given numbers and player's input ones</li>
 *     <li>"Solved" : represent the solution of the current board</li>
 * </ul>
 */
public class GameBoard {

    private int[][] board;
    private int[][] solvedBoard;
    private String difficulty;

    private int nbGridGenerated = 0;
    private int[][] bestBoard;
    private int[][] solvedBestBoard;
    private int bestBoardDistance = Integer.MAX_VALUE;

    /**
     * Basic constructor
     * @param difficulty the difficulty to generate the board with
     */
    public GameBoard(String difficulty) {
        this.difficulty = difficulty;
        board = new int[9][9];
        solvedBoard = new int[9][9];
        solvedBestBoard = new int[9][9];
        this.generate();
    }

    /**
     * Copy constructor
     * @param board the board to copy
     */
    public GameBoard(int[][] board){
        this.board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }

    /**
     * @return the board
     */
    public int[][] getBoard() {
        return this.board.clone();
    }

    /**
     * @return the solved board
     */
    public int[][] getSolvedBoard() {
        return this.solvedBoard;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * @param row the cell row
     * @param col the cell column
     * @return the value of the cell
     */
    public int getCell(int row, int col){
        return board[row][col];
    }

    /**
     * @param num the new value of the cell
     * @param row the cell row
     * @param col the cell column
     */
    public void setCell(int num, int row, int col) {
        this.board[row][col] = num;
    }

    /**
     * @return true if there is no empty cell in the board
     */
    public boolean isFull() {
        boolean res = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(board[i][j] == 0) {
                    res = false;
                    break;
                }
            }
        }
        return res;
    }

    /**
     * @return true if the board matches the solvedBoard
     */
    public boolean isComplete() {
        boolean res = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(board[i][j] != solvedBoard[i][j]) {
                    res = false;
                    break;
                }
            }
        }
        return res;
    }

    /**
     * Recursive function to generate a Sudoku grid
     * Principle :
     *    - We fill the diagnoal miniGrids (3*3 square) because they are totally independants
     *    - We fill remaining values
     *    - We get the numbers of value to remove based on the given difficulty
     *    - If we successfully removed the correct numbers of value (+- 5)
     *          we are done
 *        - Else, we try again (maximum 1000 times)
 *        - if we have not successed, we state that the board is the closest one we have generated
     */
    public void generate() {        
        if (this.nbGridGenerated < 1000) {
            fillDiagonal();
            fillRemaining(0,3);
            
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    this.solvedBoard[i][j] = board[i][j];
                }
            }
            
            int n = getNumbersToRemove();
            int distance = removeNumbers(n);

            if (distance> 5) {
                if (distance < this.bestBoardDistance) {
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            this.solvedBestBoard[i][j] = solvedBoard[i][j];
                        }
                    }
                    this.bestBoard = board;
                    this.bestBoardDistance = distance;
                }
                board = new int[9][9];
                this.nbGridGenerated++;
                generate();
            } else {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        this.solvedBestBoard[i][j] = solvedBoard[i][j];
                    }
                }
                this.bestBoard = board;
            }        
        }
        this.solvedBoard = solvedBestBoard;
        this.board = bestBoard;
    }

    /**
     * @return the board as a formatted string
     */
    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                res += board[i][j] + " ";
            }
            res += "\n";
        }
        res += "";
        return res;
    }

    /**
     * Display the board as a formatted string
     */
    public void displayBoard(){
        System.out.println(this);
    }

    /**
     * Display the solved board as a formatted string
     */
    public void displaySolvedBoard(){
        String res = "";
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                res += solvedBoard[i][j] + " ";
            }
            res += "\n";
        }
        res += "";
        System.out.println(res);
    }

    /**
     * fill the 3 diagonal miniGrid box (3*3 squares)
     */
    private void fillDiagonal() {
        for (int i = 0; i <9; i+=3) {
            fillBox(i,i);
        }
    }

    /**
     * Fill a 3*3 box
     * @param row the box beginning row
     * @param col the box beginning col
     */
    private void fillBox(int row, int col) {
        int num = getRandomNumber();
        for (int i = 0; i <3; i++) {
            for (int j = 0; j <3; j++) {
                while (isInBox(num, row, col)){
                    num = getRandomNumber();
                }
                board[row+i][col+j] = num;
            }
        }
    }

    /**
     * Recursive function to fill all the empty values of the board
     * @param row the row to start filling from
     * @param col the col to start filling from
     * @return true if the board is filled
     */
    private boolean fillRemaining(int row, int col) {
        // At the end of the row, we go back
        // At the beginning of the next row
        if (col >= 9 && row<8){
            row+=1; col=0;
        }
        // If we are at the end of the grid
        if(col >=9 && row >=9) {
            return true;
        }
        // I we are in a diagonal box,
        // We just move to the one on its right
        if (row < 3) {
            if (col <3) col=3;
        } else if (row < 6) {
            if (col >=3 && col <6) col =6;
        } else {
            if (col >= 6) {
                row+= 1; col=0;
                if (row >= 9) return true;
            }
        }

        for (int num=1; num <=9; num++) {
            if (canPlace(num, row, col)){
                board[row][col] = num;
                if (fillRemaining(row, col+1))
                    return true;
                board[row][col] = 0;
            }
        }

        return false;
    }

    /**
     * @return A random cell of the grid as a 2D point
     */
    public Point getRandomCell() {
        int i = getRandomNumber()-1;
        int j = getRandomNumber()-1;
        Point initialCell = new Point(i,j);
        while(board[i][j] == solvedBoard[i][j]) {
            j++;
            if (j > 8) {
                j=0; i++;
            }
            if (i > 8) {
                i=0;
            }
            if (i== initialCell.getX() && j == initialCell.getY()){
                return null;
            }
        }

        return new Point(i,j);
    }

    /**
     * @param num the number to test
     * @param row the row to check into
     * @return true if the number is in the row
     */
    private boolean isInRow(int num, int row) {
        boolean res = false;
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) {
                res = true;
                break;
            }
        }
        return res;
    }

    /**
     * @param num the number to test
     * @param col the col to check into
     * @return true if the number is in the col
     */
    private boolean isInCol(int num, int col) {
        boolean res = false;
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num) {
                res = true;
                break;
            }
        }
        return res;
    }

    /**
     * @param num the number to place
     * @param row the row to place into
     * @param col the col to place into
     * @return true if we can place the number at the given cell
     */
    public boolean canPlace(int num, int row, int col) {
        boolean res = true;
        if(isInRow(num, row) || isInCol(num, col) || isInBox(num, row-row%3, col-col%3)){
            res = false;
        }

        return res;
    }

    /**
     * @param num the number to test
     * @param row the row to check into
     * @param col the col to check into
     * @return true if the number is in the miniGrid box
     */
    private boolean isInBox(int num, int row, int col) {
        boolean res = false;
        for (int i = 0; i <3; i++) {
            for (int j = 0; j <3; j++) {
                if (board[row+i][col+j] == num) {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }

    /**
     * @return a random number from 1 to 9
     */
    private int getRandomNumber() {
        return (int) Math.floor((Math.random()*9 +1));
    }

    /**
     * @return the numbers of value to remove from the grid based on difficulty
     */
    private int getNumbersToRemove() {
        int n;
        switch (difficulty){
            case "easy": default:
                n = 40;
                break;
            case "medium":
                n = 45;
                break;
            case "hard":
                n = 55;
                break;
            case "very_hard":
                n = 64;
                break;
        }

        return n;
    }

    /**
     * Principle :
     *     - We remove a number
     *     - We check if the solution is still unique without the removed number
     *     - If it is, we continue to remove numbers
     *     - Elsewise we put back the number and stop trying to remove numbers
     * @param nb number of values to remove
     * @return the numbers of values removed
     */
    private int removeNumbers(int nb) {
        boolean canContinue = true;
        while(canContinue) {
            int col = getRandomNumber()-1;
            int row = getRandomNumber()-1;
            if (board[row][col] != 0) {
                int value = board[row][col];
                board[row][col] = 0;
                if (isUniqueSolution()) {
                    nb--;
                    if (nb <=0) {
                        canContinue = false;
                    }
                } else {
                    board[row][col] = value;
                    canContinue = false;
                }
            }
        }
        return nb;
    }

    /**
     * Static method to compare two boards
     * @param b1 the first board to compare
     * @param b2 the second board to compare
     * @return true if the boards are equals
     */
    public static boolean areEquals(GameBoard b1, GameBoard b2) {
        for (int i=0; i<9; i++) {
            for (int j =0; j<9; j++) {
                if (b1.getCell(i,j) != b2.getCell(i,j)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @return true if ther is only one solution to the board
     */
    public boolean isUniqueSolution() {
        SudokuSolver solver = new SudokuSolver(new GameBoard(board));

        solver.solve(true);
        GameBoard firstSolution = solver.getBoard();

        solver = new SudokuSolver(new GameBoard(board));
        solver.solve(false);

        GameBoard secondSolution = solver.getBoard();

        return areEquals(firstSolution, secondSolution);
    }

    /**
     * Calculate the Branch difficulty score of the board
     * from 0 to 254 : easiest to hardest
     * @return branch difficulty score
     */
    public int BranchDifficultyScore() {
        int[][] tempGrid = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tempGrid[i][j] = this.board[i][j];
            }
        }

        ArrayList<ArrayList<Integer>> empty = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tempGrid[i][j] == 0) {
                    ArrayList<Integer> temp = new ArrayList<>();
                    temp.add(i*9 + j);

                    for (int num = 0; num < 9; num++) {
                        if (canPlace(num, i, j)) {
                            temp.add(num);
                        }
                    }
                    empty.add(temp);
                }
            }
        }

        int minIndex = 0;
        int check = empty.size();
        for (int i = 0; i < check; i++) {
            if (empty.get(i).size() < empty.get(minIndex).size()) {
                 minIndex = i;
            }
        }
        int branchFactor = empty.get(minIndex).size();

        int factor = (int)Math.pow(branchFactor-2, 2);
        int emptyCells = empty.size();

        int score = factor*100 + emptyCells;

        return score;
    }
}