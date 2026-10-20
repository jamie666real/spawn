package com.jamie666real.spawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpawnPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        Bukkit.getPluginManager().registerEvents(this, this);

        if (getCommand("setspawn") != null) {
            getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        }

        if (getCommand("spawn") != null) {
            getCommand("spawn").setExecutor(new SpawnCommand(this));
        }
    }

    public Location getSpawnLocation() {
        if (!getConfig().contains("spawn.world")) {
            return null;
        }

        String worldName = getConfig().getString("spawn.world");
        if (worldName == null || worldName.isBlank()) {
            return null;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }

        double x = getConfig().getDouble("spawn.x");
        double y = getConfig().getDouble("spawn.y");
        double z = getConfig().getDouble("spawn.z");
        float yaw = (float) getConfig().getDouble("spawn.yaw", 0.0D);
        float pitch = (float) getConfig().getDouble("spawn.pitch", 0.0D);

        return new Location(world, x, y, z, yaw, pitch);
    }

    public void setSpawn(Location location) {
        if (location.getWorld() == null) {
            return;
        }

        getConfig().set("spawn.world", location.getWorld().getName());
        getConfig().set("spawn.x", location.getX());
        getConfig().set("spawn.y", location.getY());
        getConfig().set("spawn.z", location.getZ());
        getConfig().set("spawn.yaw", location.getYaw());
        getConfig().set("spawn.pitch", location.getPitch());
        saveConfig();
    }

    @EventHandler
    public void onPlayerVoid(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (event.getTo() == null) {
            return;
        }

        Location location = event.getTo();
        World world = location.getWorld();
        if (world == null) {
            return;
        }

        if (location.getY() <= world.getMinHeight() - 1) {
            Location spawnLocation = getSpawnLocation();
            if (spawnLocation == null) {
                spawnLocation = world.getSpawnLocation();
            }

            player.teleport(spawnLocation);
            player.sendMessage("§aYou fell into the void. Teleported to spawn!");
        }
    }
}
