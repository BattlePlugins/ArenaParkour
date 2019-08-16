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
            return sendMessage(sender, "&cArena " + arena.getName() + " is not a Parkour arena!");
        }
        if ((index < 1) || (index > 100)) {
            return sendMessage(sender, "&cIndex must be between [1-100]!");
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
            return sendMessage(sender, "&cArena " + arena.getName() + " is not a Parkour arena!");
        }
        ParkourArena pa = (ParkourArena)arena;
        pa.clearCheckPoints();
        BattleArena.saveArenas(Parkour.getSelf());
        return sendMessage(sender, "&2CheckPoints cleared for &6" + arena.getName());
    }

    @MCCommand(cmds={"addVictoryPoint", "avp"}, admin=true)
    public boolean addVictoryPoints(ArenaPlayer sender, Arena arena, int index) {
        if (!(arena instanceof ParkourArena)) {
            return sendMessage(sender, "&cArena " + arena.getName() + " is not a Parkour arena!");
        }
        if ((index < 1) || (index > 100)) {
            return sendMessage(sender, "&cIndex must be between [1-100]!");
        }
        ParkourArena pa = (ParkourArena)arena;
        try {
            pa.addVictoryPoint(sender.getPlayer(), index - 1,  true);
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
            return sendMessage(sender, "&cArena " + arena.getName() + " is not a Parkour arena!");
        }
        ParkourArena pa = (ParkourArena)arena;
        pa.clearVictoryPoints();
        BattleArena.saveArenas(Parkour.getSelf());
        return sendMessage(sender, "&2VictoryPoints cleared for &6" + arena.getName());
    }

    @MCCommand(cmds={"last", "lp"})
    public boolean lastCheckPoint(ArenaPlayer sender) {
        return lastCheckPoint(sender, Integer.MAX_VALUE);
    }

    @MCCommand(cmds={"last", "lp"})
    public boolean lastCheckPoint(ArenaPlayer sender, int checkPointNumber) {
        Competition comp = sender.getCompetition();
        if (!(comp instanceof Match)) {
            return sendMessage(sender, "&cYou aren't in a parkour!");
        }
        Arena arena = ((Match)comp).getArena();
        if (!(arena instanceof ParkourArena)) {
            return sendMessage(sender, "&cArena " + arena.getName() + " is not a Parkour arena!");
        }
        if ((checkPointNumber < 0) || ((checkPointNumber > 100) && (checkPointNumber != Integer.MAX_VALUE))) {
            return sendMessage(sender, "&4You need to enter a valid checkpoint!");
        }
        ParkourArena pa = (ParkourArena)arena;
        ArenaTeam team = pa.getTeam(sender);
        CheckPoint check = pa.getTeamsCheckPoint(team);
        if (check == null) {
            return sendMessage(sender, "&4You have not gotten to a checkpoint yet!");
        }
        if (checkPointNumber != Integer.MAX_VALUE) {
            if (checkPointNumber > check.getNumber() + 1) {
                return sendMessage(sender, "&4You haven't reached that checkpoint yet!");
            }
            check = pa.getCheckPoint(checkPointNumber - 1);
            if (check == null) {
                return sendMessage(sender, "&4That checkpoint doesn't exist!");
            }
        }
        TeleportController.teleport(sender.getPlayer(), check.getSpawnPoint());
        return sendMessage(sender, "&6You have been teleported to checkpoint &2" + (check.getNumber() + 1) + "&6!");
    }
}
