package co.com.cofees.commands.subcommands;

import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.Region;
import co.com.cofees.tools.Regions;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

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

    private void showParticles(Region region) {
        new BukkitRunnable() {
            int counter = 0; // Counter to keep track of ticks

            @Override
            public void run() {
                if (counter >= 40) { // 40 ticks = 2 seconds
                    cancel(); // Stop the runnable
                    return;
                }

                Location corner1 = region.getLocation1();
                Location corner2 = region.getLocation2();

                World world = corner1.getWorld();

                double minX = Math.min(corner1.getX(), corner2.getX());
                double minY = Math.min(corner1.getY(), corner2.getY());
                double minZ = Math.min(corner1.getZ(), corner2.getZ());
                double maxX = Math.max(corner1.getX(), corner2.getX());
                double maxY = Math.max(corner1.getY(), corner2.getY());
                double maxZ = Math.max(corner1.getZ(), corner2.getZ());

                // Generate particles along the edges of the region
                for (double x = minX; x <= maxX; x++) {
                    for (double y = minY; y <= maxY; y++) {
                        for (double z = minZ; z <= maxZ; z++) {
                            // Check if the current location is on the edge of the region
                            if (x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ) {
                                Location location = new Location(world, x, y, z+1);
                                world.spawnParticle(Particle.COMPOSTER, location, 1, 0, 0, 0, 0);
                            }
                        }
                    }
                }

                counter++; // Increment the counter
            }
        }.runTaskTimer(QuickSurvival.getInstance(), 0L, 1L); // Run the task every tick
    }

    }


