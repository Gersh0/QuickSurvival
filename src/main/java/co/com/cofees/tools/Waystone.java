package co.com.cofees.tools;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Waystone {
//this class have a
    //Location, name, list of players, and a ItemStack for the icon

    Location location;
    String name;
    List<String> players;
    ItemStack icon;

    public Waystone(Location location, String name, List<String> players, ItemStack icon) {
        this.location = location;
        this.name = name;
        this.players = players;
        icon = icon == null ? new ItemStack(Material.GREEN_WOOL) : icon;

        ItemMeta defaultMeta = icon.getItemMeta();

        if (defaultMeta == null) return;

        defaultMeta.setDisplayName(name);
        icon.setItemMeta(defaultMeta);
        defaultMeta.getPersistentDataContainer().set(Keys.WAYSTONE, PersistentDataType.STRING, name);


        //set the icon to a default icon
        this.icon = icon;

    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public List<String> getPlayers() {
        return players;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public void addPlayer(String player) {

        players.add(player);
    }

    public void removePlayer(String player) {
        players.remove(player);
    }

    public boolean containsPlayer(String player) {
        return players.contains(player);
    }
    
}
