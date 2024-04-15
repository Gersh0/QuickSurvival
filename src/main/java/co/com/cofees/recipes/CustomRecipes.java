package co.com.cofees.recipes;

import co.com.cofees.tools.Keys;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.Map;

public class CustomRecipes {

    public static void registerCustomCrafting() {
        backpacks();
        waystones();
        enderGems();
        warpScrolls();
        regionScrolls();
    }

    private static void backpacks() {
        registerBackpackRecipe(Map.of('H', Material.CHEST, 'C', Material.LEATHER, 'S', Material.STRING));
        registerBackpackRecipe(2, Map.of('H', Material.IRON_INGOT), "HHH", "HMH", "HHH", Keys.BACKPACKLV1);
        registerBackpackRecipe(3, Map.of('O', Material.GOLD_INGOT), "OOO", "OMO", "OOO", Keys.BACKPACKLV2);
        registerBackpackRecipe(4, Map.of('D', Material.DIAMOND), "DDD", "DMD", "DDD", Keys.BACKPACKLV3);
        registerBackpackRecipe(5, Map.of('N', Material.NETHERITE_INGOT), "   ", "NM ", "   ", Keys.BACKPACKLV4);
    }

    private static void registerBackpackRecipe(Map<Character, Material> materials) {
        BackpackRecipe backpack = new BackpackRecipe(1, materials, new String[]{"CCC", "SHS", "CCC"});
        backpack.registerRecipe();
    }

    private static void registerBackpackRecipe(int level, Map<Character, Material> materials, String top, String middle, String bottom, NamespacedKey key) {
        BackpackRecipe backpack = new BackpackRecipe(level, materials, new String[]{top, middle, bottom});
        backpack.registerRecipe('M', RecipesCustom.createChoice(Material.CLOCK, key));
    }

    private static void enderGems() {
        Map<Character, Material> ingredients = Map.of('E', Material.ENDER_EYE, 'P', Material.ENDER_PEARL);
        String[] shape = {" E ", "EPE", " E "};
        EnderGemRecipe enderGemRecipe = new EnderGemRecipe(ingredients, shape);
        enderGemRecipe.registerRecipe();
    }

    private static void warpScrolls() {
        Map<Character, Material> ingredients = Map.of('E', Material.ENDER_PEARL, 'P', Material.PAPER);
        String[] shape = {" P ", "PEP", " P "};
        WarpScrollRecipe warpScrollRecipe = new WarpScrollRecipe(ingredients, shape);
        warpScrollRecipe.registerRecipe();
    }

    private static void waystones() {
        Map<Character, Material> ingredients = Map.of('B', Material.STONE_BRICKS, 'G', Material.ENDER_PEARL, 'O', Material.OBSIDIAN);
        String[] shape = {" B ", "BGB", "OOO"};
        WaystoneRecipe waystoneRecipe = new WaystoneRecipe(ingredients, shape);
        waystoneRecipe.registerRecipe();
    }

    private static void regionScrolls() {
        Map<Character, Material> ingredients = Map.of('E', Material.ENDER_EYE, 'P', Material.PAPER, 'S', Material.STICK);
        String[] shape = {"SPS", "PEP", "SPS"};
        RegionScrollRecipe regionScrollRecipe = new RegionScrollRecipe(ingredients, shape);
        regionScrollRecipe.registerRecipe();
    }
}