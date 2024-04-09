package co.com.cofees.recipes;

import co.com.cofees.tools.Keys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CustomRecipes {

    public static void registerCustomCrafting() {
        backpacks();
        waystones();
    }

    public static ItemStack createCustomItem(Material material, String displayName, String lore, NamespacedKey key, String keyValue, int amount) {
        // Crear el ítem
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();

        // Configurar el nombre visible del ítem

        Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.BOLD + displayName);

        // Configurar la persistencia de datos
        if (key != null && keyValue != null) {
            itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, keyValue);
        }

        // Configurar el lore del ítem
        if (lore != null && !lore.isEmpty()) {
            itemMeta.setLore(List.of(lore));
        }

        // Establecer la meta del ítem
        item.setItemMeta(itemMeta);

        return item;
    }


    // NO ESTA TERMINADO
    public static void addCustomRecipe(ItemStack itemStack, NamespacedKey key, String shape1, String shape2, String shape3, HashMap<Character, Material> ingredientMap) {
        ShapedRecipe backpack = new ShapedRecipe(key, itemStack);
        backpack.shape(shape1, shape2, shape3); //Controlar casos que no se puedan
        backpack.setIngredient('H', Material.CHEST);//1
        backpack.setIngredient('C', Material.LEATHER);//2
        backpack.setIngredient('S', Material.STRING);//3  esos 3 se pueden recibir en un hashmap
        // Registrar el crafteo
        Bukkit.addRecipe(backpack);
    }


    public static void backpacks() {
        // Crear el ítem que se obtendrá al realizar el crafteo
        ItemStack backpackLv1 = new ItemStack(Material.CLOCK);
        ItemMeta backpackLv1Meta = backpackLv1.getItemMeta();
        backpackLv1Meta.setDisplayName(ChatColor.BOLD + "Backpack lv1");

        backpackLv1Meta.getPersistentDataContainer().set(Keys.BACKPACKLV1, PersistentDataType.STRING, "true");
        backpackLv1Meta.getPersistentDataContainer().set(Keys.BACKPACK_CODE, PersistentDataType.STRING, "true");
        backpackLv1Meta.setLore(List.of("UNA MOCHILA EN MAICRA?"));
        backpackLv1.setAmount(1);
        //setear meta
        backpackLv1Meta.setCustomModelData(1);
        backpackLv1.setItemMeta(backpackLv1Meta);


        // Crear el crafteo personalizado
        ShapedRecipe bl1 = new ShapedRecipe(Keys.BACKPACKLV1Recipe, backpackLv1);
        bl1.shape("CCC", "SHS", "CCC");
        bl1.setIngredient('H', Material.CHEST);
        bl1.setIngredient('C', Material.LEATHER);
        bl1.setIngredient('S', Material.STRING);
        bl1.setCategory(CraftingBookCategory.EQUIPMENT);

        // Registrar el crafteo
        Bukkit.addRecipe(bl1);

        //backpack lv2

        // Crear el ítem que se obtendrá al realizar el crafteo
        ItemStack backpackLv2 = new ItemStack(Material.CLOCK);
        ItemMeta backpackLv2Meta = backpackLv2.getItemMeta();
        backpackLv2Meta.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Backpack lv2");

        backpackLv2Meta.getPersistentDataContainer().set(Keys.BACKPACKLV2, PersistentDataType.STRING, "true");

        backpackLv2Meta.setLore(List.of("UNA MOCHILA MAS GRANDE"));
        backpackLv2.setAmount(1);
        //setear meta
        backpackLv2Meta.setCustomModelData(2);
        backpackLv2.setItemMeta(backpackLv2Meta);


        // Crear el crafteo personalizado
        ShapedRecipe bl2 = new ShapedRecipe(Keys.BACKPACKLV2Recipe, backpackLv2);
        bl2.shape("HHH", "HMH", "HHH");
        bl2.setIngredient('H', Material.IRON_INGOT);
        bl2.setIngredient('M', new RecipeChoice.MaterialChoice(Material.CLOCK) {
            @Override
            public boolean test(@Nullable ItemStack itemStack) {
                return canCraftLv2(itemStack);
            }
        });
        bl2.setCategory(CraftingBookCategory.EQUIPMENT);


        // Registrar el crafteo
        Bukkit.addRecipe(bl2);

        //backpack lv3

        // Crear el ítem que se obtendrá al realizar el crafteo
        ItemStack backpackLv3 = new ItemStack(Material.CLOCK);
        ItemMeta backpackLv3Meta = backpackLv3.getItemMeta();
        backpackLv3Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        backpackLv3Meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        backpackLv3Meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Backpack lv3");

        backpackLv3Meta.getPersistentDataContainer().set(Keys.BACKPACKLV3, PersistentDataType.STRING, "true");
        backpackLv3Meta.setLore(List.of("UNA MOCHILA AUN MAS GRANDE"));
        backpackLv3.setAmount(1);
        //setear meta
        backpackLv3Meta.setCustomModelData(3);
        backpackLv3.setItemMeta(backpackLv3Meta);


        // Crear el crafteo personalizado
        ShapedRecipe bl3 = new ShapedRecipe(Keys.BACKPACKLV3Recipe, backpackLv3);
        bl3.shape("OOO", "OMO", "OOO");
        bl3.setIngredient('O', Material.GOLD_INGOT);
        bl3.setIngredient('M', new RecipeChoice.MaterialChoice(backpackLv2.getType()) {
            @Override
            public boolean test(@Nullable ItemStack itemStack) {
                if (itemStack == null) {
                    return false;
                }
                PersistentDataContainer container = Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer();
                return container.has(Keys.BACKPACKLV2, PersistentDataType.STRING);
            }
        });
        bl3.setCategory(CraftingBookCategory.EQUIPMENT);
        // Registrar el crafteo
        Bukkit.addRecipe(bl3);

        //backpack lv4

        // Crear el ítem que se obtendrá al realizar el crafteo
        ItemStack backpackLv4 = new ItemStack(Material.CLOCK);
        ItemMeta backpackLv4Meta = backpackLv4.getItemMeta();
        backpackLv4Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        backpackLv4Meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        backpackLv4Meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Backpack lv4");

        backpackLv4Meta.getPersistentDataContainer().set(Keys.BACKPACKLV4, PersistentDataType.STRING, "true");
        backpackLv4Meta.setLore(List.of("UNA MOCHILA INCLUSO MAS GRANDE? CUANDO TERMINA?"));
        backpackLv4.setAmount(1);
        //setear meta
        backpackLv4Meta.setCustomModelData(4);
        backpackLv4.setItemMeta(backpackLv4Meta);


        // Crear el crafteo personalizado
        ShapedRecipe bl4 = new ShapedRecipe(Keys.BACKPACKLV4Recipe, backpackLv4);
        bl4.shape("DDD", "DMD", "DDD");
        bl4.setIngredient('D', Material.DIAMOND);
        bl4.setIngredient('M', new RecipeChoice.MaterialChoice(Material.CLOCK) {
            @Override
            public boolean test(@Nullable ItemStack itemStack) {
                if (itemStack == null) {
                    return false;
                }
                PersistentDataContainer container = Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer();
                return container.has(Keys.BACKPACKLV3, PersistentDataType.STRING);
            }
        });
        bl4.setCategory(CraftingBookCategory.EQUIPMENT);
        // Registrar el crafteo
        Bukkit.addRecipe(bl4);

        //backpack lv4

        // Crear el ítem que se obtendrá al realizar el crafteo
        ItemStack backpackLv5 = new ItemStack(Material.CLOCK);
        ItemMeta backpackLv5Meta = backpackLv5.getItemMeta();
        backpackLv5Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        backpackLv5Meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        backpackLv5Meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.MAGIC + "Backpack lv5");

        backpackLv5Meta.getPersistentDataContainer().set(Keys.BACKPACKLV5, PersistentDataType.STRING, "true");
        backpackLv5Meta.setLore(List.of("LA MOCHILA DEFINITIVA"));
        backpackLv5.setAmount(1);
        //setear meta
        backpackLv5Meta.setCustomModelData(5);
        backpackLv5.setItemMeta(backpackLv5Meta);

        // Crear el crafteo personalizado
        ShapedRecipe bl5 = new ShapedRecipe(Keys.BACKPACKLV5Recipe, backpackLv5);
        bl5.shape("NM ");
        bl5.setIngredient('N', Material.NETHERITE_INGOT);
        bl5.setIngredient('M', new RecipeChoice.MaterialChoice(Material.CLOCK) {
            @Override
            public boolean test(@Nullable ItemStack itemStack) {
                if (itemStack == null) {
                    return false;
                }
                PersistentDataContainer container = Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer();
                return container.has(Keys.BACKPACKLV4, PersistentDataType.STRING);
            }
        });
        bl5.setCategory(CraftingBookCategory.EQUIPMENT);

        // Registrar el crafteo
        Bukkit.addRecipe(bl5);
    }

    public static void waystones () {
        //crear una ender pearl especial para la waystone
        ItemStack enderGem = new ItemStack(Material.ENDER_PEARL);
        ItemMeta enderGemMeta = enderGem.getItemMeta();
        enderGemMeta.setDisplayName(ChatColor.DARK_PURPLE + "Ender Gem");
        enderGemMeta.getPersistentDataContainer().set(Keys.ENDER_GEM, PersistentDataType.STRING, "true");
        enderGemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        enderGemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        enderGemMeta.setLore(List.of("A special ender pearl for waystones"));

        enderGem.setItemMeta(enderGemMeta);

        // Crear el crafteo personalizado
        ShapedRecipe enderGemRecipe = new ShapedRecipe(Keys.ENDER_GEM, enderGem);
        enderGemRecipe.shape(" E ", "EPE", " E ");
        enderGemRecipe.setIngredient('E', Material.ENDER_EYE);
        enderGemRecipe.setIngredient('P', Material.ENDER_PEARL);
        enderGemRecipe.setCategory(CraftingBookCategory.MISC);

        // Registrar el crafteo

        Bukkit.addRecipe(enderGemRecipe);


        // Crear el ítem que se obtendrá al realizar el crafteo
        ItemStack waystone = new ItemStack(Material.BLACK_BANNER);
        ItemMeta waystoneMeta = waystone.getItemMeta();
        waystoneMeta.setDisplayName("Waystone");

        waystoneMeta.getPersistentDataContainer().set(Keys.WAYSTONE, PersistentDataType.STRING, waystoneMeta.getDisplayName());
        waystoneMeta.setLore(List.of("Place this to create a waystone"));
        waystone.setAmount(1);
        waystoneMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        waystoneMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        //setear meta
        waystone.setItemMeta(waystoneMeta);

        // Crear el crafteo personalizado

        ShapedRecipe waystoneRecipe = new ShapedRecipe(Keys.WAYSTONE, waystone);
        waystoneRecipe.shape(" B ", "BGB", "OOO");
        waystoneRecipe.setIngredient('B', Material.STONE_BRICKS);
        //set ingredient to the ender gem
        waystoneRecipe.setIngredient('G', new RecipeChoice.ExactChoice(enderGem));
        waystoneRecipe.setIngredient('O', Material.OBSIDIAN);
        waystoneRecipe.setCategory(CraftingBookCategory.MISC);

        // Registrar el crafteo
        Bukkit.addRecipe(waystoneRecipe);

        //add a warp Scroll
        ItemStack warpScroll = new ItemStack(Material.PAPER);
        ItemMeta warpScrollMeta = warpScroll.getItemMeta();
        warpScrollMeta.setDisplayName(ChatColor.DARK_PURPLE + "Warp Scroll");
        warpScrollMeta.getPersistentDataContainer().set(Keys.WARP_SCROLL, PersistentDataType.STRING, "true");
        warpScrollMeta.setLore(List.of("A scroll to warp to a waystone"));
        warpScrollMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        warpScrollMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        warpScroll.setItemMeta(warpScrollMeta);

        //add a recipe for the warp scroll
        ShapedRecipe warpScrollRecipe = new ShapedRecipe(Keys.WARP_SCROLL, warpScroll);
        warpScrollRecipe.shape(" P ", "PEP", " P ");
        //add ender gem as ingredient
        warpScrollRecipe.setIngredient('E', new RecipeChoice.ExactChoice(enderGem));
        warpScrollRecipe.setIngredient('P', Material.PAPER);
        warpScrollRecipe.setCategory(CraftingBookCategory.EQUIPMENT);

        //register the recipe
        Bukkit.addRecipe(warpScrollRecipe);

    }


    // Métodos para verificar las condiciones de crafteo de cada mochila
    private static boolean canCraftLv2(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        return container.has(Keys.BACKPACKLV1, PersistentDataType.STRING);
    }

    private static boolean canCraftLv3(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        return container.has(Keys.BACKPACKLV2, PersistentDataType.STRING);
    }

    private static boolean canCraftLv4(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        return container.has(Keys.BACKPACKLV3, PersistentDataType.STRING);
    }

    private static boolean canCraftLv5(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        return container.has(Keys.BACKPACKLV4, PersistentDataType.STRING);
    }




}