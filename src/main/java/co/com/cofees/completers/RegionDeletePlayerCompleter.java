package co.com.cofees.completers;

import co.com.cofees.tools.Region;
import co.com.cofees.tools.Regions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class RegionDeletePlayerCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //return players that are in the region
        Regions regions = Regions.getInstance();

        if (args.length == 0){
            return regions.getRegions().stream().map(Region::getName).collect(Collectors.toList());
        }


        String regionName = args[0];
        if (regions.findRegion(regionName) == null) {
            return null;
        }

        return regions.getPlayersInRegion(regionName);


    }
}
