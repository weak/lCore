package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.util.player.PlayerUtil;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "vote")
public class VoteCommand {

    public void execute(Player player) {
        if (PlayerUtil.isLiked(player.getUniqueId())) {
            player.sendMessage(CC.translate("&cYou have already voted on NameMC."));
            return;
        }
        player.sendMessage(CC.translate(
            "&eLogin with your premium account and give us a like on &ahttps://namemc.com/server/lallemand.dev &eto recieve the " +
                "\"&7[&a&l✔&7] &aVerified&e\" prefix"));
    }

}
