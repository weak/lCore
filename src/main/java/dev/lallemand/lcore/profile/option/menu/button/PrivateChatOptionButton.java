package dev.lallemand.lcore.profile.option.menu.button;

import dev.lallemand.lcore.profile.option.menu.ProfileOptionButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.item.ItemBuilder;

public class PrivateChatOptionButton extends ProfileOptionButton {

    @Override
    public String getOptionName() {
        return "&c&lPrivate Messages";
    }

    @Override
    public ItemStack getEnabledItem(Player player) {
        return new ItemBuilder(Material.NAME_TAG).build();
    }

    @Override
    public ItemStack getDisabledItem(Player player) {
        return new ItemBuilder(Material.NAME_TAG).build();
    }

    @Override
    public String getDescription() {
        return "If enabled, you will receive private chat messages.";
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
        return Profile.getProfiles().get(player.getUniqueId()).getOptions().receivingNewConversations();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.getOptions().receivingNewConversations(!profile.getOptions().receivingNewConversations());
        if (profile.getOptions().receivingNewConversations()) {
            player.sendMessage(Locale.OPTIONS_PRIVATE_MESSAGES_ENABLED.format());
        } else {
            player.sendMessage(Locale.OPTIONS_PRIVATE_MESSAGES_DISABLED.format());
        }
        player.closeInventory();
    }

}
