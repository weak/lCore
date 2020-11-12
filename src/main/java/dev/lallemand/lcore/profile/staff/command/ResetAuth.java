package dev.lallemand.lcore.profile.staff.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.profile.Profile;

@CommandMeta(label = "resetauth", async = true, permission = "lcore.staff.resetauth")
public class ResetAuth {

    public void execute(CommandSender sender, @CPL("player") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        profile.setAuthenticated(false);
        profile.setSecretToken(null);
        profile.save();

        sender.sendMessage(Locale.SECURITY_AUTH_RESET.format(profile.getPlayer()));
    }

}
