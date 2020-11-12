package dev.lallemand.lcore.network.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.network.packet.PacketStaffRequest;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.profile.ProfileCooldown;

@CommandMeta(label = {"request", "helpop"}, async = true)
public class RequestCommand {

    public void execute(Player player, @CPL("string") String reason) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (!profile.getRequestCooldown().hasExpired()) {
            player.sendMessage(ChatColor.RED + "You cannot request assistance that quickly. Try again later.");
            return;
        }

        profile.setRequestCooldown(new ProfileCooldown(120_000L));
        player.sendMessage(Locale.STAFF_REQUEST_SUBMITTED.format());

        lCore.get().getPidgin().sendPacket(new PacketStaffRequest(
            profile.getColoredUsername(),
            reason,
            Bukkit.getServerName()
        ));
    }

}
