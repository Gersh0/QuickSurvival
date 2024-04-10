package co.com.cofees.recipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Map;

public abstract class RecipesCustom implements CustomRecipe {
    protected NamespacedKey key;
    protected ItemStack result;
    protected Map<Character, Material> ingredients;
    protected String[] shape;

    // constructor, getters, setters omitted for brevity

    @Override
    public void registerRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(shape);
        ingredients.forEach(recipe::setIngredient);
        Bukkit.addRecipe(recipe);
    }

    @Override
    public void registerRecipe(char c, RecipeChoice.MaterialChoice materialChoice) {
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(shape);
        ingredients.forEach(recipe::setIngredient);
        recipe.setIngredient(c, materialChoice);
        Bukkit.addRecipe(recipe);
    }
}
