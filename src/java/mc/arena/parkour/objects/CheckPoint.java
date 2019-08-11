package mc.arena.parkour.objects;

import java.util.HashMap;
import java.util.Map;

import mc.alk.arena.alib.arenaregenutil.region.ArenaRegion;
import mc.alk.arena.objects.regions.BoundingBox;
import mc.alk.arena.util.SerializerUtil;
import org.bukkit.Location;

public class CheckPoint extends BoundingBox implements ArenaRegion {

    Location spawnPoint;
    boolean victoryPoint;
    int number;

    public CheckPoint() {}

    public CheckPoint(Location l1, Location l2, Location spawnPoint, int number, boolean victoryPoint) {
        super(l1, l2);
        this.number = number;
        this.spawnPoint = spawnPoint;
        this.victoryPoint = victoryPoint;
    }

    public Location getSpawnPoint() {
        return this.spawnPoint;
    }

    public boolean isVictoryPoint() {
        return this.victoryPoint;
    }

    public void setVictoryPoint(boolean bool) {
        this.victoryPoint = bool;
    }

    public void setSpawnPoint(Location loc) {
        this.spawnPoint = loc;
    }

    public Object yamlToObject(Map<String, Object> map, String value) {
        Location tc = SerializerUtil.getLocation((String)map.get("topCorner"));
        Location bc = SerializerUtil.getLocation((String)map.get("bottomCorner"));

        Location sp = SerializerUtil.getLocation((String)map.get("spawnPoint"));

        boolean bool = Boolean.valueOf(map.get("isVictoryPoint").toString()).booleanValue();
        int number = Integer.valueOf(map.get("number").toString()).intValue();
        return new CheckPoint(tc, bc, sp, number, bool);
    }

    public Object objectToYaml() {
        Map<String, String> map = new HashMap();
        map.put("topCorner", SerializerUtil.getLocString(this.upper));
        map.put("bottomCorner", SerializerUtil.getLocString(this.lower));
        map.put("spawnPoint", SerializerUtil.getLocString(this.spawnPoint));
        map.put("isVictoryPoint", String.valueOf(isVictoryPoint()));
        map.put("number", String.valueOf(this.number));
        return map;
    }

    @Override
    public boolean isValid() {
        return (this.upper != null) && (this.lower != null) && (this.spawnPoint != null);
    }

    public String getID() {
        return super.hashCode() + "";
    }

    public String getWorldName() {
        return null;
    }

    public int getNumber() {
        return this.number;
    }

    public String toString() {
        return "[CheckPoint " + this.number + "  v=" + this.victoryPoint + " " + super.toString() + "]";
    }
}
