package dev.lallemand.lcore.profile.staff.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"vanish", "v"}, permission = "lcore.vanish")
public class VanishCommand {

    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        boolean toggle = !profile.getStaffOptions().isVanish();
        profile.getStaffOptions().setVanish(toggle);
        player.sendMessage(profile.getStaffOptions().isVanish() ?
            CC.GREEN + "You are now vanished." : CC.RED + "You are no longer vanished.");
        profile.toggleVanish();
    }

    public void execute(Player player, Boolean toggle) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getStaffOptions().setVanish(toggle);
        player.sendMessage(profile.getStaffOptions().isVanish() ?
            CC.GREEN + "You are now vanished." : CC.RED + "You are no longer vanished.");
        profile.toggleVanish();
    }

}
