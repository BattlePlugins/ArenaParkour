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

@EventTrigger("on-parkour-complete")
public class ParkourCompleteEvent extends BukkitArenaPlayerEvent {
    public static final ResolverKey<Duration> TIME_ELAPSED = ResolverKey.create("time-elapsed", Duration.class);

    private static final HandlerList HANDLERS = new HandlerList();

    private final Duration elapsedTime;

    public ParkourCompleteEvent(@NotNull ParkourArena arena, @NotNull ArenaPlayer player) {
        super(arena, player);

        this.elapsedTime = arena.getElapsedTime(player);
    }

    @Override
    public Resolver resolve() {
        return super.resolve().toBuilder()
                .define(TIME_ELAPSED, ResolverProvider.simple(this.elapsedTime, Util::toTimeString))
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
