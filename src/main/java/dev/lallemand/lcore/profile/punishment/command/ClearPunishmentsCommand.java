package dev.lallemand.lcore.profile.punishment.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.profile.punishment.Punishment;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.network.packet.PacketClearPunishments;
import dev.lallemand.lcore.profile.Profile;

@CommandMeta(label = "clearpunishments", permission = "lcore.admin.clearpunishments", async = true)
public class ClearPunishmentsCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile) {
        if (profile == null) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }


        profile.getPunishments().forEach(Punishment::delete);

        profile.getPunishments().clear();
        profile.save();

        lCore.get().getPidgin().sendPacket(new PacketClearPunishments(profile.getUuid()));

        sender.sendMessage(ChatColor.GREEN + "Cleared punishments of " + profile.getColoredUsername() +
            ChatColor.GREEN + "!");
    }

}
