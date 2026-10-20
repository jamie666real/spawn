package com.jamie666real.spawn;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SpawnCommand implements CommandExecutor {

    private final SpawnPlugin plugin;

    public SpawnCommand(SpawnPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Location spawnLocation = plugin.getSpawnLocation();
        if (spawnLocation == null) {
            spawnLocation = player.getWorld().getSpawnLocation();
        }

        player.teleport(spawnLocation);
        player.sendMessage(ChatColor.GOLD + "Teleported to spawn!");
        return true;
    }
}
