package com.ridango.game;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class CocktailGame {
    private int highScore = 0;
    private int currentScore = 0;
    private Set<String> usedCocktails = new HashSet<>();
    private CocktailService cocktailService;

    public CocktailGame() {
        this.cocktailService = new CocktailService();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;

        while (playAgain) {
            currentScore = 0; // Reset current score for each game session
            playGame(scanner);
            System.out.println("Game over! Your final score is: " + highScore);
//            System.out.println("Highest score: " + highScore);

            System.out.print("Do you want to play again? (yes/no): ");
            playAgain = scanner.nextLine().equalsIgnoreCase("yes");
        }

        scanner.close();
    }

    private void playGame(Scanner scanner) {
        int attempts;
        boolean guessedCorrectly;

        while (true) {
            attempts = 5; // Reset attempts for each new cocktail
            guessedCorrectly = false;

            Cocktail cocktail = cocktailService.fetchRandomCocktail();
            if (cocktail == null) {
                System.out.println("Error fetching cocktail data. Please try again.");
                return;
            }

            while (usedCocktails.contains(cocktail.getName())) {
                cocktail = cocktailService.fetchRandomCocktail();
            }
            usedCocktails.add(cocktail.getName());

            String hiddenName = cocktail.getName().replaceAll("[a-zA-Z]", "_");
            System.out.println("Guess the cocktail: " + hiddenName);
            System.out.println("Instructions: " + cocktail.getInstructions());

            while (attempts > 0 && !guessedCorrectly) {
                System.out.print("Enter your guess: ");
                String guess = scanner.nextLine();

                if (guess.equalsIgnoreCase(cocktail.getName())) {
                    guessedCorrectly = true;
                    currentScore += attempts;
                    System.out.println("Correct! Your score is now: " + currentScore);
                    System.out.println("Attempts left: " + attempts); // Show attempts left
                    break; // Exit the loop to start a new cocktail
                } else {
                    attempts--;
                    hiddenName = revealLetters(hiddenName, cocktail.getName(), 1);
                    System.out.println("Wrong guess. Attempts left: " + attempts);
                    System.out.println("Revealed letters: " + hiddenName);
                    displayCocktailHints(cocktail);
                }
            }

            if (!guessedCorrectly && attempts == 0) {
                System.out.println("You've used all attempts. The cocktail was: " + cocktail.getName());
                if (currentScore > highScore) {
                    highScore = currentScore; // Update high score if current score is higher
                }
                currentScore = 0; // Reset score after game over
                break; // End the game session
            }
        }
    }

    private String revealLetters(String hiddenName, String actualName, int lettersToReveal) {
        char[] hiddenChars = hiddenName.toCharArray();
        char[] actualChars = actualName.toCharArray();
        Random random = new Random();

        for (int i = 0; i < lettersToReveal; i++) {
            int randomIndex;
            do {
                randomIndex = random.nextInt(actualName.length());
            } while (hiddenChars[randomIndex] != '_');

            hiddenChars[randomIndex] = actualChars[randomIndex];
        }

        return new String(hiddenChars);
    }

    private void displayCocktailHints(Cocktail cocktail) {
        System.out.println("Category: " + cocktail.getCategory());
        System.out.println("Glass: " + cocktail.getGlass());
        System.out.println("Ingredients: " + cocktail.getIngredients());
        System.out.println("Image URL: " + cocktail.getImageUrl());
    }
}
