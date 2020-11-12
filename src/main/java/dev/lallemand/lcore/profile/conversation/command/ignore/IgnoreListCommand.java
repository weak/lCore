package dev.lallemand.lcore.profile.conversation.command.ignore;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "ignore list", async = true)
public class IgnoreListCommand {
    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (profile.getIgnoreList().isEmpty()) {
            player.sendMessage(CC.translate("&eYou are not ignoring anyone."));
            return;
        }
        player.sendMessage(CC.translate("&7&lIgnore List"));
        player.sendMessage(CC.SB_BAR);
        profile.getIgnoreList().forEach(uuid -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            player.sendMessage(CC.translate(" &7-&f" + offlinePlayer.getName()));
        });
        player.sendMessage(CC.SB_BAR);
    }
}
