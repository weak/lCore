package dev.lallemand.lcore.profile.staff.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.BungeeUtil;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"staffswitch", "srv"}, async = true, permission = "lcore.staff.join")
public class JoinCommand {

    public void execute(Player player, @CPL("serverName") String serverName) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        /*if (!profile.isAuthenticated()) {
            player.sendMessage(Locale.SECURITY_AUTH_REQUIRED.format());
            return;
        }*/

        player.sendMessage(CC.GREEN + "Connecting to " + CC.WHITE + serverName);
        BungeeUtil.connect(player, serverName);
    }

}
