package dev.lallemand.lcore.profile.option.menu.button;

import dev.lallemand.lcore.profile.option.menu.ProfileOptionButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.item.ItemBuilder;

public class ScoreboardOptionButton extends ProfileOptionButton {
    @Override
    public ItemStack getEnabledItem(Player player) {
        return new ItemBuilder(Material.ITEM_FRAME).build();
    }

    @Override
    public ItemStack getDisabledItem(Player player) {
        return new ItemBuilder(Material.ITEM_FRAME).build();
    }

    @Override
    public String getOptionName() {
        return "&9&lScoreboard";
    }

    @Override
    public String getDescription() {
        return "If enabled, you can see scoreboard.";
    }

    @Override
    public String getEnabledOption() {
        return "&aEnable";
    }

    @Override
    public String getDisabledOption() {
        return "&cDisable";
    }

    @Override
    public boolean isEnabled(Player player) {
        return Profile.getProfiles().get(player.getUniqueId()).getOptions().scoreboard();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.getOptions().scoreboard(!profile.getOptions().scoreboard());
        if (profile.getOptions().scoreboard()) {
            player.sendMessage(Locale.OPTIONS_SCOREBOARD_ENABLE.format());
        } else {
            player.sendMessage(Locale.OPTIONS_SCOREBOARD_DISABLE.format());
        }
        player.closeInventory();
    }
}
