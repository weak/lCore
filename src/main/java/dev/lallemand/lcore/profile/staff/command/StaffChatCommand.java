package dev.lallemand.lcore.profile.staff.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.network.packet.PacketStaffChat;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"staffchat", "sc"}, permission = "lcore.staff.staffchat")
public class StaffChatCommand {

    public void execute(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        profile.getStaffOptions().setStaffChatModeEnabled(!profile.getStaffOptions().isStaffChatModeEnabled());

        player.sendMessage(profile.getStaffOptions().isStaffChatModeEnabled() ?
            CC.GREEN + "You are now talking in staff chat." : CC.RED + "You are no longer talking in staff chat.");
    }

    public void execute(Player player, @CPL("message") String message) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        lCore.get().getPidgin().sendPacket(new PacketStaffChat(profile.getColoredUsername(),
            Bukkit.getServerName(), message));
    }

}
