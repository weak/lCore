package dev.lallemand.lcore.profile.staff.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "socialspy", permission = "lcore.staff.socialspy")
public class SocialSpyCommand {

    public void excute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getStaffOptions().setSocialSpy(!profile.getStaffOptions().isSocialSpy());
        player.sendMessage(CC.translate("&eSocial spy has been " +
            (profile.getStaffOptions().isSocialSpy() ? "&aenabled" : "&cdisabled") + "&e."));
    }

}
