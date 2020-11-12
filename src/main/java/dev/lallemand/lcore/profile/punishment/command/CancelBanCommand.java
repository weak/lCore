package dev.lallemand.lcore.profile.punishment.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.punishment.procedure.PunishmentProcedure;
import dev.lallemand.lcore.profile.punishment.procedure.PunishmentProcedureStage;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"cancelban", "cancel"})
public class CancelBanCommand {

    public void execute(Player player) {
        PunishmentProcedure procedure = PunishmentProcedure.getByPlayer(player);

        if (procedure != null && procedure.getStage() == PunishmentProcedureStage.REQUIRE_TEXT) {
            PunishmentProcedure.getProcedures().remove(procedure);
            player.sendMessage(CC.RED + "You have cancelled the punishment procedure.");
        }
    }
}
