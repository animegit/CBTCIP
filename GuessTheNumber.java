import java.util.*;

public class GuessTheNumberGame {
    private static final int MAX_ATTEMPTS = 10;
    private static final int MAX_ROUNDS = 3;

    private static int score = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        for (int round = 1; round <= MAX_ROUNDS; round++) {
            System.out.println("Round " + round);
            
            int target = random.nextInt(100) + 1;
            System.out.println("\nI'm thinking of a number between 1 and 100.\n");
            System.out.println("You only have 10 attempts to guess the number....\n");

            for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
                System.out.print("Attempt " + attempt + ": Enter your guess: ");
                int userGuess = scanner.nextInt();

                try {
                    validate(userGuess);

                    if (userGuess == target) {
                        System.out.println("Congratulations! You guessed the correct number.");
                        System.out.println("Moving to the next Round...\n");
                        score += MAX_ATTEMPTS - attempt + 1;
                        break;
                    } else if (userGuess < target) {
                        System.out.println("Try a higher number.\n");
                    } else {
                        System.out.println("Try a lower number.\n");
                    }
                } catch (@SuppressWarnings("unused") InvalidException e) {
                    System.out.println("Invalid guess. Please enter a number between 1 and 100.\n");
                }
            }
        }

        System.out.println("Game Over!");
        System.out.println("Your total score is: " + score);
    }

    private static void validate(int guess) throws InvalidException {
        if (guess < 1 || guess > 100) {
            throw new InvalidException();
        }
    }
}

// Custom exception class with annotation
class InvalidException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid guess. Please enter a number between 1 and 100.";
    }
}
