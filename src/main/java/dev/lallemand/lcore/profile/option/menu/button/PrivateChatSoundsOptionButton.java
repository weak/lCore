package dev.lallemand.lcore.profile.option.menu.button;

import dev.lallemand.lcore.profile.option.menu.ProfileOptionButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.item.ItemBuilder;

public class PrivateChatSoundsOptionButton extends ProfileOptionButton {

    @Override
    public String getOptionName() {
        return "&e&lSounds";
    }

    @Override
    public ItemStack getEnabledItem(Player player) {
        return new ItemBuilder(Material.ANVIL).build();
    }

    @Override
    public ItemStack getDisabledItem(Player player) {
        return new ItemBuilder(Material.ANVIL).build();
    }

    @Override
    public String getDescription() {
        return "If enabled, a sound will be played when you receive a private chat message.";
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
        return Profile.getProfiles().get(player.getUniqueId()).getOptions().playingMessageSounds();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.getOptions().playingMessageSounds(!profile.getOptions().playingMessageSounds());
        if (profile.getOptions().playingMessageSounds()) {
            player.sendMessage(Locale.OPTIONS_PRIVATE_MESSAGE_SOUND_ENABLED.format());
        } else {
            player.sendMessage(Locale.OPTIONS_PRIVATE_MESSAGE_SOUND_DISABLED.format());
        }
        player.closeInventory();
    }

}
