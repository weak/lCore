package dev.lallemand.lcore.rank.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank inherit", permission = "lcore.admin.rank", async = true)
public class RankInheritCommand {

    public void execute(CommandSender sender, @CPL("parent") Rank parent, @CPL("child") Rank child) {
        if (parent == null) {
            sender.sendMessage(ChatColor.RED + "A rank with that name does not exist (parent).");
            return;
        }

        if (child == null) {
            sender.sendMessage(ChatColor.RED + "A rank with that name does not exist (child).");
            return;
        }

        if (parent.canInherit(child)) {
            parent.getInherited().add(child);
            parent.save();
            parent.refresh();

            sender.sendMessage(ChatColor.GREEN + "You made the parent rank " + parent.getDisplayName() +
                " inherit the child rank " + child.getDisplayName() + ".");
        } else {
            sender.sendMessage(ChatColor.RED + "That parent rank cannot inherit that child rank.");
        }
    }

}
