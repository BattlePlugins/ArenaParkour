package org.battleplugins.arena.parkour;

import org.battleplugins.arena.config.ArenaOption;

public class ParkourConfig {

    @ArenaOption(name = "display-checkpoint-holograms", description = "Whether a hologram should appear above each checkpoint indicating the checkpoint number.", required = true)
    private boolean displayCheckpointHolograms;

    public boolean isDisplayCheckpointHolograms() {
        return this.displayCheckpointHolograms;
    }
}
