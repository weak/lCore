package dev.lallemand.lcore.profile.staff.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.network.event.FreezePlayerEvent;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.player.PlayerUtil;
import dev.lallemand.lcore.util.string.CC;

import java.util.List;

@CommandMeta(label = {"freeze", "ss", "screenshare"}, async = true, permission = "lcore.staff.freeze")
public class FreezeCommand {

    public void execute(CommandSender sender, @CPL("player") Player target) {
        Profile profile = Profile.getByUuid(target.getUniqueId());
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        Profile staff = Profile.getByUsername(sender.getName());
        if (staff == null || !staff.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile == staff) {
            sender.sendMessage(CC.RED + "You cannot screen-share yourself.");
            return;
        }

        if (profile.isFrozen()) {
            String format = Locale.STAFF_UNFREEZE_MESSAGE.format(profile.getColoredUsername(), staff.getColoredUsername());
            CC.sendStaff(format);
            profile.setFrozen(false);
            PlayerUtil.allowMovement(target);
            new FreezePlayerEvent(sender, target, false).call();
            return;
        }

        profile.setFrozen(true);
        String format = Locale.STAFF_FREEZE_MESSAGE.format(profile.getColoredUsername(), staff.getColoredUsername());
        CC.sendStaff(format);
        PlayerUtil.denyMovement(target);
        new FreezePlayerEvent(sender, target, true).call();
        List<String> messages = Locale.STAFF_FREEZE.formatLines();
        for (String message : messages) {
            target.sendMessage(message);
        }
    }

}