import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 * A traditional Hangman game.
 * The player attempts to guess a secret word by guessing letters
 * one at a time.
 * Each incorrect guess reduces the number of remaining tries.
 * <pre>
 * Implements visual feedback (e.g., ASCII scaffold).
 * Handles duplicate guesses and win/loss conditions.
 * </pre>
 * @version 1
 */
class HangmanGame implements Game {
    /** Maximum wrong guesses allowed. */
    private static final int MAX_WRONG = 6;
    /** Word list to choose from. */
    private static final String[] WORDS = {
        "JAVA", "PYTHON", "COMPUTER", "HANGMAN", "PROGRAMMING",
        "KEYBOARD", "MONITOR", "SOFTWARE", "DEVELOPER", "ALGORITHM"
    };
    /** Console input. */
    private final Scanner scanner = new Scanner(System.in);
    /** Random for word selection. */
    private final Random random = new Random();

    @Override
    public String getName() {
        return "Hangman";
    }

    @Override
    public Optional<Integer> play() {
        System.out.println("Welcome to Hangman!");
        System.out.println("Guess the hidden word one letter at a time.");
        System.out.println("Each wrong guess reduces your number of tries.");

        String word = WORDS[random.nextInt(WORDS.length)];
        Set<Character> guessedLetters = new HashSet<>();
        int wrongGuesses = 0;

        while (wrongGuesses < MAX_WRONG) {
            printHangman(wrongGuesses);
            String masked = getMaskedWord(word, guessedLetters);
            System.out.println("Word: " + masked);
            System.out.println("Guessed: " + guessedLetters);
            System.out.println("Wrong guesses: " + wrongGuesses + "/" + MAX_WRONG);

            if (!masked.contains("_")) {
                System.out.println("Congratulations! You guessed the word: "
                    + word);
                return Optional.of(MAX_WRONG - wrongGuesses);
            }

            System.out.print("Enter a letter: ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                System.out.println("Please enter a single letter.");
                continue;
            }

            char letter = input.charAt(0);
            if (guessedLetters.contains(letter)) {
                System.out.println("You already guessed that letter.");
                continue;
            }

            guessedLetters.add(letter);

            if (word.indexOf(letter) >= 0) {
                System.out.println("Correct!");
            } else {
                System.out.println("Wrong!");
                wrongGuesses++;
            }
        }

        printHangman(wrongGuesses);
        System.out.println("Game over! The word was: " + word);
        return Optional.of(0);
    }

    private String getMaskedWord(String word, Set<Character> guessed) {
        StringBuilder sb = new StringBuilder();
        for (char c : word.toCharArray()) {
            if (guessed.contains(c)) {
                sb.append(c);
            } else {
                sb.append('_');
            }
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    private void printHangman(int wrong) {
        String[] stages = {
            "  +---+\n  |   |\n      |\n      |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n      |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n  |   |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n /|   |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n /|\\  |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n /|\\  |\n /    |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n /|\\  |\n / \\  |\n      |\n========="
        };
        System.out.println(stages[wrong]);
    }
}
