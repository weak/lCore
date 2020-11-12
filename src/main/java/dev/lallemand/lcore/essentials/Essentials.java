package dev.lallemand.lcore.essentials;

import dev.lallemand.lcore.essentials.tasks.RestartTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.essentials.event.SpawnTeleportEvent;
import dev.lallemand.lcore.util.LocationUtil;

import java.io.IOException;
import java.util.List;

public class Essentials {

    private lCore lcore;
    private Location spawn;
    @Getter @Setter
    private boolean donorOnly;
    @Getter private List<String> commandsBlocked;
    @Getter private boolean restarting;
    @Getter private int taskID;

    public Essentials(lCore lcore) {
        this.lcore = lcore;
        this.spawn = LocationUtil.deserialize(lcore.getMainConfig().getStringOrDefault("ESSENTIAL.SPAWN_LOCATION", null));
        this.donorOnly = lcore.getMainConfig().getBoolean("ESSENTIAL.DONOR_ONY");
        this.commandsBlocked = lcore.getMainConfig().getStringList("ESSENTIAL.COMMAND_BLOCK");
    }

    public void restart(int time) {
        this.restarting = true;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(lcore, new RestartTask(time), 0, 20);
    }

    public void cancelRestart() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public void setSpawn(Location location) {
        spawn = location;

        if (spawn == null) {
            lcore.getMainConfig().getConfiguration().set("ESSENTIAL.SPAWN_LOCATION", null);
        } else {
            lcore.getMainConfig().getConfiguration().set("ESSENTIAL.SPAWN_LOCATION", LocationUtil.serialize(this.spawn));
        }

        try {
            lcore.getMainConfig().getConfiguration().save(lcore.getMainConfig().getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        lcore.reloadConfig();
        lcore.getMainConfig().setConfiguration(YamlConfiguration.loadConfiguration(lcore.getMainConfig().getFile()));
        this.commandsBlocked = lcore.getMainConfig().getStringList("ESSENTIAL.COMMAND_BLOCK");
    }

    public void teleportToSpawn(Player player) {
        Location location = spawn == null ? lcore.getServer().getWorlds().get(0).getSpawnLocation() : spawn;

        SpawnTeleportEvent event = new SpawnTeleportEvent(player, location);
        event.call();

        if (!event.isCancelled() && event.getLocation() != null) {
            player.teleport(event.getLocation());
        }
    }

    public int clearEntities(World world) {
        int removed = 0;

        for (Entity entity : world.getEntities()) {
            if (entity.getType() == EntityType.PLAYER) {
                continue;
            }

            removed++;
            entity.remove();
        }

        return removed;
    }

    public int clearEntities(World world, EntityType... excluded) {
        int removed = 0;

        entityLoop:
        for (Entity entity : world.getEntities()) {
            if (entity instanceof Item) {
                removed++;
                entity.remove();
                continue;
            }

            for (EntityType type : excluded) {
                if (entity.getType() == EntityType.PLAYER) {
                    continue entityLoop;
                }

                if (entity.getType() == type) {
                    continue entityLoop;
                }
            }

            removed++;
            entity.remove();
        }

        return removed;
    }

}
