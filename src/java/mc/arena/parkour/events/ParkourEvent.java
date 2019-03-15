package mc.arena.parkour.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ParkourEvent extends Event implements Cancellable {

    private HandlerList handlers = new HandlerList();
    private boolean bool = false;

    public boolean isCancelled() {
        return this.bool;
    }

    public void setCancelled(boolean bool) {
        this.bool = bool;
    }

    public HandlerList getHandlers() {
        return this.handlers;
    }
}
