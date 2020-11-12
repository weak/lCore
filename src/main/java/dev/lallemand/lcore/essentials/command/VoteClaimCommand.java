package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.util.player.PlayerUtil;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "voteclaim")
public class VoteClaimCommand {
    public void execute(Player player) {
        if (!PlayerUtil.isLiked(player.getUniqueId())) {
            player.sendMessage(CC.translate("&cYou have not voted on NameMC."));
            return;
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "perms add " + player.getName() + " lcore.prefix.verified");
        player.sendMessage(CC.translate("Thanks for vote!"));
    }
}
