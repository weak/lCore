package dev.lallemand.lcore.rank.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.util.string.CC;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.rank.Rank;

@CommandMeta(label = {"rank removepermission", "rank removeperm", "rank deleteperm", "rank delperm"},
    permission = "lcore.admin.rank",
    async = true)
public class RankRemovePermissionCommand {

    public void execute(CommandSender sender, @CPL("rank") Rank rank, @CPL("permission") String permission) {
        if (rank == null) {
            sender.sendMessage(Locale.RANK_NOT_FOUND.format());
            return;
        }

        permission = permission.toLowerCase().trim();

        if (!rank.getPermissions().contains(permission)) {
            sender.sendMessage(CC.RED + "That rank is not assigned that permission.");
        } else {
            rank.getPermissions().remove(permission);
            rank.save();
            rank.refresh();

            sender.sendMessage(CC.GREEN + "Successfully removed permission from rank.");
        }
    }

}
