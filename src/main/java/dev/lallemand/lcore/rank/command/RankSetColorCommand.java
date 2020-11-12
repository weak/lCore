package dev.lallemand.lcore.rank.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.util.string.CC;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.rank.Rank;

@CommandMeta(label = "rank setcolor", permission = "lcore.admin.rank", async = true)
public class RankSetColorCommand {

    public void execute(CommandSender sender, @CPL("rank") Rank rank, @CPL("color") String chatColor) {
        if (rank == null) {
            sender.sendMessage(CC.RED + "A rank with that name does not exist.");
            return;
        }

        if (chatColor == null) {
            sender.sendMessage(CC.RED + "That color is not valid.");
            return;
        }

        rank.setColor(chatColor);
        rank.save();
        rank.refresh();

        sender.sendMessage(CC.GREEN + "You updated the rank's color.");
    }

}
