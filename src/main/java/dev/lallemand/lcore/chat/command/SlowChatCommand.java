package dev.lallemand.lcore.chat.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.lCoreAPI;
import dev.lallemand.lcore.Locale;

@CommandMeta(label = "slowchat", permission = "lcore.staff.slowchat")
public class SlowChatCommand {

    public void execute(CommandSender sender) {
        lCore.get().getChat().togglePublicChatDelay();

        String senderName;

        if (sender instanceof Player) {
            senderName = lCoreAPI.getColoredName((Player) sender);
        } else {
            senderName = ChatColor.DARK_RED + "Console";
        }

        String context = lCore.get().getChat().getDelayTime() == 1 ? "" : "s";

        if (lCore.get().getChat().isPublicChatDelayed()) {
            Bukkit.broadcastMessage(Locale.DELAY_CHAT_ENABLED_BROADCAST.format(senderName,
                lCore.get().getChat().getDelayTime(), context));
        } else {
            Bukkit.broadcastMessage(Locale.DELAY_CHAT_DISABLED_BROADCAST.format(senderName));
        }
    }

    public void execute(CommandSender sender, Number secondsN) {
        int seconds = secondsN.intValue();
        if (seconds < 0 || seconds > 60) {
            sender.sendMessage(ChatColor.RED + "A delay can only be between 1-60 seconds.");
            return;
        }

        String context = seconds == 1 ? "" : "s";

        sender.sendMessage(ChatColor.GREEN + "You have updated the chat delay to " + seconds + " second" + context + ".");
        lCore.get().getChat().setDelayTime(seconds);
    }

}
