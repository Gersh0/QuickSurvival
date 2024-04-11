package co.com.cofees.commands;



import java.util.*;
import java.util.stream.Collectors;

import co.com.cofees.commands.subcommands.DeleteHomeCommand;
import co.com.cofees.commands.subcommands.RegionAddPlayerCommand;
import co.com.cofees.commands.subcommands.SetHomeCommand;
import co.com.cofees.completers.DeleteHomeCompleter;
import co.com.cofees.completers.RegionAddPlayerCompleter;
import co.com.cofees.completers.SetHomeCompleter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import co.com.cofees.tools.Regions;
import co.com.cofees.tools.Region;
import org.jetbrains.annotations.NotNull;

public final class RegionCommand implements CommandExecutor, TabCompleter {


    private final Map<String, CommandExecutor> subCommands = new HashMap<>();
    private final Map<String, TabCompleter> subCommandCompleters = new HashMap<>();

    public RegionCommand() {
        register("addPlayer", new RegionAddPlayerCommand(), new RegionAddPlayerCompleter());

    }

    // TODO Move into PlayerCache
    private final Map<UUID, Tuple<Location, Location>> selections = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");

            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /region <pos1|pos2|save <name>|paste <name>>");

            return true;
        }

        Regions regions = Regions.getInstance();

        Player player = (Player) sender;
        String param = args[0].toLowerCase();
        //use of Tuple class to store two locations

        Tuple<Location, Location> selection = selections.getOrDefault(player.getUniqueId(), new Tuple<>(null, null));

        //switch case to handle the different commands
        //pos1: set the first location
        if ("pos1".equals(param)) {
            Location loc = player.getTargetBlockExact(5).getLocation();
            if (loc == null) {
                sender.sendMessage("§8[§c❌§8] §7Please look at a block!");

                return true;
            }
            selection.setFirst(loc);

            sender.sendMessage("§8[§a✔§8] §7First location set!");
            selections.put(player.getUniqueId(), selection);
        //pos2: set the second location
        } else if ("pos2".equals(param)) {
            Location loc = player.getTargetBlockExact(5).getLocation();
            if (loc == null) {
                sender.sendMessage("§8[§c❌§8] §7Please look at a block!");

                return true;
            }
            selection.setSecond(loc);

            sender.sendMessage("§8[§a✔§8] §7Second location set!");
            selections.put(player.getUniqueId(), selection);

        //save: save the region
        } else if ("save".equals(param)) {
            if (selection.getFirst() == null || selection.getSecond() == null) {
                sender.sendMessage("§8[§c❌§8] §7Please select both positions first using /region pos1 and /region pos2");

                return true;
            }

            if (args.length != 2) {
                sender.sendMessage("§8[§6🛑§8] §7Usage: /region save <name>");

                return true;
            }

			/*File file = new File(plugin.getDataFolder(), "schematic/" + args[1] + ".schem");

			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();

			WorldEditHook.save(selection.getFirst(), selection.getSecond(), file);*/

            String name = args[1];

            if (regions.findRegion(name) != null) {
                sender.sendMessage(ChatColor.RED + "Region by this name already exists.");

                return true;
            }

            List<String> players = new ArrayList<>();
            players.add(player.getName());

            regions.saveRegion(name, selection.getFirst(), selection.getSecond(), players);

            sender.sendMessage("§8[§a✔§8] §7Schematic saved!");
        //list: list all the regions
        } else if ("list".equals(param)) {
            sender.sendMessage(ChatColor.GOLD + "Installed regions: " + String.join(", ", regions.getRegionsNames()));
        //current: get the current region
        } else if ("current".equals(param)) {
            Region standingIn = regions.findRegion(player.getLocation());

            sender.sendMessage(ChatColor.GOLD + "You are standing in region: "
                    + (standingIn == null ? "none" : standingIn.getName()));

			/*} else if ("paste".equals(param)) {
				if (args.length != 2) {
					sender.sendMessage("§8[§6🛑§8] §7Usage: /region paste <name>");

					return true;
				}

				File file = new File(plugin.getDataFolder(), "schematic/" + args[1] + ".schem");

				if (!file.exists()) {
					sender.sendMessage("§8[§c❌§8] §7Schematic not found!");

					return true;
				}

				WorldEditHook.paste(player.getLocation(), file);
				sender.sendMessage("§8[§a✔§8] §7Schematic pasted at " + player.getLocation());*/

        } else if ("delete".equals(param) ){
            if (args.length != 2) {
                sender.sendMessage("§8[§6🛑§8] §7Usage: /region delete <name>");

                return true;
            }

            String name = args[1];

            if (regions.findRegion(name) == null) {
                sender.sendMessage(ChatColor.RED + "Region by this name does not exist.");

                return true;
            }

            regions.deleteRegion(name);

            sender.sendMessage("§8[§a✔§8] §7Region deleted!");

        } else if ("addPlayer".equals(param)){
            if (args.length != 3) {
                sender.sendMessage("§8[§6🛑§8] §7Usage: /region addPlayer <region> <player>");

                return true;
            }

            String regionName = args[1];
            String playerName = args[2];
            List<String> players = regions.getPlayersInRegion(regionName);

            Region region = regions.findRegion(regionName);

            if (region == null) {
                sender.sendMessage(ChatColor.RED + "Region by this name does not exist.");

                return true;
            }

            if (!players.contains(playerName)){
                sender.sendMessage(ChatColor.RED + "You cannot add a player, you dont have permission.");
                return true;
            }



            region.addPlayer(playerName);
            regions.save();

            sender.sendMessage("§8[§a✔§8] §7Player added to region!");
        } else if ("deletePlayer".equals(param)){
            if (args.length != 3) {
                sender.sendMessage("§8[§6🛑§8] §7Usage: /region deletePlayer <region> <player>");

                return true;
            }

            String regionName = args[1];
            String playerName = args[2];
            Region region = regions.findRegion(regionName);
            List<String> players = regions.getPlayersInRegion(regionName);

            if (region == null) {
                sender.sendMessage(ChatColor.RED + "Region by this name does not exist.");

                return true;
            }

            if (!players.contains(playerName)){
                sender.sendMessage(ChatColor.RED + "You cannot delete a player, you dont have permission.");
                return true;
            }

            region.deletePlayer(playerName);
            regions.save();

            sender.sendMessage("§8[§a✔§8] §7Player removed from region!");

        } else
            sender.sendMessage("§8[§6🛑§8] §7Usage: /region <pos1|pos2|save <name>|paste <name>>");

        return true;
    }

    private static class Tuple<A, B> {
        private A first;
        private B second;

        public Tuple(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public void setFirst(A first) {
            this.first = first;
        }

        public B getSecond() {
            return second;
        }

        public void setSecond(B second) {
            this.second = second;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) return null; //Clauses the case if is not a player.
        if (command.getName().equalsIgnoreCase("region") && args.length == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("pos1");
            completions.add("pos2");
            completions.add("list");
            completions.add("addPlayer");
            completions.add("deletePlayer");
            completions.add("current");
            completions.add("delete");
            completions.add("save");
            //add the Region names
            completions.addAll(Regions.getInstance().getRegionsNames());



            return completions;
        }
        if (args.length > 1 && args[0].equalsIgnoreCase("delete")) {
            Regions regions = Regions.getInstance();
            return regions.getRegions().stream().map(Region::getName).collect(Collectors.toList());
        }

        if (args.length > 1 && args[0].equalsIgnoreCase("save")) {
            return Collections.singletonList("<name>");
        }

        if (args.length>1 && args[0].equalsIgnoreCase("addPlayer")){
            Regions regions = Regions.getInstance();
            return regions.getPlayersInRegion(args[1]);
        }

        if (args.length==2 && args[0].equalsIgnoreCase("deletePlayer")){
            Regions regions = Regions.getInstance();
            //show the players in the region
            return regions.getRegions().stream().map(Region::getName).collect(Collectors.toList());
        }

        if (args.length>2 && args[0].equalsIgnoreCase("deletePlayer")){
            Regions regions = Regions.getInstance();
            return regions.getPlayersInRegion(args[1]);
        }




        return null;
    }



    // Register a subcommand with its executor and completer
    public void register(String name, CommandExecutor cmd, TabCompleter completer) {
        subCommands.put(name, cmd);
        subCommandCompleters.put(name, completer);
    }


}