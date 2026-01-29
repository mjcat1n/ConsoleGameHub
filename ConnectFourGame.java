import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

/**
 * A vertical strategy game where the player plays against a computer AI.
 * The player and the computer take turns dropping colored discs into a
 * 7-column, 6-row grid.
 * The goal is to be the first to form a line of four discs
 * horizontally, vertically, or diagonally.
 * <pre>
 * Implement simple or advanced AI for the opponent.
 * Consider using a matrix for the board state.
 * </pre>
 * @version 1
 */
class ConnectFourGame implements Game {
    /** Number of columns. */
    private static final int COLS = 7;
    /** Number of rows. */
    private static final int ROWS = 6;
    /** Empty cell. */
    private static final char EMPTY = '.';
    /** Player disc. */
    private static final char PLAYER = 'X';
    /** Computer disc. */
    private static final char COMPUTER = 'O';
    /** The game board. */
    private char[][] board;
    /** Console input. */
    private final Scanner scanner = new Scanner(System.in);
    /** Random for AI. */
    private final Random random = new Random();

    @Override
    public String getName() {
        return "Connect Four";
    }

    @Override
    public Optional<Integer> play() {
        System.out.println("Welcome to Connect Four!");
        System.out.println("Drop discs into columns to connect 4 in a row.");
        System.out.println("You are X, computer is O.");

        board = new char[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                board[r][c] = EMPTY;
            }
        }

        printBoard();

        while (true) {
            // Player turn
            int col = getPlayerMove();
            dropDisc(col, PLAYER);
            printBoard();

            if (checkWin(PLAYER)) {
                System.out.println("Congratulations! You win!");
                return Optional.of(1);
            }
            if (isBoardFull()) {
                System.out.println("It's a draw!");
                return Optional.of(0);
            }

            // Computer turn
            System.out.println("Computer is thinking...");
            int compCol = getComputerMove();
            dropDisc(compCol, COMPUTER);
            printBoard();

            if (checkWin(COMPUTER)) {
                System.out.println("Computer wins! Better luck next time.");
                return Optional.of(0);
            }
            if (isBoardFull()) {
                System.out.println("It's a draw!");
                return Optional.of(0);
            }
        }
    }

    private void printBoard() {
        System.out.println(" 1 2 3 4 5 6 7");
        for (int r = 0; r < ROWS; r++) {
            System.out.print("|");
            for (int c = 0; c < COLS; c++) {
                System.out.print(board[r][c] + "|");
            }
            System.out.println();
        }
        System.out.println("---------------");
    }

    private int getPlayerMove() {
        while (true) {
            System.out.print("Choose column (1-7): ");
            String input = scanner.nextLine().trim();
            try {
                int col = Integer.parseInt(input) - 1;
                if (col < 0 || col >= COLS) {
                    System.out.println("Invalid column. Choose 1-7.");
                    continue;
                }
                if (board[0][col] != EMPTY) {
                    System.out.println("Column is full. Choose another.");
                    continue;
                }
                return col;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number.");
            }
        }
    }

    private int getComputerMove() {
        // Try to win
        for (int c = 0; c < COLS; c++) {
            if (board[0][c] == EMPTY) {
                int r = getDropRow(c);
                board[r][c] = COMPUTER;
                if (checkWin(COMPUTER)) {
                    board[r][c] = EMPTY;
                    return c;
                }
                board[r][c] = EMPTY;
            }
        }
        // Block player
        for (int c = 0; c < COLS; c++) {
            if (board[0][c] == EMPTY) {
                int r = getDropRow(c);
                board[r][c] = PLAYER;
                if (checkWin(PLAYER)) {
                    board[r][c] = EMPTY;
                    return c;
                }
                board[r][c] = EMPTY;
            }
        }
        // Prefer center
        if (board[0][3] == EMPTY) {
            return 3;
        }
        // Random valid column
        while (true) {
            int c = random.nextInt(COLS);
            if (board[0][c] == EMPTY) {
                return c;
            }
        }
    }

    private int getDropRow(int col) {
        for (int r = ROWS - 1; r >= 0; r--) {
            if (board[r][col] == EMPTY) {
                return r;
            }
        }
        return -1;
    }

    private void dropDisc(int col, char disc) {
        int row = getDropRow(col);
        if (row >= 0) {
            board[row][col] = disc;
        }
    }

    private boolean checkWin(char disc) {
        // Horizontal
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c <= COLS - 4; c++) {
                if (board[r][c] == disc && board[r][c + 1] == disc
                        && board[r][c + 2] == disc && board[r][c + 3] == disc) {
                    return true;
                }
            }
        }
        // Vertical
        for (int r = 0; r <= ROWS - 4; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] == disc && board[r + 1][c] == disc
                        && board[r + 2][c] == disc && board[r + 3][c] == disc) {
                    return true;
                }
            }
        }
        // Diagonal down-right
        for (int r = 0; r <= ROWS - 4; r++) {
            for (int c = 0; c <= COLS - 4; c++) {
                if (board[r][c] == disc && board[r + 1][c + 1] == disc
                        && board[r + 2][c + 2] == disc
                        && board[r + 3][c + 3] == disc) {
                    return true;
                }
            }
        }
        // Diagonal up-right
        for (int r = 3; r < ROWS; r++) {
            for (int c = 0; c <= COLS - 4; c++) {
                if (board[r][c] == disc && board[r - 1][c + 1] == disc
                        && board[r - 2][c + 2] == disc
                        && board[r - 3][c + 3] == disc) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int c = 0; c < COLS; c++) {
            if (board[0][c] == EMPTY) {
                return false;
            }
        }
        return true;
    }
}
