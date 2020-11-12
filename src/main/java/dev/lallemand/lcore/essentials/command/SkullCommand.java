package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.util.item.ItemBuilder;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "skull", permission = "lcore.skull")
public class SkullCommand {

    public void execute(Player player, @CPL("SkullOwner") String targetName) {
        player.getInventory().addItem(new ItemBuilder(Material.SKULL_ITEM).skullOwner(targetName).build());
        player.sendMessage(CC.translate("&eYou have received the head of &f" + targetName));
    }

}
