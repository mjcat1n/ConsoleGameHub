import java.util.Optional;
import java.util.Scanner;

/**
 * A word guessing game similar to Wordle.
 * The player has a limited number of attempts to guess a secret
 * 5-letter word.
 * After each guess, the game indicates whether the guess is correct.
 * <br />
 * The score is determined by how many attempts the player had remaining
 *   when they guessed the word correctly.
 * @version 1
 */
class WordGuessGame implements Game {
    /** The secret word to guess. */
    private static final String SECRET_WORD = "APPLE";
    /** Maximum number of guesses allowed. */
    private static final int MAX_GUESSES = 6;
    /** Number of letters in a word. */
    private static final int WORD_LENGTH = 5;
    /** Console input. */
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getName() {
        return "Word Guess";
    }

    @Override
    public Optional<Integer> play() {
        System.out.println(
            "[Playing Word Guess - You will have a limited number of attempts"
            + " to guess a secret 5 letter word.]"
        );
        System.out.println(
            "After each guess, the game will indicate whether the guess is"
            + " correct."
        );
        System.out.println(
            "Your score is determined by the number of attempts remaining"
            + " after you guessed the word correctly!"
        );

        int attemptsLeft = MAX_GUESSES;

        while (attemptsLeft > 0) {
            System.out.print("Enter guess: ");
            String guess = scanner.nextLine().trim().toUpperCase();

            if (guess.length() != WORD_LENGTH || !guess.matches("[A-Z]+")) {
                System.out.println("Invalid input. Please enter a "
                    + WORD_LENGTH + "-letter word with alphabetic "
                    + "characters only.");
                continue;
            }

            if (guess.equals(SECRET_WORD)) {
                System.out.println("Congratulations! You guessed the word!");
                return Optional.of(attemptsLeft);
            } else {
                attemptsLeft--;
                System.out.println("Incorrect! Attempts remaining: "
                    + attemptsLeft);
            }
        }

        System.out.println("You ran out of guesses. The word was: "
            + SECRET_WORD);
        return Optional.of(0);
    }
}
