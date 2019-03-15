package mc.arena.parkour.events;

import mc.alk.arena.objects.teams.ArenaTeam;
import mc.arena.parkour.objects.CheckPoint;
import org.bukkit.entity.Player;

public class ArrivedAtCheckPointEvent extends ParkourEvent {

    private final Player player;
    private final CheckPoint checkPoint;
    private final ArenaTeam team;

    public ArrivedAtCheckPointEvent(Player player, ArenaTeam team, CheckPoint point) {
        this.player = player;
        this.checkPoint = point;
        this.team = team;
    }

    public Player getPlayer() {
        return this.player;
    }

    public CheckPoint getCheckPoint() {
        return this.checkPoint;
    }

    public ArenaTeam getTeam() {
        return this.team;
    }
}
