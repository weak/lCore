package dev.lallemand.lcore.rank.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.rank.Rank;

import java.util.ArrayList;
import java.util.List;

@CommandMeta(label = "ranks", permission = "lcore.admin.rank")
public class RanksCommand {

    public void execute(CommandSender sender) {
        List<Rank> ranks = new ArrayList<>(Rank.getRanks().values());
        ranks.sort((o1, o2) -> o2.getPriority() - o1.getPriority());

        sender.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "Ranks");

        for (Rank rank : ranks) {
            sender.sendMessage(ChatColor.GRAY + " - " + rank.getPrefix() + ChatColor.RESET + rank.getColor() + rank.getDisplayName() +
                ChatColor.RESET + " (Weight: " + rank.getPriority() + ")");
        }
    }

}
