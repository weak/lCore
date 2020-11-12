package dev.lallemand.lcore.profile.punishment.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.profile.punishment.menu.PunishmentsListMenu;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.cache.RedisPlayerData;
import dev.lallemand.lcore.profile.Profile;

@CommandMeta(label = {"check", "c", "hist", "history"}, permission = "lcore.staff.check", async = true)
public class CheckCommand {

    public void execute(Player player, @CPL("player") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        RedisPlayerData redisPlayerData = lCore.get().getRedisCache().getPlayerData(profile.getUuid());
        if (redisPlayerData == null) {
            player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        new PunishmentsListMenu(profile, redisPlayerData).openMenu(player);
    }

}
