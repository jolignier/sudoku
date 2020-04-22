/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * @author Ipro
 * Date  20/04/2020
 */

public class GameBoard {

    private int[][] board;
    private int[][] solvedBoard;
    private String difficulty;

    private int nbGridGenerated = 0;
    private int[][] bestBoard;
    private int[][] solvedBestBoard;
    private int bestBoardDistance = Integer.MAX_VALUE;

    public GameBoard(String difficulty) {
        this.difficulty = difficulty;
        board = new int[9][9];
        solvedBoard = new int[9][9];
        solvedBestBoard = new int[9][9];
        this.generate();
        this.displayBoard();
        this.displaySolvedBoard();
    }

    public GameBoard(int[][] board){
        this.board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }

    public int[][] getBoard() {
        return this.board.clone();
    }

    public int[][] getSolvedBoard() {
        return this.solvedBoard;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getCell(int row, int col){
        return board[row][col];
    }

    public void setCell(int num, int row, int col) {
        this.board[row][col] = num;
    }

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

    public void displayBoard(){
        String res = "";
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                res += board[i][j] + " ";
            }
            res += "\n";
        }
        res += "";

        System.out.println(res);
    }

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

    private void fillDiagonal() {
        for (int i = 0; i <9; i+=3) {
            fillBox(i,i);
        }
    }

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

    public boolean canPlace(int num, int row, int col) {
        boolean res = true;
        if(isInRow(num, row) || isInCol(num, col) || isInBox(num, row-row%3, col-col%3)){
            res = false;
        }

        return res;
    }

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

    private int getRandomNumber() {
        return (int) Math.floor((Math.random()*9 +1));
    }

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

    public boolean isUniqueSolution() {
        SudokuSolver solver = new SudokuSolver(new GameBoard(board));

        solver.solve(true);
        GameBoard firstSolution = solver.getBoard();

        solver = new SudokuSolver(new GameBoard(board));
        solver.solve(false);

        GameBoard secondSolution = solver.getBoard();

        return areEquals(firstSolution, secondSolution);
    }
}