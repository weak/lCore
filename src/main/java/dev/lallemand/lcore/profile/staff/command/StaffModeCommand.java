package dev.lallemand.lcore.profile.staff.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"staffmode", "mod"}, permission = "lcore.staff.staffmode")
public class StaffModeCommand {
    public void execute(Player player, boolean toggle) {
        Profile profile = Profile.getByPlayer(player);
        profile.getStaffOptions().setStaffModeEnabled(toggle);

        player.sendMessage(profile.getStaffOptions().isStaffModeEnabled() ?
            CC.GREEN + "You are now in staff mode." : CC.RED + "You are no longer in staff mode.");
        profile.toggleStaffMode();
    }

    public void execute(Player player) {
        Profile profile = Profile.getByPlayer(player);
        profile.getStaffOptions().setStaffModeEnabled(!profile.getStaffOptions().isStaffModeEnabled());

        profile.getPlayer().sendMessage(profile.getStaffOptions().isStaffModeEnabled() ?
            CC.GREEN + "You are now in staff mode." : CC.RED + "You are no longer in staff mode.");
        profile.toggleStaffMode();
    }
}
