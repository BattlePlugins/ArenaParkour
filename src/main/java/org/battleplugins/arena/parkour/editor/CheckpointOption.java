package org.battleplugins.arena.parkour.editor;

import org.battleplugins.arena.editor.type.EditorKey;

public enum CheckpointOption implements EditorKey {
    POSITION("position");

    private final String key;

    CheckpointOption(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
