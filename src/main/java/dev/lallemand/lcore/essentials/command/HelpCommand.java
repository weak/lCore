package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"help", "ts", "discord", "?", "store", "links", "twitter"}, async = true)
public class HelpCommand {

    public void execute(Player player) {
        for (String message : lCore.get().getMainConfig().getStringList("SETTINGS.HELP_MESSAGE")) {
            player.sendMessage(CC.translate(message));
        }
    }

}

