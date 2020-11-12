package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "toggledonoronly", permission = "lcore.donoronly")
public class ToggleDonorOnlyCommand {

    public void execute(CommandSender sender) {
        lCore.get().getEssentials().setDonorOnly(!lCore.get().getEssentials().isDonorOnly());
        boolean donorOnly = lCore.get().getEssentials().isDonorOnly();
        sender.sendMessage(CC.translate("&eYou have " +
            (donorOnly ? "&aactivated" : "&cdisabled") + "&e the donors only mode."));
        lCore.get().getMainConfig().getConfiguration().set("ESSENTIAL.DONOR_ONY", donorOnly);
    }

}
