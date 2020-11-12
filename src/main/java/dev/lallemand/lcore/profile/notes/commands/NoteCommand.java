package dev.lallemand.lcore.profile.notes.commands;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "note", permission = "lcore.note")
public class NoteCommand {
    public void execute(Player player) {
        player.sendMessage(CC.translate("&7&lNote commands"));
        player.sendMessage(CC.translate("&e/note add &7(name) (note)"));
        player.sendMessage(CC.translate("&e/note update &7(name) (note id) (new_note)"));
        player.sendMessage(CC.translate("&e/note remove &7(name) (note id)"));
        player.sendMessage(CC.translate("&e/notes"));
    }
}
