package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "spawn", permission = "lcore.spawn")
public class SpawnCommand {

    public void execute(Player player) {
        lCore.get().getEssentials().teleportToSpawn(player);
        player.sendMessage(CC.GREEN + "You teleported to this world's spawn.");
    }

}
