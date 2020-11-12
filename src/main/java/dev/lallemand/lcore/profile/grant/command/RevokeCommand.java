package dev.lallemand.lcore.profile.grant.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "revoke", async = true, permission = "lcore.grants.revoke")
public class RevokeCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile) {
        profile.setActiveGrant(null);
        profile.checkGrants();
        if (profile.getPlayer() != null)
            profile.getPlayer().sendMessage(CC.YELLOW + "Your rank has been revoked to default.");
        profile.save();
        sender.sendMessage(CC.YELLOW + profile.getName() + " rank has been revoked to default.");
    }

}
