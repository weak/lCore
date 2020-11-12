package dev.lallemand.lcore.profile.notes.commands;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.profile.notes.Note;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "note add", permission = "lcore.note.add")
public class NoteAddCommand {

    public void execute(Player player, @CPL("player") Profile profile, String noteString) {

        int id = profile.getNotes().size() + 1;

        profile.getNotes().add(new Note(id, noteString, player));

        player.sendMessage(CC.translate("&aNote successfully added to &f" + profile.getName()));
    }


}
