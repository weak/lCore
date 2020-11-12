package dev.lallemand.lcore.rank.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.rank.Rank;
import dev.lallemand.lcore.util.string.CC;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank setweight", permission = "lcore.admin.rank", async = true)
public class RankSetWeightCommand {

    public void execute(CommandSender sender, @CPL("rank") Rank rank, @CPL("weight") String weight) {
        if (rank == null) {
            sender.sendMessage(CC.RED + "A rank with that name does not exist.");
            return;
        }

        try {
            Integer.parseInt(weight);
        } catch (Exception e) {
            sender.sendMessage(CC.RED + "Invalid number.");
            return;
        }

        rank.setPriority(Integer.parseInt(weight));
        rank.save();
        rank.refresh();

        sender.sendMessage(CC.GREEN + "You updated the rank's weight.");
    }

}
