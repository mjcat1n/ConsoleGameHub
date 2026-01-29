import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

/**
 * A code-breaking game where the app selects a sequence of symbols, and
 * the player tries to guess the sequence within a fixed number of attempts.
 *
 * Feedback for each guess indicates how many values are correct and in
 * the correct position and how many are correct but in the wrong position.
 *
 * Consider using arrays or strings to store and compare codes.
 * @version 1
 */
class MasterMindGame implements Game {
    /** Code length. */
    private static final int CODE_LENGTH = 4;
    /** Number of possible colors/digits. */
    private static final int NUM_COLORS = 6;
    /** Maximum attempts. */
    private static final int MAX_ATTEMPTS = 10;
    /** Console input. */
    private final Scanner scanner = new Scanner(System.in);
    /** Random for code generation. */
    private final Random random = new Random();

    @Override
    public String getName() {
        return "MasterMind";
    }

    @Override
    public Optional<Integer> play() {
        System.out.println("Welcome to MasterMind!");
        System.out.println("I've chosen a secret code of " + CODE_LENGTH
            + " digits.");
        System.out.println("Each digit is between 1 and " + NUM_COLORS + ".");
        System.out.println("Guess the code within " + MAX_ATTEMPTS
            + " attempts.");
        System.out.println("Feedback: X = correct position, "
            + "O = wrong position");

        int[] secretCode = generateCode();
        int attemptsLeft = MAX_ATTEMPTS;

        while (attemptsLeft > 0) {
            System.out.println("\nAttempts remaining: " + attemptsLeft);
            System.out.print("Enter your guess (" + CODE_LENGTH
                + " digits, e.g., 1234): ");

            String input = scanner.nextLine().trim();

            if (input.length() != CODE_LENGTH) {
                System.out.println("Please enter exactly " + CODE_LENGTH
                    + " digits.");
                continue;
            }

            int[] guess = new int[CODE_LENGTH];
            boolean valid = true;
            for (int i = 0; i < CODE_LENGTH; i++) {
                char c = input.charAt(i);
                if (c < '1' || c > '0' + NUM_COLORS) {
                    System.out.println("Digits must be between 1 and "
                        + NUM_COLORS);
                    valid = false;
                    break;
                }
                guess[i] = c - '0';
            }

            if (!valid) {
                continue;
            }

            int[] result = evaluateGuess(secretCode, guess);
            int correctPosition = result[0];
            int correctValue = result[1];

            StringBuilder feedback = new StringBuilder();
            for (int i = 0; i < correctPosition; i++) {
                feedback.append("X");
            }
            for (int i = 0; i < correctValue; i++) {
                feedback.append("O");
            }
            if (feedback.length() == 0) {
                feedback.append("-");
            }

            System.out.println("Feedback: " + feedback);

            if (correctPosition == CODE_LENGTH) {
                System.out.println("Congratulations! You cracked the code!");
                return Optional.of(attemptsLeft);
            }

            attemptsLeft--;
        }

        StringBuilder code = new StringBuilder();
        for (int d : secretCode) {
            code.append(d);
        }
        System.out.println("Out of attempts! The code was: " + code);
        return Optional.of(0);
    }

    private int[] generateCode() {
        int[] code = new int[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; i++) {
            code[i] = random.nextInt(NUM_COLORS) + 1;
        }
        return code;
    }

    private int[] evaluateGuess(int[] secret, int[] guess) {
        int correctPosition = 0;
        int correctValue = 0;

        boolean[] secretUsed = new boolean[CODE_LENGTH];
        boolean[] guessUsed = new boolean[CODE_LENGTH];

        // First pass: correct position
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (guess[i] == secret[i]) {
                correctPosition++;
                secretUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        // Second pass: correct value, wrong position
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (!guessUsed[i]) {
                for (int j = 0; j < CODE_LENGTH; j++) {
                    if (!secretUsed[j] && guess[i] == secret[j]) {
                        correctValue++;
                        secretUsed[j] = true;
                        break;
                    }
                }
            }
        }

        return new int[]{correctPosition, correctValue};
    }
}
