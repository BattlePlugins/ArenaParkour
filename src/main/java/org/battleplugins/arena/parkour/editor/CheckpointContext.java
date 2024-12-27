package org.battleplugins.arena.parkour.editor;

import io.papermc.paper.math.Position;
import org.battleplugins.arena.Arena;
import org.battleplugins.arena.editor.ArenaEditorWizard;
import org.battleplugins.arena.editor.EditorContext;
import org.battleplugins.arena.parkour.ParkourMap;
import org.bukkit.entity.Player;

public class CheckpointContext extends EditorContext<CheckpointContext> {
    private ParkourMap map;
    private Position position;

    public CheckpointContext(ArenaEditorWizard<CheckpointContext> wizard, Arena arena, Player player) {
        super(wizard, arena, player);
    }

    public ParkourMap getMap() {
        return this.map;
    }

    public void setMap(ParkourMap map) {
        this.map = map;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public boolean isComplete() {
        return this.map != null && this.position != null;
    }
}
