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
    private final Set<Region> regions = QuickSurvival.regions;


    public Regions() {
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

    public void saveRegion(Region region, YamlConfiguration config, String path) {
        this.regions.add(region);
        config.set(path + ".players", region.getPlayers());
        config.set(path + ".name", region.getName());
        LocationHandler.serializeLocation(region.getLocation1(), config, path + ".location1");
        LocationHandler.serializeLocation(region.getLocation2(), config, path + ".location2");

        try {
            config.save(new File(QuickSurvival.getInstance().getDataFolder(), "regions.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        QuickSurvival.getInstance().getServer().getLogger().info("Region saved: " + region.getName());

    }

    public void deleteRegion(String name) {
        QuickSurvival.regionsConfig.set("Regions." + name, null);
        QuickSurvival.regions.remove(findRegion(name));
        try {
            QuickSurvival.regionsConfig.save(new File (QuickSurvival.getInstance().getDataFolder(), "regions.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        QuickSurvival.getInstance().getServer().getLogger().info("Region deleted: " + name);
    }

    public static Regions getInstance() {
        return instance;
    }


}
