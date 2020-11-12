package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "setspawn", permission = "lcore.setspawn")
public class SetSpawnCommand {

    public void execute(Player player) {
        lCore.get().getEssentials().setSpawn(player.getLocation());
        player.sendMessage(CC.GREEN + "You updated this world's spawn.");
    }

}
