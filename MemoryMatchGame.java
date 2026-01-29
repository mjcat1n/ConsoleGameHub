import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * A game that simulates a memory matching challenge.
 * Cards (represented by characters or numbers) are arranged in a grid.
 * The player flips two cards at a time and tries to find matching pairs.
 * <br />
 * The game ends when all pairs have been matched.
 * The score can be the number of turns that player took.
 * <pre>
 * Manages board state and the display of revealed vs. hidden tiles.
 * </pre>
 * @version 2.1
 */
class MemoryMatchGame implements Game {
    /** Grid rows. */
    private static final int ROWS = 4;
    /** Grid columns. */
    private static final int COLS = 4;
    /** Card symbols (need ROWS*COLS/2 unique). */
    private static final char[] SYMBOLS = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'
    };
    /** The card grid. */
    private char[][] cards;
    /** Revealed state. */
    private boolean[][] revealed;
    /** Console input. */
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getName() {
        return "Memory Match";
    }

    @Override
    public Optional<Integer> play() {
        System.out.println("Welcome, you are now playing Memory Match");
        System.out.println("Flip items to match pairs");
        System.out.println("If items don't match flip over, go again");
        System.out.println("When all items are matched you win");
        System.out.println("Enter positions as 'row col' (e.g., '1 2')");
        System.out.println("Have fun!!!");

        initGame();
        int turns = 0;
        int pairsFound = 0;
        int totalPairs = (ROWS * COLS) / 2;

        while (pairsFound < totalPairs) {
            printBoard();
            System.out.println("Pairs found: " + pairsFound + "/" + totalPairs);

            int[] first = getCardChoice("Select first card: ");
            if (first == null) {
                continue;
            }
            revealed[first[0]][first[1]] = true;
            printBoard();

            int[] second = getCardChoice("Select second card: ");
            if (second == null) {
                revealed[first[0]][first[1]] = false;
                continue;
            }
            revealed[second[0]][second[1]] = true;
            printBoard();

            turns++;

            if (cards[first[0]][first[1]] == cards[second[0]][second[1]]) {
                System.out.println("Match found!");
                pairsFound++;
            } else {
                System.out.println("No match. Try to remember!");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                revealed[first[0]][first[1]] = false;
                revealed[second[0]][second[1]] = false;
            }
        }

        printBoard();
        System.out.println("Congratulations! You found all pairs in "
            + turns + " turns!");
        // Score: fewer turns = higher score
        int score = Math.max(0, 100 - (turns - totalPairs) * 5);
        return Optional.of(score);
    }

    private void initGame() {
        cards = new char[ROWS][COLS];
        revealed = new boolean[ROWS][COLS];

        List<Character> deck = new ArrayList<>();
        for (int i = 0; i < (ROWS * COLS) / 2; i++) {
            deck.add(SYMBOLS[i]);
            deck.add(SYMBOLS[i]);
        }
        Collections.shuffle(deck);

        int idx = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                cards[r][c] = deck.get(idx++);
                revealed[r][c] = false;
            }
        }
    }

    private void printBoard() {
        System.out.print("  ");
        for (int c = 1; c <= COLS; c++) {
            System.out.print(c + " ");
        }
        System.out.println();

        for (int r = 0; r < ROWS; r++) {
            System.out.print((r + 1) + " ");
            for (int c = 0; c < COLS; c++) {
                if (revealed[r][c]) {
                    System.out.print(cards[r][c] + " ");
                } else {
                    System.out.print("* ");
                }
            }
            System.out.println();
        }
    }

    private int[] getCardChoice(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+");

            if (parts.length != 2) {
                System.out.println("Enter as 'row col' (e.g., '1 2')");
                return null;
            }

            try {
                int row = Integer.parseInt(parts[0]) - 1;
                int col = Integer.parseInt(parts[1]) - 1;

                if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
                    System.out.println("Invalid position.");
                    return null;
                }
                if (revealed[row][col]) {
                    System.out.println("Card already revealed.");
                    return null;
                }
                return new int[]{row, col};
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                return null;
            }
        }
    }
}
