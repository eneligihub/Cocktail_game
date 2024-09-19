package com.ridango;

import com.ridango.game.Cocktail;
import com.ridango.game.CocktailGameManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CocktailGameApplicationTests {

    private CocktailGameManager gameManager;
    private Cocktail mockCocktail1;

    @BeforeEach
    public void setUp() {
        // For my test I am creating a mock cocktail
        mockCocktail1 = Mockito.mock(Cocktail.class);
        when(mockCocktail1.getName()).thenReturn("Margarita");
        List<Cocktail> cocktailList = Arrays.asList(mockCocktail1);
        gameManager = new CocktailGameManager(cocktailList);
    }

    @Test
    public void testGetNextCocktail() {
        Cocktail cocktail = gameManager.getNextCocktail();
        assertNotNull(cocktail);
        assertEquals("Margarita", cocktail.getName());
    }

    @Test
    public void testCorrectGuess() {
        gameManager.getNextCocktail();
        CocktailGameManager.GameState gameState = gameManager.takeGuess("Margarita");
        assertTrue(gameState.isCorrect());
        assertEquals(5, gameState.getCurrentScore());
    }

    @Test
    public void testIncorrectGuess() {
        gameManager.getNextCocktail();
        CocktailGameManager.GameState gameState = gameManager.takeGuess("WrongName");
        assertFalse(gameState.isCorrect());
        assertEquals(4, gameState.getRemainingAttempts());
    }

    @Test
    public void testAttemptsDecreaseOnWrongGuess() {
        gameManager.getNextCocktail();
        gameManager.takeGuess("WrongGuess1");
        gameManager.takeGuess("WrongGuess2");
        CocktailGameManager.GameState gameState = gameManager.takeGuess("WrongGuess3");
        assertEquals(2, gameState.getRemainingAttempts());
    }

    @Test
    public void testNoAttemptsLeft() {
        gameManager.getNextCocktail();
        for (int i = 0; i < 5; i++) {
            gameManager.takeGuess("WrongGuess");
        }
        CocktailGameManager.GameState gameState = gameManager.takeGuess("WrongGuess");
        assertEquals(0, gameState.getRemainingAttempts());

    }
    @Test
    public void testRevealLetters() {
        Cocktail realCocktail = new Cocktail(
                "Margarita",
                "Shake all ingredients with ice and strain into a cocktail glass.",
                "Cocktail",
                "Cocktail Glass",
                "Tequila, Lime Juice, Triple Sec",
                "https://example.com/margarita.jpg"
        );
        List<Cocktail> cocktailList = Arrays.asList(realCocktail);
        gameManager = new CocktailGameManager(cocktailList);
        gameManager.getNextCocktail();
        String hiddenName = "_________"; //Margarita
        String revealed = gameManager.revealLetters(hiddenName, 2);

        // Ensure that at least two letters are revealed
        assertNotEquals(hiddenName, revealed);
        assertEquals(9 - 2, countUnderscores(revealed));
    }

    private int countUnderscores(String str) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == '_') {
                count++;
            }
        }
        return count;
    }
}
