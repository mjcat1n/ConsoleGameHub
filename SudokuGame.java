import java.util.Optional;
import java.util.Scanner;

/**
 * A number placement puzzle on a 9x9 grid.
 * The objective is to fill the grid with digits from 1 to 9 so that each
 * column, row, and 3x3 subgrid contains all digits without repetition.
 * <pre>
 * Implements puzzle validation and a playable UI.
 * May also generate puzzles.
 * </pre>
 * @version 1
 */
class SudokuGame implements Game {
    /** Grid size. */
    private static final int SIZE = 9;
    /** The puzzle grid. */
    private int[][] grid;
    /** Fixed cells that can't be changed. */
    private boolean[][] fixed;
    /** Console input. */
    private final Scanner scanner = new Scanner(System.in);

    /** Sample puzzle (0 = empty). */
    private static final int[][] PUZZLE = {
        {5, 3, 0, 0, 7, 0, 0, 0, 0},
        {6, 0, 0, 1, 9, 5, 0, 0, 0},
        {0, 9, 8, 0, 0, 0, 0, 6, 0},
        {8, 0, 0, 0, 6, 0, 0, 0, 3},
        {4, 0, 0, 8, 0, 3, 0, 0, 1},
        {7, 0, 0, 0, 2, 0, 0, 0, 6},
        {0, 6, 0, 0, 0, 0, 2, 8, 0},
        {0, 0, 0, 4, 1, 9, 0, 0, 5},
        {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    @Override
    public String getName() {
        return "Sudoku";
    }

    @Override
    public Optional<Integer> play() {
        System.out.println("Welcome to Sudoku!");
        System.out.println("Fill the grid so each row, column, and 3x3 box");
        System.out.println("contains digits 1-9 without repetition.");
        System.out.println("Commands: 'row col value' to set, "
            + "'c row col' to clear, 'q' to quit");

        initGame();
        printBoard();

        while (true) {
            System.out.print("Enter command: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("q")) {
                System.out.println("Thanks for playing!");
                return Optional.of(0);
            }

            String[] parts = input.split("\\s+");

            if (parts.length == 2 && parts[0].equals("c")) {
                try {
                    int row = Integer.parseInt(parts[1].substring(0, 1)) - 1;
                    int col = Integer.parseInt(parts[1].substring(1, 2)) - 1;
                    if (fixed[row][col]) {
                        System.out.println("Cannot clear a fixed cell.");
                    } else {
                        grid[row][col] = 0;
                    }
                    printBoard();
                } catch (Exception e) {
                    System.out.println("Invalid. Use 'c row col'");
                }
                continue;
            }

            if (parts.length != 3) {
                System.out.println("Invalid. Use 'row col value' or 'c row col'");
                continue;
            }

            try {
                int row = Integer.parseInt(parts[0]) - 1;
                int col = Integer.parseInt(parts[1]) - 1;
                int val = Integer.parseInt(parts[2]);

                if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
                    System.out.println("Row/col must be 1-9.");
                    continue;
                }
                if (val < 1 || val > 9) {
                    System.out.println("Value must be 1-9.");
                    continue;
                }
                if (fixed[row][col]) {
                    System.out.println("Cannot modify a fixed cell.");
                    continue;
                }

                grid[row][col] = val;
                printBoard();

                if (!isValidPlacement(row, col, val)) {
                    System.out.println("Warning: This creates a conflict!");
                }

                if (isSolved()) {
                    System.out.println("Congratulations! Puzzle solved!");
                    return Optional.of(1);
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid numbers. Use 'row col value'");
            }
        }
    }

    private void initGame() {
        grid = new int[SIZE][SIZE];
        fixed = new boolean[SIZE][SIZE];

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = PUZZLE[r][c];
                fixed[r][c] = PUZZLE[r][c] != 0;
            }
        }
    }

    private void printBoard() {
        System.out.println("    1 2 3   4 5 6   7 8 9");
        System.out.println("  +-------+-------+-------+");
        for (int r = 0; r < SIZE; r++) {
            System.out.print((r + 1) + " | ");
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] == 0) {
                    System.out.print(". ");
                } else {
                    System.out.print(grid[r][c] + " ");
                }
                if ((c + 1) % 3 == 0) {
                    System.out.print("| ");
                }
            }
            System.out.println();
            if ((r + 1) % 3 == 0) {
                System.out.println("  +-------+-------+-------+");
            }
        }
    }

    private boolean isValidPlacement(int row, int col, int val) {
        // Check row
        for (int c = 0; c < SIZE; c++) {
            if (c != col && grid[row][c] == val) {
                return false;
            }
        }
        // Check column
        for (int r = 0; r < SIZE; r++) {
            if (r != row && grid[r][col] == val) {
                return false;
            }
        }
        // Check 3x3 box
        int boxR = (row / 3) * 3;
        int boxC = (col / 3) * 3;
        for (int r = boxR; r < boxR + 3; r++) {
            for (int c = boxC; c < boxC + 3; c++) {
                if ((r != row || c != col) && grid[r][c] == val) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isSolved() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] == 0) {
                    return false;
                }
                if (!isValidPlacement(r, c, grid[r][c])) {
                    return false;
                }
            }
        }
        return true;
    }
}
