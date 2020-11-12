package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"gamemode", "gm"}, permission = "lcore.gamemode")
public class GameModeCommand {

    public void execute(Player player, @CPL("gamemode") GameMode gameMode) {
        if (gameMode == null) {
            player.sendMessage(CC.RED + "That game mode is not valid.");
            return;
        }

        player.setGameMode(gameMode);
        player.updateInventory();
        player.sendMessage(CC.GOLD + "You updated your game mode.");
    }

    public void execute(CommandSender sender, @CPL("gamemode") GameMode gameMode, @CPL("player") Player target) {
        if (target == null) {
            sender.sendMessage(Locale.PLAYER_NOT_FOUND.format());
            return;
        }

        if (gameMode == null) {
            sender.sendMessage(CC.RED + "That game mode is not valid.");
            return;
        }

        target.setGameMode(gameMode);
        target.updateInventory();
        target.sendMessage(CC.GOLD + "Your game mode has been updated by " + sender.getName());
    }

}
