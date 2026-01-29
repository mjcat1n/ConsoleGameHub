import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

/**
 * A classic 3x3 two-player game adapted for single-player mode
 * against the computer.
 * The player and the computer take turns marking Xs and Os on a grid.
 * The winner is the first to align three in a row (horizontally,
 * vertically, or diagonally).
 * <pre>
 * Implementation may be a basic AI using heuristics or
 * an unbeatable strategy using the Minimax algorithm.
 * </pre>
 * @version 2
 */
public class TicTacToeGame implements Game {
    /** Board size. */
    private static final int SIZE = 3;
    /** Player marker. */
    private static final char PLAYER = 'X';
    /** Computer marker. */
    private static final char COMPUTER = 'O';
    /** Empty cell. */
    private static final char EMPTY = '.';
    /** The game board. */
    private char[][] board;
    /** Console input. */
    private final Scanner scanner = new Scanner(System.in);
    /** Random for AI moves. */
    private final Random random = new Random();

    /**
     * Returns the name of the game.
     * @return the game name.
     */
    public String getName() {
        return "Tic-Tac-Toe";
    }

    /**
     * Plays the game and returns the score.
     * @return 1 for win, 0 for loss/draw.
     */
    public Optional<Integer> play() {
        System.out.println("Welcome to Tic-Tac-Toe!");
        System.out.println("Objective: Be the first to get 3 of your "
            + "marks in a row.");
        System.out.println("How to play: Enter row and column (1-3) "
            + "to place your X.");
        System.out.println("You are X, computer is O.");

        board = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }

        printBoard();

        while (true) {
            playerMove();
            printBoard();
            if (checkWin(PLAYER)) {
                System.out.println("Congratulations! You win!");
                return Optional.of(1);
            }
            if (isBoardFull()) {
                System.out.println("It's a draw!");
                return Optional.of(0);
            }

            computerMove();
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
        System.out.println("  1 2 3");
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void playerMove() {
        while (true) {
            System.out.print("Enter row and column (e.g., 1 2): ");
            String line = scanner.nextLine().trim();
            String[] parts = line.split("\\s+");
            if (parts.length != 2) {
                System.out.println("Invalid input. Enter two numbers.");
                continue;
            }
            try {
                int row = Integer.parseInt(parts[0]) - 1;
                int col = Integer.parseInt(parts[1]) - 1;
                if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
                    System.out.println("Invalid position. Use 1-3.");
                    continue;
                }
                if (board[row][col] != EMPTY) {
                    System.out.println("Cell already taken. Try again.");
                    continue;
                }
                board[row][col] = PLAYER;
                return;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter numbers only.");
            }
        }
    }

    private void computerMove() {
        System.out.println("Computer is thinking...");
        int[] move = findBestMove();
        if (move != null) {
            board[move[0]][move[1]] = COMPUTER;
        }
    }

    private int[] findBestMove() {
        // Try to win
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = COMPUTER;
                    if (checkWin(COMPUTER)) {
                        board[i][j] = EMPTY;
                        return new int[]{i, j};
                    }
                    board[i][j] = EMPTY;
                }
            }
        }
        // Block player
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = PLAYER;
                    if (checkWin(PLAYER)) {
                        board[i][j] = EMPTY;
                        return new int[]{i, j};
                    }
                    board[i][j] = EMPTY;
                }
            }
        }
        // Take center
        if (board[1][1] == EMPTY) {
            return new int[]{1, 1};
        }
        // Random move
        while (true) {
            int i = random.nextInt(SIZE);
            int j = random.nextInt(SIZE);
            if (board[i][j] == EMPTY) {
                return new int[]{i, j};
            }
        }
    }

    private boolean checkWin(char mark) {
        // Rows and columns
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] == mark && board[i][1] == mark
                    && board[i][2] == mark) {
                return true;
            }
            if (board[0][i] == mark && board[1][i] == mark
                    && board[2][i] == mark) {
                return true;
            }
        }
        // Diagonals
        if (board[0][0] == mark && board[1][1] == mark
                && board[2][2] == mark) {
            return true;
        }
        if (board[0][2] == mark && board[1][1] == mark
                && board[2][0] == mark) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
}
