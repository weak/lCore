package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;

@CommandMeta(label = "restart", permission = "lcore.restart")
public class RestartCommand {

    public void execute(Player player) {
        player.sendMessage(ChatColor.YELLOW + "/restart start (time in seconds)");
        player.sendMessage(ChatColor.YELLOW + "/restart cancel");
    }

    @CommandMeta(label = "start", permission = "lcore.start")
    public class start extends RestartCommand {
        public void execute(Player player, @CPL("seconds") Number seconds) {
            lCore.get().getEssentials().restart(seconds.intValue());
        }
    }

    @CommandMeta(label = "cancel", permission = "lcore.start")
    public class cancel extends RestartCommand {
        public void execute(Player player) {
            lCore.get().getEssentials().cancelRestart();
        }
    }

}
