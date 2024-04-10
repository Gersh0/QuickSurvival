package co.com.cofees.recipes;

import org.bukkit.inventory.RecipeChoice;

public interface CustomRecipe {
    void registerRecipe();

    void registerRecipe(char c, RecipeChoice.MaterialChoice materialChoice);
}