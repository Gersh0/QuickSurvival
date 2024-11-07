package co.com.cofees.commands;

import co.com.cofees.tools.ControlMenuGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ControlMenuCommand implements CommandExecutor {

    ControlMenuGUI guiManager = new ControlMenuGUI();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be executed by players.");
            return false;
        }

        guiManager.openMainMenu(player);
        return true;
    }
}
