package co.com.cofees.tools;

import co.com.cofees.QuickSurvival;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;
import java.io.File;

public final class Regions {

    private static final Regions instance = new Regions();
    private final File file;
    private final YamlConfiguration config;
    private final Set<Region> regions = new HashSet<>();


    public Regions() {
        this.file = new File(QuickSurvival.getInstance().getDataFolder(), "regions.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    /*
    File pluginFolder = QuickSurvival.getInstance().getDataFolder();
            File inventoryFolder = new File(pluginFolder, "inventory");

            boolean saved = false;

            if (!inventoryFolder.exists()) saved  = inventoryFolder.mkdir();

            return (saved)? inventoryFolder : null;
     */
    public void load() {
        try {
            boolean created = false;
            if (!file.exists())
                created = file.createNewFile();

            if (created) {
                Bukkit.getConsoleSender().sendMessage("Created regions.yml");
            }

            config.load(file);


        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }

        regions.clear();

        if (config.isSet("Regions")) {

            for (Map<?, ?> rawRegion : config.getMapList("Regions"))
                regions.add(Region.deserialize((Map<String, Object>) rawRegion));

            Bukkit.getConsoleSender().sendMessage("Loaded regions: " + getRegionsNames());
        }
    }

    public void save() {
        List<Map<String, Object>> serializedRegions = new ArrayList<>();
        for (Region region : regions)
            serializedRegions.add(region.serialize());

        config.set("Regions", serializedRegions);

        try {
            config.save(file);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Nullable
    public Region findRegion(Location location) {
        for (Region region : this.regions)
            if (region.isWithin(location))
                return region;
        return null;
    }


    public Region findRegion(String name) {
        for (Region region : this.regions)
            if (region.getName().equalsIgnoreCase(name))
                return region;

        return null;
    }

    public Set<Region> getRegions() {
        return Collections.unmodifiableSet(regions);
    }

    public List<String> getPlayersInRegion(String name) {
        Region region = findRegion(name);
        if (region != null) {
            return region.getPlayers();
        }
        return null;
    }

    public Set<String> getRegionsNames() {
        Set<String> names = new HashSet<>();

        for (Region region : regions)
            names.add(region.getName());

        return Collections.unmodifiableSet(names);
    }

    public void saveRegion(String name, Location primary, Location secondary, List<String> players) {
        this.regions.add(new Region(name, primary, secondary, players));
        save();
    }

    public void deleteRegion(String name) {
        Region region = findRegion(name);
        if (region != null) {
            regions.remove(region);
            save();
        }
    }

    public static Regions getInstance() {
        return instance;
    }


}
