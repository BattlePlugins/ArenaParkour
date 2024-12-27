package org.battleplugins.arena.parkour;

import io.papermc.paper.math.Position;
import org.battleplugins.arena.Arena;
import org.battleplugins.arena.competition.LiveCompetition;
import org.battleplugins.arena.competition.map.LiveCompetitionMap;
import org.battleplugins.arena.competition.map.MapFactory;
import org.battleplugins.arena.competition.map.MapType;
import org.battleplugins.arena.competition.map.options.Bounds;
import org.battleplugins.arena.competition.map.options.Spawns;
import org.battleplugins.arena.config.ArenaOption;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParkourMap extends LiveCompetitionMap {
    static final MapFactory FACTORY = MapFactory.create(ParkourMap.class, ParkourMap::new);

    @ArenaOption(name = "checkpoints", description = "The checkpoints in the parkour map.")
    private List<Checkpoint> checkpoints;

    public ParkourMap() {
    }

    public ParkourMap(String name, Arena arena, MapType type, String world, @Nullable Bounds bounds, @Nullable Spawns spawns) {
        super(name, arena, type, world, bounds, spawns);
    }

    public List<Position> getCheckpoints() {
        return this.checkpoints == null ? List.of() : this.checkpoints.stream().map(Checkpoint::toPosition).toList();
    }

    public void addCheckpoint(Position checkpoint) {
        if (this.checkpoints == null) {
            this.checkpoints = new ArrayList<>();
        }

        this.checkpoints.add(new Checkpoint(checkpoint.blockX(), checkpoint.blockY(), checkpoint.blockZ()));
    }

    public void addCheckpoint(int index, Position checkpoint) {
        if (this.checkpoints == null) {
            this.checkpoints = new ArrayList<>();
        }

        this.checkpoints.add(index, new Checkpoint(checkpoint.blockX(), checkpoint.blockY(), checkpoint.blockZ()));
    }

    public void removeCheckpoint(Position checkpoint) {
        this.checkpoints.remove(new Checkpoint(checkpoint.blockX(), checkpoint.blockY(), checkpoint.blockZ()));
    }

    public boolean isFinalCheckpoint(Position checkpoint) {
        if (this.checkpoints == null || this.checkpoints.isEmpty()) {
            return false;
        }

        return this.checkpoints.get(this.checkpoints.size() - 1).toPosition().equals(checkpoint);
    }

    public boolean isCheckpoint(Position checkpoint) {
        if (this.checkpoints == null) {
            return false;
        }

        return this.checkpoints.contains(new Checkpoint(checkpoint.blockX(), checkpoint.blockY(), checkpoint.blockZ()));
    }

    public int getCheckpointNumber(Position position) {
        if (!this.isCheckpoint(position)) {
            return -1;
        }

        return this.checkpoints.indexOf(new Checkpoint(position.blockX(), position.blockY(), position.blockZ())) + 1;
    }

    @Override
    public LiveCompetition<ParkourCompetition> createCompetition(Arena arena) {
        if (!(arena instanceof ParkourArena parkourArena)) {
            throw new IllegalArgumentException("Arena must be a Parkour arena!");
        }

        return new ParkourCompetition(parkourArena, arena.getType(), this);
    }

    public static class Checkpoint {
        @ArenaOption(name = "x", description = "The x coordinate of the checkpoint.", required = true)
        private int x;

        @ArenaOption(name = "y", description = "The y coordinate of the checkpoint.", required = true)
        private int y;

        @ArenaOption(name = "z", description = "The z coordinate of the checkpoint.", required = true)
        private int z;

        public Checkpoint() {
        }

        public Checkpoint(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Position toPosition() {
            return Position.block(this.x, this.y, this.z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Checkpoint that = (Checkpoint) o;
            return this.x == that.x && this.y == that.y && this.z == that.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y, this.z);
        }
    }
}
