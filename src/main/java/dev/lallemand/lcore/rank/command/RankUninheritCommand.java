package dev.lallemand.lcore.rank.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank uninherit", permission = "lcore.admin.rank", async = true)
public class RankUninheritCommand {

    public void execute(CommandSender sender, @CPL("parent") Rank parent, @CPL("child") Rank child) {
        if (parent == null) {
            sender.sendMessage(ChatColor.RED + "A rank with that name does not exist (parent).");
            return;
        }

        if (child == null) {
            sender.sendMessage(ChatColor.RED + "A rank with that name does not exist (child).");
            return;
        }

        if (parent.getInherited().remove(child)) {
            parent.save();
            parent.refresh();

            sender.sendMessage(ChatColor.GREEN + "You made the parent rank " + parent.getDisplayName() +
                " no longer inherit the child rank " + child.getDisplayName() + ".");
        } else {
            sender.sendMessage(ChatColor.RED + "That parent rank does not inherit that child rank.");
        }
    }

}
