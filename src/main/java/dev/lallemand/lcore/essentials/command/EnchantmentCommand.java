package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import dev.lallemand.lcore.util.item.EnchantmentUtil;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "enchantment", permission = "lcore.enchantment")
public class EnchantmentCommand {

    public void excute(Player player, @CPL("enchantment") String enchantment, @CPL("level") String level) {
        ItemStack hand = player.getItemInHand();
        if (hand == null) {
            player.sendMessage(CC.translate("&cYou don't have any item in your hand."));
            return;
        }
        if (EnchantmentUtil.getByName(enchantment) == null) {
            player.sendMessage(CC.translate("&cEnchantment not found."));
            return;
        }
        hand.addUnsafeEnchantment(EnchantmentUtil.getByName(enchantment), Integer.parseInt(level));
        player.sendMessage(CC.translate("&eApplied &c" + enchantment + " " + level + " &eto &c"
            + StringUtils.capitalize(hand.getType().name().toLowerCase().replace("_", " "))));
        Bukkit.getOnlinePlayers().forEach(staff -> {
            if (staff.hasPermission("lcore.enchantment.alert")) {
                staff.sendMessage(CC.translate("&7(" + player.getName() + "&7) &eApplied &c" + enchantment + " " + level + " &eto &c"
                    + StringUtils.capitalize(hand.getType().name().toLowerCase().replace("_", " "))));
            }
        });
    }

}
