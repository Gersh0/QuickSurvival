package co.com.cofees.tools;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class LocationHandler {

    public static HashMap<String, Location> loadLocations(YamlConfiguration config, JavaPlugin core, String section) {
        HashMap<String, Location> homes = new HashMap<>();
        ConfigurationSection locationSection = config.getConfigurationSection(section);

        if (locationSection != null) {
            locationSection.getKeys(false).forEach(locationName -> {
                // Create a location from the config, checking for null values
                Location location = createLocationFromConfig(locationSection, locationName, core);
                if (location != null) { // Ensure the location is not null before adding
                    homes.put(locationName, location);
                } else {
                    core.getLogger().warning("Invalid location for: " + locationName);
                }
            });
        }

        return homes;
    }

    private static Location createLocationFromConfig(ConfigurationSection section, String locationName, JavaPlugin core) {
        String world = section.getString(locationName + ".world");

        if (world == null) return null;

        double x = section.getDouble(locationName + ".x");
        double y = section.getDouble(locationName + ".y");
        double z = section.getDouble(locationName + ".z");
        float yaw = (float) section.getDouble(locationName + ".yaw");
        float pitch = (float) section.getDouble(locationName + ".pitch");

        return new Location(core.getServer().getWorld(world), x, y, z, yaw, pitch);
    }
}