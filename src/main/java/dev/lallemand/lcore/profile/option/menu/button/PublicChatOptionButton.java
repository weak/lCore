package dev.lallemand.lcore.profile.option.menu.button;

import dev.lallemand.lcore.profile.option.menu.ProfileOptionButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.item.ItemBuilder;

public class PublicChatOptionButton extends ProfileOptionButton {

    @Override
    public String getOptionName() {
        return "&a&lGlobal Chat";
    }

    @Override
    public ItemStack getEnabledItem(Player player) {
        return new ItemBuilder(Material.BOOK_AND_QUILL).build();
    }

    @Override
    public ItemStack getDisabledItem(Player player) {
        return new ItemBuilder(Material.BOOK_AND_QUILL).build();
    }

    @Override
    public String getDescription() {
        return "If enabled, you will receive public chat messages.";
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
        return Profile.getProfiles().get(player.getUniqueId()).getOptions().publicChatEnabled();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.getOptions().publicChatEnabled(!profile.getOptions().publicChatEnabled());
        if (profile.getOptions().publicChatEnabled()) {
            player.sendMessage(Locale.OPTIONS_GLOBAL_CHAT_ENABLED.format());
        } else {
            player.sendMessage(Locale.OPTIONS_GLOBAL_CHAT_DISABLED.format());
        }
        player.closeInventory();
    }

}
