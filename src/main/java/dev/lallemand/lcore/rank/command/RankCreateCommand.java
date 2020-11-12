package dev.lallemand.lcore.rank.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.util.string.CC;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.rank.Rank;

@CommandMeta(label = "rank create", permission = "lcore.admin.rank", async = true)
public class RankCreateCommand {

    public void execute(CommandSender sender, @CPL("rank") String name) {
        if (Rank.getRankByDisplayName(name) != null) {
            sender.sendMessage(CC.RED + "A rank with that name already exists.");
            return;
        }

        Rank rank = new Rank(name);
        rank.save();

        sender.sendMessage(CC.GREEN + "You created a new rank.");
    }

}
