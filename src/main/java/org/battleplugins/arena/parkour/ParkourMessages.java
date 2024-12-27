package org.battleplugins.arena.parkour;

import org.battleplugins.arena.messages.Message;
import org.battleplugins.arena.messages.Messages;

public final class ParkourMessages {
    public static final Message SET_CHECKPOINT_POSITION = Messages.info("parkour-editor-set-checkpoint-position", "Click a block to set the position of the checkpoint.");

    public static final Message CURRENT_TIME_MESSAGE = Messages.info("parkour-current-time", "Current time: <secondary>{}</secondary>");
    public static final Message CHECKPOINT_HOLOGRAM = Messages.info("parkour-checkpoint-hologram", "Checkpoint <secondary>#{}</secondary>");
    public static final Message CHECKPOINT_INFO = Messages.info("parkour-checkpoint-info", "Checkpoint <secondary>#{}</secondary>: <secondary>Position:</secondary> <primary>{}</primary>");
    public static final Message CHECKPOINT_INDEX_CHANGED = Messages.success("parkour-checkpoint-index-changed", "Checkpoint index changed successfully!");
    public static final Message NO_CHECKPOINTS = Messages.error("parkour-no-checkpoints", "There are no checkpoints in this map!");

    public static final Message MUST_BE_IN_PARKOUR_ARENA = Messages.error("parkour-must-be-in-parkour-arena", "You must be in a Parkour arena to use this command!");
    public static final Message START_HOLOGRAM = Messages.success("parkour-start-hologram", "Parkour Start");
    public static final Message END_HOLOGRAM = Messages.success("parkour-finish-end", "Parkour End");
    public static final Message CHECKPOINT_ADDED = Messages.success("parkour-checkpoint-added", "Checkpoint added successfully!");
    public static final Message CHECKPOINT_REMOVED = Messages.success("parkour-checkpoint-removed", "Checkpoint removed successfully!");
    public static final Message INVALID_CHECKPOINT = Messages.error("parkour-invalid-checkpoint", "Invalid checkpoint! There are only <secondary>{}</secondary> checkpoints.");

    public static void init() {
        // no-op
    }
}
