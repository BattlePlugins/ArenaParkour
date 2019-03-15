package mc.arena.parkour.events;

import mc.alk.arena.objects.teams.ArenaTeam;
import mc.arena.parkour.objects.CheckPoint;
import org.bukkit.entity.Player;

public class ArrivedAtVictoryPointEvent extends ArrivedAtCheckPointEvent {

    public ArrivedAtVictoryPointEvent(Player player, ArenaTeam team, CheckPoint point) {
        super(player, team, point);
    }
}
