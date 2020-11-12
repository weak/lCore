package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "rename", permission = "lcore.rename")
public class RenameCommand {

    public void execute(Player player, String name) {
        if (player.getItemInHand() != null) {
            ItemStack itemStack = player.getItemInHand();
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(CC.translate(name));
            itemStack.setItemMeta(itemMeta);

            player.updateInventory();
            player.sendMessage(CC.GREEN + "You renamed the item in your hand.");
        } else {
            player.sendMessage(CC.RED + "There is nothing in your hand.");
        }
    }

}