package dev.lallemand.lcore.rank.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.util.string.CC;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.rank.Rank;

@CommandMeta(label = {"rank addpermission", "rank addperm"}, permission = "lcore.admin.rank", async = true)
public class RankAddPermissionCommand {

    public void execute(CommandSender sender, @CPL("rank") Rank rank, @CPL("permission") String permission) {
        if (rank == null) {
            sender.sendMessage(Locale.RANK_NOT_FOUND.format());
            return;
        }

        permission = permission.toLowerCase().trim();

        if (rank.getPermissions().contains(permission)) {
            sender.sendMessage(CC.RED + "That rank is already assigned that permission.");
            return;
        }

        for (Rank childRank : rank.getInherited()) {
            if (childRank.hasPermission(permission)) {
                sender.sendMessage(CC.RED + "That rank is inheriting that permission from the " +
                    rank.getColor() + rank.getDisplayName() + " rank.");
                return;
            }
        }

        rank.getPermissions().add(permission);
        rank.save();
        rank.refresh();

        sender.sendMessage(CC.GREEN + "Successfully added permission to rank.");
    }

}
