package org.battleplugins.arena.parkour.event;

import org.battleplugins.arena.ArenaPlayer;
import org.battleplugins.arena.event.EventTrigger;
import org.battleplugins.arena.event.player.BukkitArenaPlayerEvent;
import org.battleplugins.arena.parkour.ParkourArena;
import org.battleplugins.arena.resolver.Resolver;
import org.battleplugins.arena.resolver.ResolverKey;
import org.battleplugins.arena.resolver.ResolverProvider;
import org.battleplugins.arena.util.Util;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

@EventTrigger("on-parkour-checkpoint")
public class ParkourCheckpointEvent extends BukkitArenaPlayerEvent {
    public static final ResolverKey<Duration> TIME_ELAPSED = ResolverKey.create("time-elapsed", Duration.class);
    public static final ResolverKey<Integer> CHECKPOINT_NUMBER = ResolverKey.create("checkpoint-number", Integer.class);

    private static final HandlerList HANDLERS = new HandlerList();

    private final int checkpointNumber;
    private final Duration elapsedTime;

    public ParkourCheckpointEvent(@NotNull ParkourArena arena, @NotNull ArenaPlayer player, int checkpointNumber) {
        super(arena, player);

        this.elapsedTime = arena.getElapsedTime(player);
        this.checkpointNumber = checkpointNumber;
    }

    /**
     * Gets the checkpoint number the player has reached.
     *
     * @return the checkpoint number the player has reached
     */
    public int getCheckpointNumber() {
        return this.checkpointNumber;
    }

    /**
     * Gets the elapsed time the player has been running.
     *
     * @return the elapsed time the player has been running
     */
    public Duration getElapsedTime() {
        return this.elapsedTime;
    }

    @Override
    public Resolver resolve() {
        return super.resolve().toBuilder()
                .define(TIME_ELAPSED, ResolverProvider.simple(this.elapsedTime, Util::toTimeString))
                .define(CHECKPOINT_NUMBER, ResolverProvider.simple(this.checkpointNumber, String::valueOf))
                .build();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
