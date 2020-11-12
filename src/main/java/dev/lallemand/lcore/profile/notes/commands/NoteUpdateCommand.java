package dev.lallemand.lcore.profile.notes.commands;


import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.profile.notes.Note;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "note update", permission = "lcore.note.update")
public class NoteUpdateCommand {

    public void execute(Player player, @CPL("player") Profile profile, Number noteID, @CPL("update") String noteupdate) {
        if (profile.getNote(noteID.intValue()) == null) {
            player.sendMessage(CC.translate("&7That note doesn't exist."));
            return;
        }
        Note note = profile.getNote(noteID.intValue());
        note.setNote(noteupdate);
        note.setUpdateAt(System.currentTimeMillis());
        note.setUpdateBy(player.getName());
        player.sendMessage(CC.translate("&eNote #" + noteID + "&e successfully update of &f" + profile.getName()));
    }
}
