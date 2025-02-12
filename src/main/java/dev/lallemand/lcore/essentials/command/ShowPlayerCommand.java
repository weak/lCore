package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.Locale;

@CommandMeta(label = "showplayer", permission = "lcore.showplayer")
public class ShowPlayerCommand {

    public void execute(Player player, Player target) {
        if (target == null) {
            player.sendMessage(Locale.PLAYER_NOT_FOUND.format());
            return;
        }

        player.showPlayer(target);
        player.sendMessage(ChatColor.GOLD + "Showing you " + target.getName());
    }

    public void execute(Player player, Player target1, Player target2) {
        if (target1 == null || target2 == null) {
            player.sendMessage(Locale.PLAYER_NOT_FOUND.format());
            return;
        }

        target1.showPlayer(target2);
        player.sendMessage(ChatColor.GOLD + "Showing " + target2.getName() + " to " + target1.getName());
    }

}