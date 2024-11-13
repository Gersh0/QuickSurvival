package co.com.cofees.events;

import co.com.cofees.tools.Keys;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class CraftBackpack implements Listener {

    @EventHandler
    public void onBackpackCraft(CraftItemEvent event) {
        if (event.getCurrentItem() == null) return;

        ItemMeta itemMeta = event.getCurrentItem().getItemMeta();
        if (itemMeta == null) return;

        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer(); //Retrieve the PersistentDataContainer from the item metadata
        if (dataContainer.has(Keys.BACKPACKLV1, PersistentDataType.STRING)) {
            handleLevel1Backpack(event);
            return;
        }

        if (dataContainer.has(Keys.BACKPACKLV2, PersistentDataType.STRING)) {
            handleBackpackCraft(event, Keys.BACKPACKLV1);
            return;
        }

        if (dataContainer.has(Keys.BACKPACKLV3, PersistentDataType.STRING)) {
            handleBackpackCraft(event, Keys.BACKPACKLV2);
            return;
        }

        if (dataContainer.has(Keys.BACKPACKLV4, PersistentDataType.STRING)) {
            handleBackpackCraft(event, Keys.BACKPACKLV3);
            return;
        }

        if (dataContainer.has(Keys.BACKPACKLV5, PersistentDataType.STRING)) {
            handleBackpackCraft(event, Keys.BACKPACKLV4);
        }
    }

    private void handleLevel1Backpack(CraftItemEvent event) {
        // Check if the current item in the event is null, if so, exit the method
        if (event.getCurrentItem() == null) return;

        // Retrieve the metadata of the current item
        ItemMeta backpackMeta = event.getCurrentItem().getItemMeta();
        // Check if the metadata is null, if so, exit the method
        if (backpackMeta == null) return;

        // Generate a unique code for the backpack by creating a UUID and removing dashes
        String additionalCode = UUID.randomUUID().toString().replace("-", "");
        // Truncate the code to 10 characters
        additionalCode = cutUUID(additionalCode, 10);

        // Ensure the PersistentDataContainer is not null and set the generated code
        backpackMeta.getPersistentDataContainer().set(Keys.BACKPACK_CODE, PersistentDataType.STRING, additionalCode);
        // Update the item metadata with the new code
        event.getCurrentItem().setItemMeta(backpackMeta);

        // Notify the player who crafted the backpack with the generated code
        Player player = (Player) event.getWhoClicked();
        player.sendMessage("Backpack created!");
        //player.sendMessage("Backpack created with additional code: " + additionalCode);
    }

    private void handleBackpackCraft(CraftItemEvent event, NamespacedKey previousLevelKey) {
        // Check if the current item in the event is null, if so, exit the method
        if (event.getCurrentItem() == null) return;

        // Retrieve the crafting inventory from the event
        Inventory craftingInventory = event.getInventory();
        // Find the previous level backpack in the crafting inventory
        ItemStack previousBackpack = Arrays.stream(craftingInventory.getContents())
                .filter(Objects::nonNull)
                .filter(item -> {
                    ItemMeta meta = item.getItemMeta();
                    return meta != null && meta.getPersistentDataContainer().has(previousLevelKey, PersistentDataType.STRING);
                })
                .findFirst()
                .orElse(null);

        // Check if the previous backpack or its metadata is null, if so, exit the method
        if (previousBackpack == null || previousBackpack.getItemMeta() == null) return;

        // Retrieve the metadata of the previous backpack
        ItemMeta previousMeta = previousBackpack.getItemMeta();
        // Retrieve the code from the previous level backpack
        String backpackUUID = previousMeta.getPersistentDataContainer().get(Keys.BACKPACK_CODE, PersistentDataType.STRING);

        // Check if the retrieved code is null, if so, exit the method
        if (backpackUUID == null) return;

        // Retrieve the metadata of the current item
        ItemMeta backpackMeta = event.getCurrentItem().getItemMeta();
        // Check if the metadata is null, if so, exit the method
        if (backpackMeta == null) return;

        // Set the retrieved code to the newly crafted backpack
        backpackMeta.getPersistentDataContainer().set(Keys.BACKPACK_CODE, PersistentDataType.STRING, backpackUUID);
        // Update the item metadata with the new code
        event.getCurrentItem().setItemMeta(backpackMeta);

        // Notify the player who crafted the backpack with the inherited code
        Player player = (Player) event.getWhoClicked();
        player.sendMessage("Backpack created with additional code: " + backpackUUID);
    }

    // Utility method to truncate a UUID string to a specified length
    public static String cutUUID(String uuid, int length) {
        return uuid.substring(0, Math.min(uuid.length(), length));
    }
}