package mc.arena.parkour.executors;

import mc.alk.arena.BattleArena;
import mc.alk.arena.competition.Competition;
import mc.alk.arena.competition.match.Match;
import mc.alk.arena.controllers.TeleportController;
import mc.alk.arena.executors.CustomCommandExecutor;
import mc.alk.arena.executors.MCCommand;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.teams.ArenaTeam;
import mc.alk.arena.util.MessageUtil;
import mc.arena.parkour.Parkour;
import mc.arena.parkour.arena.ParkourArena;
import mc.arena.parkour.objects.CheckPoint;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ParkourExecutor extends CustomCommandExecutor {

    @MCCommand(cmds={"addCheckPoint", "acp"}, admin=true)
    public boolean addCheckPoint(Player sender, Arena arena, int index) {
        if (!(arena instanceof ParkourArena)) {
            return sendMessage(sender, "&eArena " + arena.getName() + " is not a Parkour arena!");
        }
        if ((index < 1) || (index > 100)) {
            return sendMessage(sender, "&2index must be between [1-100]!");
        }
        ParkourArena pa = (ParkourArena)arena;
        try {
            pa.addCheckPoint(sender.getPlayer(), index - 1, false);
        } catch (Exception e) {
            MessageUtil.sendMessage(sender, e.getMessage());
            return true;
        }
        BattleArena.saveArenas(Parkour.getSelf());
        return sendMessage(sender, "&2Checkpoint &6" + index + "&2 added!");
    }

    @MCCommand(cmds={"clearCheckPoints", "ccp"}, admin=true)
    public boolean clearCheckPoints(CommandSender sender, Arena arena) {
        if (!(arena instanceof ParkourArena)) {
            return sendMessage(sender, "&eArena " + arena.getName() + " is not a Parkour arena!");
        }
        ParkourArena pa = (ParkourArena)arena;
        pa.clearCheckPoints();
        BattleArena.saveArenas(Parkour.getSelf());
        return sendMessage(sender, "&2CheckPoints cleared for &6" + arena.getName());
    }

    @MCCommand(cmds={"addVictoryPoint", "avp"}, admin=true)
    public boolean addVictoryPoints(ArenaPlayer sender, Arena arena, Integer index) {
        if (!(arena instanceof ParkourArena)) {
            return sendMessage(sender, "&eArena " + arena.getName() + " is not a Parkour arena!");
        }
        if ((index.intValue() < 1) || (index.intValue() > 100)) {
            return sendMessage(sender, "&2index must be between [1-100]!");
        }
        ParkourArena pa = (ParkourArena)arena;
        try {
            pa.addVictoryPoint(sender.getPlayer(), index.intValue() - 1,  true);
        } catch (Exception e) {
            MessageUtil.sendMessage(sender, e.getMessage());
            return true;
        }
        BattleArena.saveArenas(Parkour.getSelf());
        return sendMessage(sender, "&2Victory &6" + index + "&2 added!");
    }

    @MCCommand(cmds={"clearVictoryPoints", "cvp"}, admin=true)
    public boolean clearVictoryPoints(CommandSender sender, Arena arena) {
        if (!(arena instanceof ParkourArena)) {
            return sendMessage(sender, "&eArena " + arena.getName() + " is not a Parkour arena!");
        }
        ParkourArena pa = (ParkourArena)arena;
        pa.clearVictoryPoints();
        BattleArena.saveArenas(Parkour.getSelf());
        return sendMessage(sender, "&2VictoryPoints cleared for &6" + arena.getName());
    }

    @MCCommand(cmds={"last", "lp"})
    public boolean lastCheckPoint(ArenaPlayer sender) {
        return lastCheckPoint(sender, Integer.valueOf(Integer.MAX_VALUE));
    }

    @MCCommand(cmds={"last", "lp"})
    public boolean lastCheckPoint(ArenaPlayer sender, Integer checkPointNumber) {
        Competition comp = sender.getCompetition();
        if ((comp == null) || (!(comp instanceof Match))) {
            return sendMessage(sender, "&cYou aren't in a parkour!");
        }
        Arena arena = ((Match)comp).getArena();
        if (!(arena instanceof ParkourArena)) {
            return sendMessage(sender, "&eArena " + arena.getName() + " is not a Parkour arena!");
        }
        if ((checkPointNumber.intValue() < 0) || ((checkPointNumber.intValue() > 100) && (checkPointNumber.intValue() != Integer.MAX_VALUE))) {
            return sendMessage(sender, "&4You need to enter a valid checkPoint!");
        }
        ParkourArena pa = (ParkourArena)arena;
        ArenaTeam team = pa.getTeam(sender);
        CheckPoint check = pa.getTeamsCheckPoint(team);
        if (check == null) {
            return sendMessage(sender, "&4You have not gotten a checkpoint yet!");
        }
        if (checkPointNumber.intValue() != Integer.MAX_VALUE) {
            if (checkPointNumber.intValue() > check.getNumber() + 1) {
                return sendMessage(sender, "&4You havent reached that checkpoint yet!");
            }
            check = pa.getCheckPoint(Integer.valueOf(checkPointNumber.intValue() - 1));
            if (check == null) {
                return sendMessage(sender, "&4That checkpoint doesnt exist!");
            }
        }
        TeleportController.teleport(sender.getPlayer(), check.getSpawnPoint());
        return sendMessage(sender, "&6You have been teleported to checkpoint &2" + (check.getNumber() + 1) + "&6!");
    }
}
