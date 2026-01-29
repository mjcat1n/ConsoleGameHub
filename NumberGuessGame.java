import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

/**
 * A simple guessing game where the computer selects a number in a
 * given range (for example, 1 to 100),
 * and the player must guess the number in as few attempts as possible.
 * Feedback is given after each guess (e.g., "Too high", "Too low").
 * <pre>
 * The score can be calculated based on number of attempts or time taken.
 * </pre>
 * @version 1
 */
class NumberGuessGame implements Game {
    /** Maximum number to guess. */
    private static final int MAX_NUMBER = 100;
    /** Maximum attempts allowed. */
    private static final int MAX_ATTEMPTS = 10;
    /** Console input. */
    private final Scanner scanner = new Scanner(System.in);
    /** Random number generator. */
    private final Random random = new Random();

    /**
     * Gets the string name.
     * @return Number Guess.
     */
    public String getName() {
        return "Number Guess";
    }

    /**
     * Starts the game and plays until win or loss.
     * @return score based on remaining attempts.
     */
    public Optional<Integer> play() {
        System.out.println("[Playing Number Guessing Game]");
        System.out.println("This is a simple game where you"
            + " try and guess the number I pick.");
        System.out.println("If you are too high or too low I will let"
            + " you know. Try to do it in the fewest attempts!");
        System.out.println("I'm thinking of a number between 1 and "
            + MAX_NUMBER + ".");
        System.out.println("You have " + MAX_ATTEMPTS + " attempts.");

        int target = random.nextInt(MAX_NUMBER) + 1;
        int attemptsLeft = MAX_ATTEMPTS;

        while (attemptsLeft > 0) {
            System.out.print("Enter your guess (attempts left: "
                + attemptsLeft + "): ");
            String input = scanner.nextLine().trim();

            int guess;
            try {
                guess = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            if (guess < 1 || guess > MAX_NUMBER) {
                System.out.println("Please guess between 1 and " + MAX_NUMBER);
                continue;
            }

            if (guess == target) {
                System.out.println("Congratulations! You guessed it!");
                return Optional.of(attemptsLeft);
            } else if (guess < target) {
                System.out.println("Too low!");
            } else {
                System.out.println("Too high!");
            }

            attemptsLeft--;
        }

        System.out.println("Out of attempts! The number was: " + target);
        return Optional.of(0);
    }
}
