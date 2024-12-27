package org.battleplugins.arena.parkour;

import io.papermc.paper.math.Position;
import net.kyori.adventure.text.Component;
import org.battleplugins.arena.ArenaPlayer;
import org.battleplugins.arena.competition.CompetitionType;
import org.battleplugins.arena.competition.LiveCompetition;
import org.battleplugins.arena.feature.hologram.Hologram;
import org.battleplugins.arena.feature.hologram.Holograms;
import org.battleplugins.arena.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ParkourCompetition extends LiveCompetition<ParkourCompetition> {
    private final List<Hologram> holograms = new ArrayList<>();

    private final boolean displayCheckpointHolograms;

    private BukkitTask currentTimeTask;

    public ParkourCompetition(ParkourArena arena, CompetitionType type, ParkourMap map) {
        super(arena, type, map);

        this.displayCheckpointHolograms = ArenaParkour.getInstance().getMainConfig().isDisplayCheckpointHolograms();

        String message = ParkourMessages.CURRENT_TIME_MESSAGE.asPlainText();
        if (!message.isBlank()) {
            this.currentTimeTask = Bukkit.getScheduler().runTaskTimer(ArenaParkour.getInstance(), () -> {
                for (ArenaPlayer player : this.getPlayers()) {
                    Duration elapsedTime = arena.getElapsedTime(player);
                    if (elapsedTime.isNegative() || elapsedTime.isZero()) {
                        continue;
                    }

                    player.getPlayer().sendActionBar(ParkourMessages.CURRENT_TIME_MESSAGE.toComponent(Util.toTimeStringShort(elapsedTime)));
                }
            }, 0, 20);
        }
    }

    void beginCompetition() {
        if (!this.displayCheckpointHolograms) {
            return;
        }

        ParkourMap map = (ParkourMap) this.getMap();
        for (Position checkpoint : map.getCheckpoints()) {
            int checkpointNumber = map.getCheckpointNumber(checkpoint);
            Component text;
            if (checkpointNumber == 1) {
                text = ParkourMessages.START_HOLOGRAM.toComponent();
            } else if (map.isFinalCheckpoint(checkpoint)) {
                text = ParkourMessages.END_HOLOGRAM.toComponent();
            } else {
                text = ParkourMessages.CHECKPOINT_HOLOGRAM.toComponent(Integer.toString(checkpointNumber - 1)); // Subtract 1 since the first checkpoint is the start
            }

            Hologram hologram = Holograms.createHologram(this, checkpoint.offset(0.5, 1, 0.5).toLocation(map.getWorld()), text);
            this.holograms.add(hologram);
        }
    }

    void endCompetition() {
        if (!this.displayCheckpointHolograms) {
            return;
        }

        for (Hologram hologram : this.holograms) {
            Holograms.removeHologram(hologram);
        }

        this.holograms.clear();

        if (this.currentTimeTask != null) {
            this.currentTimeTask.cancel();
            this.currentTimeTask = null;
        }
    }
}
