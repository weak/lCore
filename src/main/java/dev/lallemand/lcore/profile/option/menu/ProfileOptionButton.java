package dev.lallemand.lcore.profile.option.menu;

import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.lallemand.lcore.util.item.ItemBuilder;
import dev.lallemand.lcore.util.menu.Button;
import dev.lallemand.lcore.util.string.CC;
import dev.lallemand.lcore.util.string.TextSplitter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public abstract class ProfileOptionButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(isEnabled(player) ? getEnabledItem(player) : getDisabledItem(player));

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.addAll(TextSplitter.split(40, getDescription(), CC.GRAY, " "));
        lore.add("");
        lore.add((isEnabled(player) ? CC.BLUE + StringEscapeUtils.unescapeJava(" » ") : "   ") + "&e" + getEnabledOption());
        lore.add((!isEnabled(player) ? CC.BLUE + StringEscapeUtils.unescapeJava(" » ") : "   ") + "&e" + getDisabledOption());
        lore.add("");
        lore.add("&eClick to toggle this option.");

        return itemBuilder.name(getOptionName())
            .lore(lore)
            .build();
    }

    public abstract ItemStack getEnabledItem(Player player);

    public abstract ItemStack getDisabledItem(Player player);

    public abstract String getOptionName();

    public abstract String getDescription();

    public abstract String getEnabledOption();

    public abstract String getDisabledOption();

    public abstract boolean isEnabled(Player player);

    @Override
    public boolean shouldUpdate(Player player, ClickType clickType) {
        return true;
    }

}
