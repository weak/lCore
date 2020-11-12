package dev.lallemand.lcore.profile.staff.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"togglestaffchat", "tsc"}, permission = "lcore.staff")
public class ToggleStaffChat {
    public void execute(Player player) {
        Profile profile = Profile.getByPlayer(player);
        profile.getStaffOptions().setReceiveStaffChat(!profile.getStaffOptions().isReceiveStaffChat());

        profile.getPlayer().sendMessage(profile.getStaffOptions().isStaffModeEnabled() ?
            CC.GREEN + "Receiving messages from the staff channel." : CC.RED + "No longer receiving messages from the staff channel.");
    }
}
