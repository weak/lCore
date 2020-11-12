package dev.lallemand.lcore.profile.staff.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.network.packet.PacketStaffAuth;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.TOTP;

import java.security.GeneralSecurityException;

@CommandMeta(label = "auth", async = true, permission = "lcore.staff.auth")
public class AuthCommand {

    public void execute(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.isAuthenticated()) {
            player.sendMessage(Locale.SECURITY_ALREADY_AUTH.format());
            return;
        }

        if (profile.getSecretToken() != null) {
            player.sendMessage(Locale.SECURITY_SETUP_EXISTS.format());
            return;
        }

        String secretToken = TOTP.generateBase32Secret();
        profile.setSecretToken(secretToken);
        profile.save();

        player.sendMessage(Locale.SECURITY_SETUP_DONE.format(secretToken));
    }

    public void execute(Player player, @CPL("code") String code) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.isAuthenticated()) {
            player.sendMessage(Locale.SECURITY_ALREADY_AUTH.format());
            return;
        }

        if (profile.getSecretToken() == null) {
            player.sendMessage(Locale.SECURITY_SETUP_NEEDED.format());
            return;
        }

        String expected_code;
        try {
            expected_code = TOTP.generateCurrentNumberString(profile.getSecretToken());
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            player.sendMessage(Locale.UNEXPECTED_ERROR.format(ex.getCause().getMessage()));
            return;
        }

        if (!code.equals(expected_code)) {
            player.sendMessage(Locale.SECURITY_AUTH_INVALID.format());

            lCore.get().getPidgin().sendPacket(new PacketStaffAuth(profile.getColoredUsername(),
                Bukkit.getServerName(), false));

            return;
        }

        profile.setAuthenticated(true);
        profile.save();
        player.sendMessage(Locale.SECURITY_AUTH_VALID.format());

        lCore.get().getPidgin().sendPacket(new PacketStaffAuth(profile.getColoredUsername(),
            Bukkit.getServerName(), true));
    }

}
