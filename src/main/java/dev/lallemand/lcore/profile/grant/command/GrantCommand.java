package dev.lallemand.lcore.profile.grant.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.profile.grant.Grant;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.network.packet.PacketAddGrant;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.profile.grant.event.GrantAppliedEvent;
import dev.lallemand.lcore.rank.Rank;
import dev.lallemand.lcore.util.TimeUtil;
import dev.lallemand.lcore.util.duration.Duration;
import dev.lallemand.lcore.util.string.CC;

import java.util.UUID;

@CommandMeta(label = "grant", async = true, permission = "lcore.grants.add")
public class GrantCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile, @CPL("rank") Rank rank, @CPL("duration") Duration duration, @CPL("string") String reason) {
        if (rank == null) {
            sender.sendMessage(Locale.RANK_NOT_FOUND.format());
            return;
        }

        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (duration.getValue() == -1) {
            sender.sendMessage(CC.RED + "That duration is not valid.");
            sender.sendMessage(CC.RED + "Example: [perm/1y1m1w1d]");
            return;
        }

        UUID addedBy = sender instanceof Player ? ((Player) sender).getUniqueId() : null;
        Grant grant = new Grant(UUID.randomUUID(), rank, addedBy, System.currentTimeMillis(), reason,
            duration.getValue());

        profile.setActiveGrant(grant);
        profile.save();

        lCore.get().getPidgin().sendPacket(new PacketAddGrant(profile.getUuid(), grant));

        sender.sendMessage(CC.GREEN + "You applied a `{rank}` grant to `{player}` for {time-remaining}."
            .replace("{rank}", rank.getDisplayName())
            .replace("{player}", profile.getName())
            .replace("{time-remaining}", duration.getValue() == Long.MAX_VALUE ? "forever"
                : TimeUtil.millisToRoundedTime(duration.getValue())));

        Player player = profile.getPlayer();

        if (player != null) {
            new GrantAppliedEvent(player, grant).call();
        }
    }

}
