package org.battleplugins.arena.parkour.editor;

import org.battleplugins.arena.BattleArena;
import org.battleplugins.arena.config.ParseException;
import org.battleplugins.arena.editor.ArenaEditorWizard;
import org.battleplugins.arena.editor.ArenaEditorWizards;
import org.battleplugins.arena.editor.stage.PositionInputStage;
import org.battleplugins.arena.messages.Messages;
import org.battleplugins.arena.parkour.ParkourMap;
import org.battleplugins.arena.parkour.ParkourMessages;

import java.io.IOException;

public final class ParkourEditorWizards {
    public static final ArenaEditorWizard<CheckpointContext> ADD_CHECKPOINT = ArenaEditorWizards.createWizard(CheckpointContext::new)
            .addStage(CheckpointOption.POSITION, new PositionInputStage<>(ParkourMessages.SET_CHECKPOINT_POSITION, ctx -> ctx::setPosition))
            .onCreationComplete(ctx -> {
                ParkourMap map = ctx.getMap();
                map.addCheckpoint(ctx.getPosition());

                try {
                    map.save();
                } catch (ParseException | IOException e) {
                    BattleArena.getInstance().error("Failed to save map file for arena {}", ctx.getArena().getName(), e);
                    Messages.MAP_FAILED_TO_SAVE.send(ctx.getPlayer(), map.getName());
                    return;
                }

                ParkourMessages.CHECKPOINT_ADDED.send(ctx.getPlayer(), map.getName());
            });
}
