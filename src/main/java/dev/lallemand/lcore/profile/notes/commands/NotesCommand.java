package dev.lallemand.lcore.profile.notes.commands;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.profile.notes.menu.NotesMenu;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "notes", permission = "lcore.notes")
public class NotesCommand {

    public void execute(Player player, @CPL("player") Profile profile) {
        if (profile.getNotes().isEmpty()) {
            player.sendMessage(CC.translate("&c" + profile.getName() + "Has no notes."));
            return;
        }
        new NotesMenu(profile).openMenu(player);
    }

}
