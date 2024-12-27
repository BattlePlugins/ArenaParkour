package org.battleplugins.arena.parkour;

import io.papermc.paper.math.BlockPosition;
import org.battleplugins.arena.Arena;
import org.battleplugins.arena.ArenaPlayer;
import org.battleplugins.arena.command.ArenaCommandExecutor;
import org.battleplugins.arena.competition.map.LiveCompetitionMap;
import org.battleplugins.arena.competition.map.MapFactory;
import org.battleplugins.arena.competition.phase.CompetitionPhaseType;
import org.battleplugins.arena.event.ArenaEventHandler;
import org.battleplugins.arena.event.arena.ArenaCreateCompetitionEvent;
import org.battleplugins.arena.event.arena.ArenaRemoveCompetitionEvent;
import org.battleplugins.arena.parkour.event.ParkourCheckpointEvent;
import org.battleplugins.arena.parkour.event.ParkourCompleteEvent;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;

import java.time.Duration;

public class ParkourArena extends Arena {

    @Override
    public ArenaCommandExecutor createCommandExecutor() {
        return new ParkourExecutor(this);
    }

    @Override
    public MapFactory getMapFactory() {
        return ParkourMap.FACTORY;
    }

    @ArenaEventHandler
    public void onCompetitionCreate(ArenaCreateCompetitionEvent event) {
        if (event.getCompetition() instanceof ParkourCompetition parkourCompetition) {
            parkourCompetition.beginCompetition();
        }
    }

    @ArenaEventHandler
    public void onCompetitionRemove(ArenaRemoveCompetitionEvent event) {
        if (event.getCompetition() instanceof ParkourCompetition parkourCompetition) {
            parkourCompetition.endCompetition();
        }
    }

    @ArenaEventHandler
    public void onMove(PlayerMoveEvent event, ArenaPlayer player) {
        LiveCompetitionMap map = player.getCompetition().getMap();
        if (!(map instanceof ParkourMap parkourMap)) {
            return;
        }

        // Only handle when we are ingame
        if (!CompetitionPhaseType.INGAME.equals(player.getCompetition().getPhase())) {
            return;
        }

        BlockPosition pos = event.getTo().toBlock();
        if (!parkourMap.isCheckpoint(pos)) {
            return;
        }

        boolean initial = false;
        int checkpointNumber = parkourMap.getCheckpointNumber(pos);
        if (player.getMetadata(StartTime.class) == null) {
            if (checkpointNumber == 1) {
                player.setMetadata(StartTime.class, new StartTime(System.currentTimeMillis()));
                initial = true;
            } else {
                // Don't process anything below, since we only want to start counting their
                // time when they reach the first (start) checkpoint
                return;
            }
        }

        // Only update the last position if the player has moved to a higher checkpoint
        LastPosition lastPosition = player.getMetadata(LastPosition.class);
        if (lastPosition == null || lastPosition.getIndex() < checkpointNumber) {
            player.setMetadata(LastPosition.class, new LastPosition(pos, System.currentTimeMillis(), checkpointNumber));

            if (parkourMap.isFinalCheckpoint(pos)) {
                this.getEventManager().callEvent(new ParkourCompleteEvent(this, player));
            } else if (!initial) {
                this.getEventManager().callEvent(new ParkourCheckpointEvent(this, player, checkpointNumber - 1));

                player.removeMetadata(StartTime.class);
                player.removeMetadata(LastPosition.class);
            }
        }
    }

    public Duration getElapsedTime(ArenaPlayer player) {
        StartTime startTime = player.getMetadata(StartTime.class);
        if (startTime == null) {
            return Duration.ZERO;
        }

        return Duration.ofMillis(System.currentTimeMillis() - startTime.getTime());
    }

    public void returnToCheckpoint(ArenaPlayer player) {
        if (!(player.getCompetition() instanceof ParkourCompetition)) {
            return;
        }

        LastPosition lastPosition = player.getMetadata(LastPosition.class);
        if (lastPosition == null) {
            return;
        }

        Location loc = lastPosition.getPosition().toLocation(player.getCompetition().getMap().getWorld()).add(0.5, 0, 0.5);
        loc.setPitch(player.getPlayer().getLocation().getPitch());
        loc.setYaw(player.getPlayer().getLocation().getYaw());

        player.getPlayer().teleport(loc);
    }

    public static class StartTime {
        private final long time;

        public StartTime(long time) {
            this.time = time;
        }

        public long getTime() {
            return this.time;
        }
    }

    public static class LastPosition {
        private final BlockPosition position;
        private final long time;
        private final int index;

        public LastPosition(BlockPosition position, long time, int index) {
            this.position = position;
            this.time = time;
            this.index = index;
        }

        public BlockPosition getPosition() {
            return this.position;
        }

        public long getTime() {
            return this.time;
        }

        public int getIndex() {
            return this.index;
        }
    }
}
