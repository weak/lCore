package dev.lallemand.lcore.profile.punishment.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.CommandOption;
import dev.lallemand.lcore.profile.punishment.Punishment;
import dev.lallemand.lcore.profile.punishment.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.network.packet.PacketBroadcastPunishment;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "kick", permission = "lcore.staff.kick", async = true, options = "s")
public class KickCommand {

    public void execute(CommandSender sender, CommandOption option, @CPL("player") Player player, @CPL("reason") String reason) {
        if (player == null) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        Profile profile = Profile.getProfiles().get(player.getUniqueId());

        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
            .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = new Punishment(PunishmentType.KICK, System.currentTimeMillis(),
            reason, -1, Bukkit.getServerName());

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }

        punishment.setTargetID(player.getUniqueId());
        punishment.save();
        profile.getPunishments().add(punishment);
        profile.save();

        lCore.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
            profile.getColoredUsername(), profile.getUuid(), option != null, Bukkit.getServerName()));

        new BukkitRunnable() {
            @Override
            public void run() {
                player.kickPlayer(punishment.getKickMessage());
            }
        }.runTask(lCore.get());
    }

}
