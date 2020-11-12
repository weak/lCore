package dev.lallemand.lcore.network.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.network.packet.PacketStaffReport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.lCoreAPI;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.profile.ProfileCooldown;

@CommandMeta(label = "report", async = true)
public class ReportCommand {

    public void execute(Player player, @CPL("player") Player target, @CPL("string") String reason) {
        if (target == null) {
            player.sendMessage(Locale.PLAYER_NOT_FOUND.format());
            return;
        }

        if (player.equals(target)) {
            player.sendMessage(ChatColor.RED + "You cannot report yourself.");
            return;
        }

        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (!profile.getRequestCooldown().hasExpired()) {
            player.sendMessage(ChatColor.RED + "You cannot request assistance that quickly. Try again later.");
            return;
        }

        lCore.get().getPidgin().sendPacket(new PacketStaffReport(
            profile.getColoredUsername(),
            lCoreAPI.getColoredName(target),
            reason,
            Bukkit.getServerName()
        ));

        profile.setRequestCooldown(new ProfileCooldown(120_000L));
        player.sendMessage(Locale.STAFF_REQUEST_SUBMITTED.format());
    }

}
