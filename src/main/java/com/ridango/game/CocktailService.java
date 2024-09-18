package com.ridango.game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CocktailService {
    private static final String API_URL = "https://www.thecocktaildb.com/api/json/v1/1/random.php";

    public Cocktail fetchRandomCocktail() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            conn.disconnect();

            return parseCocktailFromJson(content.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Cocktail parseCocktailFromJson(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonObject drink = jsonObject.getAsJsonArray("drinks").get(0).getAsJsonObject();

        String name = drink.get("strDrink").getAsString();
        String instructions = drink.get("strInstructions").getAsString();
        String category = drink.get("strCategory").getAsString();
        String glass = drink.get("strGlass").getAsString();

        StringBuilder ingredientsBuilder = new StringBuilder();
        for (int i = 1; i <= 15; i++) {
            String ingredient = drink.get("strIngredient" + i).isJsonNull() ? "" : drink.get("strIngredient" + i).getAsString();
            if (!ingredient.isEmpty()) {
                ingredientsBuilder.append(ingredient).append(", ");
            }
        }
        String ingredients = ingredientsBuilder.toString();
        if (!ingredients.isEmpty()) {
            ingredients = ingredients.substring(0, ingredients.length() - 2);
        }

        String imageUrl = drink.get("strDrinkThumb").getAsString();

        return new Cocktail(name, instructions, category, glass, ingredients, imageUrl);
    }
}
