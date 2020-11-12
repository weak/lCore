package dev.lallemand.lcore.profile.staff.command;

import com.google.common.collect.Lists;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.profile.staff.events.PlayerVisibilityChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

import java.util.stream.Collectors;

@CommandMeta(label = "hidestaff", permission = "lcore.hidestaff")
public class HideStaffCommand {

    public void excute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        boolean toggle = !profile.getStaffOptions().isHideStaff();
        profile.getStaffOptions().setHideStaff(toggle);
        player.sendMessage(profile.getStaffOptions().isHideStaff() ?
            CC.GREEN + "You are now hide all staff." : CC.RED + "You are no longer hide all staff.");
        toggle(player);
    }

    public void excute(Player player, Boolean toggle) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getStaffOptions().setHideStaff(toggle);
        player.sendMessage(profile.getStaffOptions().isHideStaff() ?
            CC.GREEN + "You are now hide all staff." : CC.RED + "You are no longer hide all staff.");
        toggle(player);
    }

    public void toggle(Player player) {
        if (player.isOnline()) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (profile.getStaffOptions().isHideStaff()) {
                new PlayerVisibilityChangeEvent(true, profile, Bukkit.getOnlinePlayers().stream()
                    .filter(online -> Profile.getByPlayer(online).getStaffOptions().isVanish() &&
                        Profile.getByPlayer(online).getStaffOptions().isHideStaff())
                    .collect(Collectors.toList()))
                    .call();
                if (lCore.get().getMainConfig().getBoolean("STAFF.VISIBILITY_ENGINE")) {
                    Bukkit.getOnlinePlayers().forEach(online -> {
                        Profile onlineProfile = Profile.getByPlayer(online);
                        if (onlineProfile.getStaffOptions().isVanish() || onlineProfile.getStaffOptions().isStaffModeEnabled()) {
                            player.hidePlayer(online);
                        }
                    });
                }
            } else {
                new PlayerVisibilityChangeEvent(false, profile, Lists.newArrayList(Bukkit.getOnlinePlayers()))
                    .call();
                if (lCore.get().getMainConfig().getBoolean("STAFF.VISIBILITY_ENGINE")) {
                    Bukkit.getOnlinePlayers().forEach(player::showPlayer);
                }
            }
        }
    }
}
