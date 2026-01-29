import java.util.LinkedList;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

/**
 * A simplified console version of the classic Snake game.
 * The player controls a "snake" that moves around a grid, collecting food
 *   and growing in length.
 * The game ends if the snake runs into itself or the edge of the grid.
 * <pre>
 * Simulate the game board with a 2D array.
 * Display the game using text-based output.
 * </pre>
 * @version 1
 */
class SnakeGame implements Game {
    /** Board width. */
    private static final int WIDTH = 15;
    /** Board height. */
    private static final int HEIGHT = 10;
    /** Console input. */
    private final Scanner scanner = new Scanner(System.in);
    /** Random for food placement. */
    private final Random random = new Random();
    /** Snake body positions. */
    private LinkedList<int[]> snake;
    /** Current direction. */
    private int dirX;
    /** Current direction. */
    private int dirY;
    /** Food position. */
    private int foodX;
    /** Food position. */
    private int foodY;
    /** Game score. */
    private int score;

    @Override
    public String getName() {
        return "Snake";
    }

    @Override
    public Optional<Integer> play() {
        System.out.println("Welcome to Snake!");
        System.out.println("Objective: Eat food (*) to grow and score points!");
        System.out.println("Controls: W=Up, S=Down, A=Left, D=Right, Q=Quit");
        System.out.println("Avoid walls (#) and your own tail (o)!");

        initGame();

        while (true) {
            printBoard();
            System.out.println("Score: " + score);
            System.out.print("Enter direction (W/A/S/D) or Q to quit: ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("Q")) {
                System.out.println("Thanks for playing! Final score: " + score);
                return Optional.of(score);
            }

            if (!input.isEmpty()) {
                char dir = input.charAt(0);
                switch (dir) {
                    case 'W':
                        if (dirY != 1) {
                            dirX = 0; dirY = -1;
                        }
                        break;
                    case 'S':
                        if (dirY != -1) {
                            dirX = 0; dirY = 1;
                        }
                        break;
                    case 'A':
                        if (dirX != 1) {
                            dirX = -1; dirY = 0;
                        }
                        break;
                    case 'D':
                        if (dirX != -1) {
                            dirX = 1; dirY = 0;
                        }
                        break;
                    default:
                        break;
                }
            }

            if (!moveSnake()) {
                printBoard();
                System.out.println("Game Over! Final score: " + score);
                return Optional.of(score);
            }
        }
    }

    private void initGame() {
        snake = new LinkedList<>();
        int startX = WIDTH / 2;
        int startY = HEIGHT / 2;
        snake.add(new int[]{startX, startY});
        snake.add(new int[]{startX - 1, startY});
        snake.add(new int[]{startX - 2, startY});
        dirX = 1;
        dirY = 0;
        score = 0;
        placeFood();
    }

    private void placeFood() {
        while (true) {
            foodX = random.nextInt(WIDTH);
            foodY = random.nextInt(HEIGHT);
            boolean onSnake = false;
            for (int[] seg : snake) {
                if (seg[0] == foodX && seg[1] == foodY) {
                    onSnake = true;
                    break;
                }
            }
            if (!onSnake) {
                break;
            }
        }
    }

    private boolean moveSnake() {
        int[] head = snake.getFirst();
        int newX = head[0] + dirX;
        int newY = head[1] + dirY;

        // Check wall collision
        if (newX < 0 || newX >= WIDTH || newY < 0 || newY >= HEIGHT) {
            return false;
        }

        // Check self collision
        for (int[] seg : snake) {
            if (seg[0] == newX && seg[1] == newY) {
                return false;
            }
        }

        // Add new head
        snake.addFirst(new int[]{newX, newY});

        // Check food
        if (newX == foodX && newY == foodY) {
            score += 10;
            placeFood();
        } else {
            snake.removeLast();
        }

        return true;
    }

    private void printBoard() {
        // Top border
        for (int i = 0; i < WIDTH + 2; i++) {
            System.out.print("#");
        }
        System.out.println();

        for (int y = 0; y < HEIGHT; y++) {
            System.out.print("#");
            for (int x = 0; x < WIDTH; x++) {
                char c = ' ';
                if (x == foodX && y == foodY) {
                    c = '*';
                } else {
                    boolean isHead = true;
                    for (int[] seg : snake) {
                        if (seg[0] == x && seg[1] == y) {
                            c = isHead ? '@' : 'o';
                            break;
                        }
                        isHead = false;
                    }
                }
                System.out.print(c);
            }
            System.out.println("#");
        }

        // Bottom border
        for (int i = 0; i < WIDTH + 2; i++) {
            System.out.print("#");
        }
        System.out.println();
    }
}
