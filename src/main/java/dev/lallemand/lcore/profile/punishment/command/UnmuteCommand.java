package dev.lallemand.lcore.profile.punishment.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.CommandOption;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.network.packet.PacketBroadcastPunishment;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.profile.punishment.Punishment;
import dev.lallemand.lcore.profile.punishment.PunishmentType;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "unmute", permission = "lcore.staff.unmute", async = true, options = "s")
public class UnmuteCommand {

    public void execute(CommandSender sender, CommandOption option, @CPL("player") Profile profile, @CPL("reason") String reason) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.getActivePunishmentByType(PunishmentType.MUTE) == null) {
            sender.sendMessage(CC.RED + "That player is not muted.");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
            .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = profile.getActivePunishmentByType(PunishmentType.MUTE);
        punishment.setRemovedAt(System.currentTimeMillis());
        punishment.setRemovedReason(reason);
        punishment.setRemoved(true);
        punishment.setTargetID(profile.getUuid());
        punishment.save();
        if (sender instanceof Player) {
            punishment.setRemovedBy(((Player) sender).getUniqueId());
        }

        profile.save();

        lCore.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
            profile.getColoredUsername(), profile.getUuid(), option != null, Bukkit.getServerName()));
    }

}
