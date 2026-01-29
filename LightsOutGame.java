import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

/**
 * A logic puzzle where each cell on a grid can be either "on" or "off".
 * Selecting a cell toggles it and its immediate neighbors.
 * The goal is to turn all the lights off in as few moves as possible.
 * <pre>
 * Consider a model using a grid of booleans.
 * Implement toggling logic on that grid.
 * </pre>
 * @version 1
 */
class LightsOutGame implements Game {
    /** Grid size. */
    private static final int SIZE = 5;
    /** The grid (true = on, false = off). */
    private boolean[][] grid;
    /** Console input. */
    private final Scanner scanner = new Scanner(System.in);
    /** Random for initial state. */
    private final Random random = new Random();

    @Override
    public String getName() {
        return "Lights Out";
    }

    @Override
    public Optional<Integer> play() {
        System.out.println("Welcome to Lights Out!");
        System.out.println("The tiles have an 'on' or 'off' function.");
        System.out.println("Selecting a tile will change its state.");
        System.out.println("Tiles also change the state of neighboring tiles.");
        System.out.println("Turn all lights OFF to win!");
        System.out.println("Enter 'row col' to toggle (e.g., '2 3')");
        System.out.println("Good luck!");

        initGame();
        int moves = 0;

        while (!isAllOff()) {
            printBoard();
            System.out.println("Moves: " + moves);
            System.out.print("Toggle position (row col): ");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                System.out.println("Thanks for playing!");
                return Optional.of(0);
            }

            String[] parts = input.split("\\s+");
            if (parts.length != 2) {
                System.out.println("Enter as 'row col' (e.g., '2 3')");
                continue;
            }

            try {
                int row = Integer.parseInt(parts[0]) - 1;
                int col = Integer.parseInt(parts[1]) - 1;

                if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
                    System.out.println("Position out of range (1-" + SIZE + ")");
                    continue;
                }

                toggle(row, col);
                moves++;

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter numbers.");
            }
        }

        printBoard();
        System.out.println("Congratulations! All lights are off!");
        System.out.println("You solved it in " + moves + " moves!");
        // Higher score for fewer moves
        int score = Math.max(0, 100 - moves * 5);
        return Optional.of(score);
    }

    private void initGame() {
        grid = new boolean[SIZE][SIZE];

        // Create a solvable puzzle by doing random toggles
        int toggles = 5 + random.nextInt(6);
        for (int i = 0; i < toggles; i++) {
            int r = random.nextInt(SIZE);
            int c = random.nextInt(SIZE);
            toggle(r, c);
        }

        // Make sure there's at least one light on
        if (isAllOff()) {
            toggle(SIZE / 2, SIZE / 2);
        }
    }

    private void toggle(int row, int col) {
        // Toggle selected cell
        grid[row][col] = !grid[row][col];

        // Toggle neighbors
        if (row > 0) {
            grid[row - 1][col] = !grid[row - 1][col];
        }
        if (row < SIZE - 1) {
            grid[row + 1][col] = !grid[row + 1][col];
        }
        if (col > 0) {
            grid[row][col - 1] = !grid[row][col - 1];
        }
        if (col < SIZE - 1) {
            grid[row][col + 1] = !grid[row][col + 1];
        }
    }

    private boolean isAllOff() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void printBoard() {
        System.out.print("  ");
        for (int c = 1; c <= SIZE; c++) {
            System.out.print(c + " ");
        }
        System.out.println();

        for (int r = 0; r < SIZE; r++) {
            System.out.print((r + 1) + " ");
            for (int c = 0; c < SIZE; c++) {
                System.out.print(grid[r][c] ? "O " : ". ");
            }
            System.out.println();
        }
    }
}
