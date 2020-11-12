package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.Locale;

@CommandMeta(label = "sudo", permission = "lcore.sudo")
public class SudoCommand {

    public void execute(CommandSender sender, @CPL("player") Player target, @CPL("command") String command) {
        if (target == null) {
            sender.sendMessage(Locale.PLAYER_NOT_FOUND.format());
            return;
        }

        target.performCommand(command);
        sender.sendMessage(ChatColor.GREEN + "Forced target to chat!");
    }

}
