package dev.lallemand.lcore.profile.conversation.command.ignore;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "ignore remove")
public class IgnoreRemoveCommand {

    public void execute(Player player, Player target) {
        if (target == player) {
            player.sendMessage(CC.translate("&cCan't ignore yourself."));
            return;
        }
        if (target.hasPermission("lcore.ignore.bypass")) {
            player.sendMessage(CC.translate("&cYou can't ignore this player."));
            return;
        }
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (!profile.getIgnoreList().contains(target.getUniqueId())) {
            player.sendMessage(CC.translate("&cYou are not ignoring this player."));
        } else {
            profile.getIgnoreList().remove(target.getUniqueId());
            player.sendMessage(CC.translate("&eYou no longer ignore &f" + target.getName()));
        }
    }

}
