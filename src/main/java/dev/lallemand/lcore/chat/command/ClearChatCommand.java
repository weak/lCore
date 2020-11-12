package dev.lallemand.lcore.chat.command;

import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.Locale;

@CommandMeta(label = {"clearchat", "cc"}, permission = "lcore.staff.clearchat")
public class ClearChatCommand {

    public void execute(CommandSender sender) {
        String[] strings = new String[101];

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("lcore.staff") &&
                !lCore.get().getMainConfig().getBoolean("CHAT.CLEAR_CHAT_FOR_STAFF")) {
                player.sendMessage("");
            } else {
                player.sendMessage(strings);
            }
        }

        String senderName;
        if (sender instanceof Player) {
            Profile profile = Profile.getProfiles().get(((Player) sender).getUniqueId());
            senderName = profile.getActiveRank().getColor() + sender.getName();
        } else {
            senderName = ChatColor.DARK_RED + "Console";
        }

        Bukkit.broadcastMessage(Locale.CLEAR_CHAT_BROADCAST.format(senderName));
    }

}
