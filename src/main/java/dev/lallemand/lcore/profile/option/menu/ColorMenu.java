package dev.lallemand.lcore.profile.option.menu;

import lombok.AllArgsConstructor;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.BukkitUtil;
import dev.lallemand.lcore.util.item.ItemBuilder;
import dev.lallemand.lcore.util.menu.Button;
import dev.lallemand.lcore.util.menu.Menu;
import dev.lallemand.lcore.util.string.CC;

import java.util.HashMap;
import java.util.Map;

public class ColorMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Color Menu";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Map<ChatColor, DyeColor> colors = new HashMap<>(BukkitUtil.CHAT_DYE_COLOUR_MAP);

        colors.forEach((chatColor, dyeColor) -> buttons.put(buttons.size(), new ColorButton(chatColor, dyeColor)));

        return buttons;
    }

    @AllArgsConstructor
    public class ColorButton extends Button {

        ChatColor color;
        DyeColor dyeColor;

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Profile.getByPlayer(player);
            return new ItemBuilder(Material.WOOL)
                .dyeColor(dyeColor)
                .name(color + StringUtils.capitalize(color.name().toLowerCase().replace("_", " ")))
                .lore((color == profile.getOptions().color() ? "&aColor already selected" : "&7Click to select this color") + '!')
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            Profile profile = Profile.getByPlayer(player);
            profile.getOptions().color(color);
            player.sendMessage(CC.translate("&aYour private messages will now display in " + color + StringUtils.capitalize(color.name().toLowerCase().replace("_", " ")) + "&a."));
            player.closeInventory();

        }
    }
}
