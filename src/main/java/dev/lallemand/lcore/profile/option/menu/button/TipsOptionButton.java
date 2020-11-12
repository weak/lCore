package dev.lallemand.lcore.profile.option.menu.button;

import dev.lallemand.lcore.profile.option.menu.ProfileOptionButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.item.ItemBuilder;

public class TipsOptionButton extends ProfileOptionButton {
    @Override
    public ItemStack getEnabledItem(Player player) {
        return new ItemBuilder(Material.SIGN).build();
    }

    @Override
    public ItemStack getDisabledItem(Player player) {
        return new ItemBuilder(Material.SIGN).build();
    }

    @Override
    public String getOptionName() {
        return "&7&lTips Announcements";
    }

    @Override
    public String getDescription() {
        return "If enabled, you can see tips.";
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
        return Profile.getProfiles().get(player.getUniqueId()).getOptions().tipsAnnounce();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.getOptions().tipsAnnounce(!profile.getOptions().tipsAnnounce());
        if (profile.getOptions().tipsAnnounce()) {
            player.sendMessage(Locale.OPTIONS_TIPS_ENABLED.format());
        } else {
            player.sendMessage(Locale.OPTIONS_TIPS_DISABLE.format());
        }
        player.closeInventory();
    }
}
