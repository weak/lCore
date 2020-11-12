package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "night")
public class NightCommand {

    public void execute(Player player) {
        player.setPlayerTime(18000L, false);
        player.sendMessage(CC.GREEN + "It's now night time.");
    }

}
