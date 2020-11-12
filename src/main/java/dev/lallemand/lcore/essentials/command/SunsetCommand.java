package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"sunset", "evening"})
public class SunsetCommand {

    public void execute(Player player) {
        player.setPlayerTime(12000, false);
        player.sendMessage(CC.GREEN + "It's now sunset.");
    }

}
