package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"gmc"}, permission = "lcore.gamemode")
public class GMCCommand {

    public void execute(Player player) {
        player.setGameMode(GameMode.CREATIVE);
        player.updateInventory();
        player.sendMessage(CC.GOLD + "You updated your game mode.");
    }

    public void execute(CommandSender sender, Player target) {
        if (target == null) {
            sender.sendMessage(Locale.PLAYER_NOT_FOUND.format());
            return;
        }

        target.setGameMode(GameMode.CREATIVE);
        target.updateInventory();
        target.sendMessage(CC.GOLD + "Your game mode has been updated by " + sender.getName());
    }

}
