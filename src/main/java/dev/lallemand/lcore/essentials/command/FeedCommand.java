package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"feed", "eat"}, permission = "lcore.feed")
public class FeedCommand {

    public void execute(Player player) {
        player.setFoodLevel(20);
        player.setSaturation(5.0F);
        player.updateInventory();
        player.sendMessage(CC.GOLD + "You feed yourself.");
    }

    public void execute(CommandSender sender, Player player) {
        if (player == null) {
            sender.sendMessage(Locale.PLAYER_NOT_FOUND.format());
            return;
        }

        player.setFoodLevel(20);
        player.setSaturation(5.0F);
        player.updateInventory();
        player.sendMessage(CC.GOLD + "You have been fed by " + sender.getName());
    }

}
