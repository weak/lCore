package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCoreAPI;
import dev.lallemand.lcore.util.BukkitReflection;
import dev.lallemand.lcore.util.string.CC;
import dev.lallemand.lcore.util.string.StyleUtil;

@CommandMeta(label = "ping")
public class PingCommand {

    public void execute(Player player) {
        player.sendMessage(CC.YELLOW + "Your Ping: " + StyleUtil.colorPing(BukkitReflection.getPing(player)));
    }

    public void execute(CommandSender sender, @CPL("player") Player target) {
        if (target == null) {
            sender.sendMessage(CC.RED + "A player with that name could not be found.");
        } else {
            sender.sendMessage(lCoreAPI.getColoredName(target) + CC.YELLOW + "'s Ping: " +
                StyleUtil.colorPing(BukkitReflection.getPing(target)));
        }
    }

}
