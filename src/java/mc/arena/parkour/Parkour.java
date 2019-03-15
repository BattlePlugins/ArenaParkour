package mc.arena.parkour;

import java.util.logging.Logger;
import mc.alk.arena.BattleArena;
import mc.arena.parkour.arena.ParkourArena;
import mc.arena.parkour.executors.ParkourExecutor;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Parkour extends JavaPlugin {

    Logger log = Logger.getLogger("Minecraft");
    private String logName = ChatColor.DARK_RED + "[Error]";
    static Parkour plugin;

    public void onDisable()  {
        this.log.info(this.logName + " Has disabled!");
    }

    public void onEnable() {
        plugin = this;
        this.logName = ("[" + getDescription().getName() + "]");
        BattleArena.registerCompetition(this, "Parkour", "PA", ParkourArena.class, new ParkourExecutor());

        reloadConfig();
        this.log.info(this.logName + " Has enabled running version " + getDescription().getVersion());
    }

    public static Parkour getSelf() {
        return plugin;
    }

    public void reloadConfig() {
        super.reloadConfig();
        loadConfig();
    }

    public void loadConfig() {}
}
