package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.CommandOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "sudoall", permission = "lcore.sudo", options = "s")
public class SudoAllCommand {

    public void execute(CommandSender sender, CommandOption option, @CPL("command") String command) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (option == null && !player.equals(sender)) {
                continue;
            }

            player.performCommand(command);
        }

        sender.sendMessage(ChatColor.GREEN + "Forced all players to chat!");
    }

}
