package co.com.cofees.events;

import co.com.cofees.tools.Keys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.security.Key;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CraftBackpack implements Listener {


    @EventHandler
    public void onBackpackCraft(CraftItemEvent event) {
        // Verificar si el ítem creado es una mochila lv1

       /* event.getWhoClicked().sendMessage(event.getCurrentItem() +" ---------------------------------------------------"+ Arrays.stream(event.getInventory().getContents()).toList().stream().filter(
                        (item -> item.getItemMeta().getPersistentDataContainer().has(Keys.BACKPACKLV1,PersistentDataType.STRING)))
                .findFirst().orElse(null).toString());*/
        if (event.getCurrentItem() != null &&
                Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getPersistentDataContainer().has(Keys.BACKPACKLV1, PersistentDataType.STRING)) {

            // Obtener la meta del ítem
            ItemMeta backpackMeta = event.getCurrentItem().getItemMeta();

            // Generar un código único utilizando UUID
            String codigoAdicional = UUID.randomUUID().toString().replace("-", "");
            codigoAdicional = cutUUID(codigoAdicional, 10);
            // Asignar el código adicional al PersistentDataContainer
            backpackMeta.getPersistentDataContainer().set(Keys.BACKPACK_CODE, PersistentDataType.STRING, codigoAdicional);


            event.getCurrentItem().setItemMeta(backpackMeta);

            Player player = (Player) event.getWhoClicked();
            player.sendMessage("Se creó un backpack con código adicional: " + codigoAdicional);

        } else if (event.getCurrentItem() != null &&
                Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getPersistentDataContainer().has(Keys.BACKPACKLV2, PersistentDataType.STRING)) {


            Inventory craftingInventory = event.getInventory();

            ItemStack backpack = Arrays.stream(craftingInventory.getContents()).toList().stream().filter(
                            (item -> item.getItemMeta().getPersistentDataContainer().has(Keys.BACKPACKLV1, PersistentDataType.STRING)))
                    .findFirst().orElse(null);


            // Obtener la meta del ítem
            ItemMeta backpackMeta = event.getCurrentItem().getItemMeta();

            // Obtener UUID DE LA MOCHILA ANTERIOR

            String BackpackUUID = backpack.getItemMeta().getPersistentDataContainer().get(Keys.BACKPACK_CODE, PersistentDataType.STRING);

            // Asignar el código adicional al PersistentDataContainer
            backpackMeta.getPersistentDataContainer().set(Keys.BACKPACK_CODE, PersistentDataType.STRING, BackpackUUID);


            event.getCurrentItem().setItemMeta(backpackMeta);

            Player player = (Player) event.getWhoClicked();
            player.sendMessage("Se creó un backpack con código adicional: " + BackpackUUID);

        } else if (event.getCurrentItem() != null &&
                Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getPersistentDataContainer().has(Keys.BACKPACKLV3, PersistentDataType.STRING)) {


            Inventory craftingInventory = event.getInventory();

            ItemStack backpack = Arrays.stream(craftingInventory.getContents()).toList().stream().filter(
                            (item -> item.getItemMeta().getPersistentDataContainer().has(Keys.BACKPACKLV2, PersistentDataType.STRING)))
                    .findFirst().orElse(null);


            // Obtener la meta del ítem
            ItemMeta backpackMeta = event.getCurrentItem().getItemMeta();

            // Obtener UUID DE LA MOCHILA ANTERIOR

            String BackpackUUID = backpack.getItemMeta().getPersistentDataContainer().get(Keys.BACKPACK_CODE, PersistentDataType.STRING);

            // Asignar el código adicional al PersistentDataContainer
            backpackMeta.getPersistentDataContainer().set(Keys.BACKPACK_CODE, PersistentDataType.STRING, BackpackUUID);


            event.getCurrentItem().setItemMeta(backpackMeta);

            Player player = (Player) event.getWhoClicked();
            player.sendMessage("Se creó un backpack con código adicional: " + BackpackUUID);

        } else if (event.getCurrentItem() != null &&
                Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getPersistentDataContainer().has(Keys.BACKPACKLV4, PersistentDataType.STRING)) {


            Inventory craftingInventory = event.getInventory();

            ItemStack backpack = Arrays.stream(craftingInventory.getContents()).toList().stream().filter(
                            (item -> item.getItemMeta().getPersistentDataContainer().has(Keys.BACKPACKLV3, PersistentDataType.STRING)))
                    .findFirst().orElse(null);


            // Obtener la meta del ítem
            ItemMeta backpackMeta = event.getCurrentItem().getItemMeta();

            // Obtener UUID DE LA MOCHILA ANTERIOR

            String BackpackUUID = backpack.getItemMeta().getPersistentDataContainer().get(Keys.BACKPACK_CODE, PersistentDataType.STRING);

            // Asignar el código adicional al PersistentDataContainer
            backpackMeta.getPersistentDataContainer().set(Keys.BACKPACK_CODE, PersistentDataType.STRING, BackpackUUID);


            event.getCurrentItem().setItemMeta(backpackMeta);

            Player player = (Player) event.getWhoClicked();
            player.sendMessage("Se creó un backpack con código adicional: " + BackpackUUID);

        } else if (event.getCurrentItem() != null &&
                Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getPersistentDataContainer().has(Keys.BACKPACKLV5, PersistentDataType.STRING)) {


            Inventory craftingInventory = event.getInventory();

            ItemStack backpack = Arrays.stream(craftingInventory.getContents())
                    .filter(Objects::nonNull) // Filtra elementos nulos
                    .filter(item ->
                            item.getItemMeta() != null &&
                                    item.getItemMeta().getPersistentDataContainer() != null &&
                                    item.getItemMeta().getPersistentDataContainer().has(Keys.BACKPACKLV4, PersistentDataType.STRING))
                    .findFirst()
                    .orElse(null);


            // Obtener la meta del ítem
            ItemMeta backpackMeta = event.getCurrentItem().getItemMeta();

            // Obtener UUID DE LA MOCHILA ANTERIOR

            String BackpackUUID = backpack.getItemMeta().getPersistentDataContainer().get(Keys.BACKPACK_CODE, PersistentDataType.STRING);

            // Asignar el código adicional al PersistentDataContainer
            backpackMeta.getPersistentDataContainer().set(Keys.BACKPACK_CODE, PersistentDataType.STRING, BackpackUUID);


            event.getCurrentItem().setItemMeta(backpackMeta);

            Player player = (Player) event.getWhoClicked();
            player.sendMessage("Se creó un backpack con código adicional: " + BackpackUUID);

        }
    }

    public static String cutUUID(String uuid, int lenght) {
        // Truncar o ajustar la longitud según sea necesario
        return uuid.substring(0, Math.min(uuid.length(), lenght));
    }
}


