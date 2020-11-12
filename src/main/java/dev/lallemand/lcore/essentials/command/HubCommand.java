package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.util.BungeeUtil;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"hub", "lobby", "leave"}, async = true)
public class HubCommand {

    public void execute(Player player) {
        player.sendMessage(CC.GREEN + "Sending you to " + CC.WHITE + lCore.get().getMainConfig().getString("SETTINGS.HUB_NAME"));
        BungeeUtil.connect(player, lCore.get().getMainConfig().getString("SETTINGS.HUB_NAME"));
    }

}
