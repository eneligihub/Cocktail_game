package com.ridango.game;

public class Cocktail {
    private String name;
    private String instructions;
    private String category;
    private String glass;
    private String ingredients;
    private String imageUrl;


    public Cocktail(String name, String instructions, String category, String glass, String ingredients, String imageUrl) {
        this.name = name;
        this.instructions = instructions;
        this.category = category;
        this.glass = glass;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getCategory() {
        return category;
    }

    public String getGlass() {
        return glass;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}