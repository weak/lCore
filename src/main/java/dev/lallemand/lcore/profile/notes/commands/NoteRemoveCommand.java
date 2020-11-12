package dev.lallemand.lcore.profile.notes.commands;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "note remove", permission = "lcore.note.remove")
public class NoteRemoveCommand {

    public void execute(Player player, @CPL("player") Profile profile, Number noteID) {
        if (!profile.removeNote(noteID.intValue())) {
            player.sendMessage(CC.translate("&7That note doesn't exist."));
            return;
        }
        player.sendMessage(CC.translate("&cNote #" + noteID + "&c successfully remove of &f" + profile.getName()));
    }
}
