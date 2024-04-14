package co.com.cofees.commands.subcommands;
import co.com.cofees.tools.Region;
import co.com.cofees.tools.Regions;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ShowRegionCommand implements CommandExecutor {

    Regions regions = Regions.getInstance();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("You need to specify a region name");
            return false;
        }

        if (args.length > 1) {
            sender.sendMessage("The name of the region can't have spaces");
            return false;
        }

        Region region = regions.findRegion(args[0]);

        if (region == null) {
            sender.sendMessage("Region not found");
            return false;
        }

        showParticles(region);


        return true;
    }

    //method that will make a cube of particles to show the region

    private List<Location> getRegionParticles(Region region) {

        Location corner1 = region.getLocation1();
        Location corner2 = region.getLocation2();

            List<Location> result = new ArrayList<Location>();
            World world = corner1.getWorld();
            double minX = Math.min(corner1.getX(), corner2.getX());
            double minY = Math.min(corner1.getY(), corner2.getY());
            double minZ = Math.min(corner1.getZ(), corner2.getZ());
            double maxX = Math.max(corner1.getX(), corner2.getX());
            double maxY = Math.max(corner1.getY(), corner2.getY());
            double maxZ = Math.max(corner1.getZ(), corner2.getZ());

            // 2 areas
            for (double x = minX; x <= maxX; x+=0.2D) {
                for (double z = minZ; z <= maxZ; z+=0.2D) {
                    result.add(new Location(world, x, minY, z));
                    result.add(new Location(world, x, maxY, z));
                }
            }

            // 2 sides (front & back)
            for (double x = minX; x <= maxX; x+=0.2D) {
                for (double y = minY; y <= maxY; y+=0.2D) {
                    result.add(new Location(world, x, y, minZ));
                    result.add(new Location(world, x, y, maxZ));
                }
            }

            // 2 sides (left & right)
            for (double z = minZ; z <= maxZ; z+=0.2D) {
                for (double y = minY; y <= maxY; y+=0.2D) {
                    result.add(new Location(world, minX, y, z));
                    result.add(new Location(world, maxX, y, z));
                }
            }

            return result;
        }



    //method that will show the particles
    private void showParticles(Region region) {
        List<Location> locations = new ArrayList<Location>();
        locations = getRegionParticles(region);

        for (Location location : locations) {
            location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 1, 0, 0, 0, 0);
        }
    }



    }


