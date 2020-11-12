package dev.lallemand.lcore.profile.option.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.option.menu.ProfileOptionsMenu;

@CommandMeta(label = {"settings"})
public class OptionsCommand {

    public void execute(Player player) {
        new ProfileOptionsMenu().openMenu(player);
    }

}
