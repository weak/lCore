package dev.lallemand.lcore.rank.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.rank.Rank;
import dev.lallemand.lcore.util.string.CC;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank setprefix", permission = "lcore.admin.rank", async = true)
public class RankSetPrefixCommand {

    public void execute(CommandSender sender, @CPL("rank") Rank rank, @CPL("prefix") String prefix) {
        if (rank == null) {
            sender.sendMessage(CC.RED + "A rank with that name does not exist.");
            return;
        }

        rank.setPrefix(CC.translate(prefix));
        rank.save();
        rank.refresh();

        sender.sendMessage(CC.GREEN + "You updated the rank's prefix.");
    }

}
