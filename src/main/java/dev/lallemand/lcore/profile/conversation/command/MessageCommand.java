package dev.lallemand.lcore.profile.conversation.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCoreAPI;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.profile.conversation.Conversation;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"message", "msg", "whisper", "tell", "t", "m"})
public class MessageCommand {

    public void execute(Player player, @CPL("player") Player target, @CPL("message") String message) {
        if (player.equals(target)) {
            player.sendMessage(CC.RED + "You cannot message yourself!");
            //return;
        }

        if (target == null) {
            player.sendMessage(CC.RED + "A player with that name could not be found.");
            return;
        }

        Profile playerProfile = Profile.getByUuid(player.getUniqueId());
        Profile targetProfile = Profile.getByUuid(target.getUniqueId());

        if (targetProfile.getConversations().canBeMessagedBy(player)) {
            Conversation conversation = playerProfile.getConversations().getOrCreateConversation(target);

            if (conversation.validate()) {
                conversation.sendMessage(player, target, message);
            } else {
                player.sendMessage(CC.RED + "That player is not receiving new conversations right now.");
            }
        } else {
            if ((lCoreAPI.isVanish(target) && !player.hasPermission("lcore.staff"))) {
                player.sendMessage(CC.RED + "A player with that name could not be found.");
            } else {
                player.sendMessage(CC.RED + "That player is not receiving new conversations right now.");
            }
        }
    }

}
