package com.ridango.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CocktailGame {
    private CocktailGameManager gameManager;
    private CocktailService cocktailService;

    public CocktailGame() {
        this.cocktailService = new CocktailService();
        List<Cocktail> cocktails = fetchCocktailsForGame();
        this.gameManager = new CocktailGameManager(cocktails);
    }

    private List<Cocktail> fetchCocktailsForGame() {
        List<Cocktail> cocktails = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Cocktail cocktail = cocktailService.fetchRandomCocktail();
            if (cocktail != null) {
                cocktails.add(cocktail);
            }
        }
        return cocktails;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;

        while (playAgain) {
            gameManager.resetForNextRound();
            playGame(scanner);

            System.out.println("Game over! Your final score is: " + gameManager.getHighScore());
            System.out.print("Do you want to play again? (yes/no): ");
            playAgain = scanner.nextLine().equalsIgnoreCase("yes");
        }

        scanner.close();
    }

    private void playGame(Scanner scanner) {
        boolean guessedCorrectly;
        while (true) {
            Cocktail cocktail = gameManager.getNextCocktail();
            if (cocktail == null) {
                System.out.println("No more cocktails available.");
                break;
            }

            String hiddenName = cocktail.getName().replaceAll("[a-zA-Z]", "_");
            System.out.println("Guess the cocktail: " + hiddenName);
            System.out.println("Instructions: " + cocktail.getInstructions());

            guessedCorrectly = false;
            while (true) {
                System.out.print("Enter your guess: ");
                String guess = scanner.nextLine();

                CocktailGameManager.GameState gameState = gameManager.takeGuess(guess);
                guessedCorrectly = gameState.isCorrect();

                if (guessedCorrectly) {
                    System.out.println("Correct! Your score is now: " + gameState.getCurrentScore());
                    break;
                } else {
                    hiddenName = gameManager.revealLetters(hiddenName, 1);
                    System.out.println("Wrong guess. Attempts left: " + gameState.getRemainingAttempts());
                    System.out.println("Revealed letters: " + hiddenName);
                    displayCocktailHints(cocktail);
                }


                if (gameState.getRemainingAttempts() <= 0) {
                    System.out.println("You've used all attempts. The cocktail was: " + cocktail.getName());
                    gameManager.updateHighScore();
                    break;
                }
            }

            if (!guessedCorrectly) {

                break;
            }
        }
    }

    private void displayCocktailHints(Cocktail cocktail) {
        System.out.println("Category: " + cocktail.getCategory());
        System.out.println("Glass: " + cocktail.getGlass());
        System.out.println("Ingredients: " + cocktail.getIngredients());
        System.out.println("Image URL: " + cocktail.getImageUrl());
    }
}
