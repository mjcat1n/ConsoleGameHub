import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

/**
 * A puzzle game in which the player uncovers cells on a grid.
 * Some cells contain mines. Others show the number of adjacent mines.
 * The player must avoid the mines and clear the safe spaces to win.
 * <pre>
 * Consider simulating the grid using a 2D array,
 * implementing recursive revealing, and providing flagging of cells.
 * </pre>
 * @version 1
 * @author - Chad Ninteman
 * @author - Jose Ocampo
 * @author - Toren Kochman
 */
class MineSweeperGame implements Game {
    /** Grid size. */
    private static final int SIZE = 8;
    /** Number of mines. */
    private static final int MINES = 10;
    /** Mine marker. */
    private static final int MINE = -1;
    /** The game grid with mine counts. */
    private int[][] grid;
    /** Revealed cells. */
    private boolean[][] revealed;
    /** Flagged cells. */
    private boolean[][] flagged;
    /** Console input. */
    private final Scanner scanner = new Scanner(System.in);
    /** Random for mine placement. */
    private final Random random = new Random();

    @Override
    public String getName() {
        return "MineSweeper";
    }

    @Override
    public Optional<Integer> play() {
        System.out.println("Welcome to Minesweeper!");
        System.out.println("Uncover tiles to reveal numbers or mines.");
        System.out.println("Numbers tell how many of the "
            + "8 adjacent tiles are mines.");
        System.out.println("Commands: 'r row col' to reveal, "
            + "'f row col' to flag/unflag");
        System.out.println("Uncover all safe tiles to win!");

        initGame();
        printBoard(false);

        while (true) {
            System.out.print("Enter command: ");
            String input = scanner.nextLine().trim().toLowerCase();
            String[] parts = input.split("\\s+");

            if (parts.length != 3) {
                System.out.println("Invalid. Use 'r row col' or 'f row col'");
                continue;
            }

            char cmd = parts[0].charAt(0);
            int row, col;
            try {
                row = Integer.parseInt(parts[1]) - 1;
                col = Integer.parseInt(parts[2]) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid coordinates.");
                continue;
            }

            if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
                System.out.println("Coordinates out of range (1-" + SIZE + ")");
                continue;
            }

            if (cmd == 'f') {
                if (revealed[row][col]) {
                    System.out.println("Can't flag a revealed cell.");
                } else {
                    flagged[row][col] = !flagged[row][col];
                }
                printBoard(false);
            } else if (cmd == 'r') {
                if (flagged[row][col]) {
                    System.out.println("Unflag first to reveal.");
                    continue;
                }
                if (revealed[row][col]) {
                    System.out.println("Already revealed.");
                    continue;
                }
                if (grid[row][col] == MINE) {
                    printBoard(true);
                    System.out.println("BOOM! You hit a mine. Game over!");
                    return Optional.of(0);
                }
                reveal(row, col);
                printBoard(false);

                if (checkWin()) {
                    printBoard(true);
                    System.out.println("Congratulations! You cleared all "
                        + "safe cells!");
                    return Optional.of(1);
                }
            } else {
                System.out.println("Unknown command. Use 'r' or 'f'.");
            }
        }
    }

    private void initGame() {
        grid = new int[SIZE][SIZE];
        revealed = new boolean[SIZE][SIZE];
        flagged = new boolean[SIZE][SIZE];

        // Place mines
        int placed = 0;
        while (placed < MINES) {
            int r = random.nextInt(SIZE);
            int c = random.nextInt(SIZE);
            if (grid[r][c] != MINE) {
                grid[r][c] = MINE;
                placed++;
            }
        }

        // Calculate adjacent counts
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] != MINE) {
                    grid[r][c] = countAdjacentMines(r, c);
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int nr = row + dr;
                int nc = col + dc;
                if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE) {
                    if (grid[nr][nc] == MINE) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private void reveal(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return;
        }
        if (revealed[row][col] || flagged[row][col]) {
            return;
        }
        if (grid[row][col] == MINE) {
            return;
        }

        revealed[row][col] = true;

        if (grid[row][col] == 0) {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    reveal(row + dr, col + dc);
                }
            }
        }
    }

    private boolean checkWin() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] != MINE && !revealed[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void printBoard(boolean showMines) {
        System.out.print("  ");
        for (int c = 1; c <= SIZE; c++) {
            System.out.print(c + " ");
        }
        System.out.println();

        for (int r = 0; r < SIZE; r++) {
            System.out.print((r + 1) + " ");
            for (int c = 0; c < SIZE; c++) {
                if (showMines && grid[r][c] == MINE) {
                    System.out.print("* ");
                } else if (flagged[r][c]) {
                    System.out.print("F ");
                } else if (!revealed[r][c]) {
                    System.out.print(". ");
                } else if (grid[r][c] == 0) {
                    System.out.print("  ");
                } else {
                    System.out.print(grid[r][c] + " ");
                }
            }
            System.out.println();
        }
    }
}
