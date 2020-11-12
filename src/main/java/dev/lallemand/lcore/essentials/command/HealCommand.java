package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "heal", permission = "lcore.heal")
public class HealCommand {

    public void execute(Player player) {
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setSaturation(5.0F);
        player.updateInventory();
        player.sendMessage(CC.GOLD + "You healed yourself.");
    }

    public void execute(CommandSender sender, Player player) {
        if (player == null) {
            sender.sendMessage(Locale.PLAYER_NOT_FOUND.format());
            return;
        }

        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setSaturation(5.0F);
        player.updateInventory();
        player.sendMessage(CC.GOLD + "You have been healed by " + sender.getName());
    }

}
