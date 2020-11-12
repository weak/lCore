package dev.lallemand.lcore.rank.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.util.string.CC;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.rank.Rank;

@CommandMeta(label = "rank delete", permission = "lcore.admin.rank", async = true)
public class RankDeleteCommand {

    public void execute(CommandSender sender, @CPL("rank") Rank rank) {
        if (rank == null) {
            sender.sendMessage(Locale.RANK_NOT_FOUND.format());
            return;
        }

        rank.delete();

        sender.sendMessage(CC.GREEN + "You deleted the rank.");
    }

}
