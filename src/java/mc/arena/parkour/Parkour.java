package mc.arena.parkour;

import mc.alk.arena.BattleArena;
import mc.arena.parkour.arena.ParkourArena;
import mc.arena.parkour.executors.ParkourExecutor;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Parkour extends JavaPlugin {

    private String logName = ChatColor.DARK_RED + "[Error]";
    private static Parkour plugin;

    public void onDisable()  {
        getLogger().info(this.logName + " Has disabled!");
    }

    public void onEnable() {
        plugin = this;
        this.logName = ("[" + getDescription().getName() + "]");
        BattleArena.registerCompetition(this, "Parkour", "PA", ParkourArena.class, new ParkourExecutor());

        reloadConfig();
        getLogger().info(this.logName + " Has enabled running version " + getDescription().getVersion());
    }

    public static Parkour getSelf() {
        return plugin;
    }
}
