package org.battleplugins.arena.parkour;

import io.papermc.paper.math.Position;
import org.battleplugins.arena.ArenaPlayer;
import org.battleplugins.arena.command.ArenaCommand;
import org.battleplugins.arena.command.ArenaCommandExecutor;
import org.battleplugins.arena.command.Argument;
import org.battleplugins.arena.competition.map.CompetitionMap;
import org.battleplugins.arena.messages.Messages;
import org.battleplugins.arena.parkour.editor.ParkourEditorWizards;
import org.bukkit.entity.Player;

public class ParkourExecutor extends ArenaCommandExecutor {

    public ParkourExecutor(ParkourArena arena) {
        super(arena);
    }

    @ArenaCommand(commands = "checkpoint", description = "Return to a previous checkpoint.", permissionNode = "checkpoint")
    public void checkpoint(Player player) {
        ArenaPlayer arenaPlayer = ArenaPlayer.getArenaPlayer(player);
        if (arenaPlayer == null) {
            Messages.NOT_IN_ARENA.send(player);
            return;
        }

        if (!(arenaPlayer.getCompetition() instanceof ParkourCompetition competition)) {
            ParkourMessages.MUST_BE_IN_PARKOUR_ARENA.send(player);
            return;
        }

        ((ParkourArena) competition.getArena()).returnToCheckpoint(arenaPlayer);
    }

    @ArenaCommand(commands = "checkpoint", subCommands = "add", description = "Add a checkpoint to a parkour arena.", permissionNode = "checkpoint.add")
    public void addCheckpoint(Player player, CompetitionMap map) {
        if (!(map instanceof ParkourMap parkourMap)) {
            return; // Should not happen but just incase
        }

        ParkourEditorWizards.ADD_CHECKPOINT.openWizard(player, this.arena, ctx -> ctx.setMap(parkourMap));
    }

    @ArenaCommand(commands = "checkpoint", subCommands = "remove", description = "Remove a checkpoint from a parkour arena.", permissionNode = "checkpoint.remove")
    public void removeCheckpoint(Player player, CompetitionMap map, int index) {
        if (!(map instanceof ParkourMap parkourMap)) {
            return; // Should not happen but just incase
        }

        index--;
        if (index < 0 || index >= parkourMap.getCheckpoints().size()) {
            ParkourMessages.INVALID_CHECKPOINT.send(player, Integer.toString(parkourMap.getCheckpoints().size()));
            return;
        }

        parkourMap.removeCheckpoint(parkourMap.getCheckpoints().get(index));
        ParkourMessages.CHECKPOINT_REMOVED.send(player, Integer.toString(index + 1));
    }

    @ArenaCommand(commands = "checkpoint", subCommands = "index", description = "Changes the index of a checkpoint.", permissionNode = "checkpoint.index")
    public void indexCheckpoint(Player player, CompetitionMap map, @Argument(name = "from") int from, @Argument(name = "to") int to) {
        if (!(map instanceof ParkourMap parkourMap)) {
            return; // Should not happen but just incase
        }

        from--;
        to--;

        if (parkourMap.getCheckpoints().isEmpty()) {
            ParkourMessages.NO_CHECKPOINTS.send(player);
            return;
        }

        if (from < 0 || from >= parkourMap.getCheckpoints().size() || to < 0 || to >= parkourMap.getCheckpoints().size()) {
            ParkourMessages.INVALID_CHECKPOINT.send(player, Integer.toString(parkourMap.getCheckpoints().size()));
            return;
        }

        Position checkpoint = parkourMap.getCheckpoints().get(from);
        parkourMap.removeCheckpoint(checkpoint);
        parkourMap.addCheckpoint(to, checkpoint);

        ParkourMessages.CHECKPOINT_INDEX_CHANGED.send(player);
    }

    @ArenaCommand(commands = "checkpoint", subCommands = "list", description = "List all checkpoints in a parkour arena.", permissionNode = "checkpoint.list")
    public void listCheckpoints(Player player, CompetitionMap map) {
        if (!(map instanceof ParkourMap parkourMap)) {
            return; // Should not happen but just incase
        }

        if (parkourMap.getCheckpoints().isEmpty()) {
            ParkourMessages.NO_CHECKPOINTS.send(player);
            return;
        }

        this.sendHeader(player);
        for (int i = 0; i < parkourMap.getCheckpoints().size(); i++) {
            Position pos = parkourMap.getCheckpoints().get(i);
            String position = pos.blockX() + ", " + pos.blockY() + ", " + pos.blockZ();

            ParkourMessages.CHECKPOINT_INFO.send(player, Integer.toString(i + 1), position);
        }
    }
}
