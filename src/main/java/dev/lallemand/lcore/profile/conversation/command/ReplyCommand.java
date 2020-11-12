package dev.lallemand.lcore.profile.conversation.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.profile.conversation.Conversation;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"reply", "r"})
public class ReplyCommand {

    public void execute(Player player, @CPL("message") String message) {
        Profile playerProfile = Profile.getByUuid(player.getUniqueId());
        Conversation conversation = playerProfile.getConversations().getLastRepliedConversation();

        if (conversation != null) {
            if (conversation.validate()) {
                conversation.sendMessage(player, Bukkit.getPlayer(conversation.getPartner(player.getUniqueId())), message);
            } else {
                player.sendMessage(CC.RED + "You can no longer reply to that player.");
            }
        } else {
            player.sendMessage(CC.RED + "You have nobody to reply to.");
        }
    }

}
