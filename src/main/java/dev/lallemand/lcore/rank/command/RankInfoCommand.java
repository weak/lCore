package dev.lallemand.lcore.rank.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.rank.Rank;
import dev.lallemand.lcore.util.string.CC;
import dev.lallemand.lcore.util.string.TextSplitter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.Locale;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CommandMeta(label = "rank info", permission = "lcore.admin.rank", async = true)
public class RankInfoCommand {

    public void execute(CommandSender sender, @CPL("rank") Rank rank) {
        if (rank == null) {
            sender.sendMessage(Locale.RANK_NOT_FOUND.format());
        } else {
            List<String> toSend = new ArrayList<>();
            toSend.add(CC.CHAT_BAR);
            toSend.add(ChatColor.GOLD + "Rank Information " + ChatColor.GRAY + "(" + ChatColor.RESET +
                rank.getColor() + rank.getDisplayName() + ChatColor.GRAY + ")");

            toSend.add(ChatColor.GRAY + "Weight: " + ChatColor.RESET + rank.getPriority());
            toSend.add(ChatColor.GRAY + "Prefix: " + ChatColor.RESET + rank.getPrefix() + "Example");

            List<String> permissions = rank.getAllPermissions();

            toSend.add("");
            toSend.add(ChatColor.GRAY + "Permissions: " + ChatColor.RESET + "(" + permissions.size() + ")");

            if (!permissions.isEmpty()) {
                toSend.addAll(TextSplitter.split(46, StringUtils.join(permissions, " "), "", ", "));
            }

            List<Rank> inherited = rank.getInherited();

            toSend.add("");
            toSend.add(ChatColor.GRAY + "Inherits: " + ChatColor.RESET + "(" + inherited.size() + ")");

            if (!rank.getInherited().isEmpty()) {
                List<String> rankNames = rank.getInherited()
                    .stream()
                    .map(inheritedRank -> inheritedRank.getColor() + inheritedRank.getDisplayName())
                    .collect(Collectors.toList());

                toSend.addAll(TextSplitter.split(46, StringUtils.join(rankNames, " "), "", ", "));
            }

            toSend.add(CC.CHAT_BAR);

            for (String line : toSend) {
                sender.sendMessage(line);
            }
        }
    }

}
