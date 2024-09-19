package com.ridango.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;

public class CocktailGameManager {
    private int attempts;
    private int currentScore;
    @Getter
    private int highScore;
    private List<Cocktail> cocktailList;
    private Cocktail currentCocktail;

    // Constructor
    public CocktailGameManager(List<Cocktail> cocktailList) {
        this.cocktailList = new ArrayList<>(cocktailList);
        this.highScore = 0;
        this.currentScore = 0;
        resetForNextRound();
    }


    public Cocktail getNextCocktail() {
        if (cocktailList.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(cocktailList.size());
        currentCocktail = cocktailList.remove(index);
        return currentCocktail;
    }


    public GameState takeGuess(String guess) {
        boolean correct = guess.equalsIgnoreCase(currentCocktail.getName());
        if (correct) {
            currentScore += attempts; //adding remaining attempts to score
            updateHighScore();
            resetForNextRound();
        } else {
            attempts--;
            if (attempts <= 0) {
                attempts = 0; // to be sure that after my attempts are 0, I am not able to continue
                updateHighScore();
            }
        }
        return new GameState(currentScore, highScore, attempts, correct, currentCocktail);
    }


    public void resetForNextRound() {
        attempts = 5; // Reset attempts
    }


    public void updateHighScore() {
        if (currentScore > highScore) {
            highScore = currentScore;
        }
    }


    public String revealLetters(String hiddenName, int lettersToReveal) {
        char[] hiddenChars = hiddenName.toCharArray();
        char[] actualChars = currentCocktail.getName().toCharArray();
        Random random = new Random();

        for (int i = 0; i < lettersToReveal; i++) {
            int randomIndex;
            do {
                randomIndex = random.nextInt(actualChars.length);
            } while (hiddenChars[randomIndex] != '_');

            hiddenChars[randomIndex] = actualChars[randomIndex];
        }

        return new String(hiddenChars);
    }

    // Class to store game information
    @Getter
    @Setter
    public class GameState {
        private final int currentScore;
        private final int highScore;
        private final int remainingAttempts;
        private final boolean isCorrect;
        private final Cocktail currentCocktail;


        public GameState(int currentScore, int highScore, int remainingAttempts, boolean isCorrect, Cocktail currentCocktail) {
            this.currentScore = currentScore;
            this.highScore = highScore;
            this.remainingAttempts = remainingAttempts;
            this.isCorrect = isCorrect;
            this.currentCocktail = currentCocktail;
        }
    }
}