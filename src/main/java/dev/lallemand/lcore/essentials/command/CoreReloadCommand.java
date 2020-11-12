package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "lcore reload", permission = "lcore.reload")
public class CoreReloadCommand {

    public void execute(CommandSender sender) {
        lCore.get().getEssentials().reload();
        sender.sendMessage(CC.translate("&alcore reloaded."));
    }

}
