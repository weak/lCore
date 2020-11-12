package dev.lallemand.lcore.profile.conversation.command.ignore;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "ignore")
public class IgnoreCommand {
    public void execute(Player player) {
        player.sendMessage(CC.translate("&7&lIgnore commands"));
        player.sendMessage(CC.translate("&e/ignore add &7(name)"));
        player.sendMessage(CC.translate("&e/ignore remove &7(name)"));
        player.sendMessage(CC.translate("&e/ignore list"));
    }
}
