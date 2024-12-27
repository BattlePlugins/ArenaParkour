package org.battleplugins.arena.parkour;

import org.battleplugins.arena.competition.victory.VictoryCondition;
import org.battleplugins.arena.event.ArenaEventHandler;
import org.battleplugins.arena.parkour.event.ParkourCompleteEvent;

import java.util.Set;

public class ParkourCompleteCondition extends VictoryCondition<ParkourCompetition> {

    @ArenaEventHandler
    public void onParkourComplete(ParkourCompleteEvent event) {
        this.advanceToNextPhase(Set.of(event.getArenaPlayer()));
    }
}
