package org.battleplugins.arena.parkour;

import org.battleplugins.arena.BattleArenaApi;
import org.battleplugins.arena.competition.victory.VictoryConditionType;
import org.battleplugins.arena.config.ArenaConfigParser;
import org.battleplugins.arena.config.ParseException;
import org.battleplugins.arena.event.ArenaEventType;
import org.battleplugins.arena.parkour.event.ParkourCheckpointEvent;
import org.battleplugins.arena.parkour.event.ParkourCompleteEvent;
import org.battleplugins.arena.util.Version;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class ArenaParkour extends JavaPlugin {
    public static final VictoryConditionType<?, ?> PARKOUR_COMPLETE_CONDITION = VictoryConditionType.create("parkour-complete", ParkourCompleteCondition.class);

    public static final ArenaEventType<ParkourCheckpointEvent> PARKOUR_CHECKPOINT_EVENT = ArenaEventType.create("on-parkour-checkpoint", ParkourCheckpointEvent.class);
    public static final ArenaEventType<ParkourCompleteEvent> PARKOUR_COMPLETE_EVENT = ArenaEventType.create("on-parkour-complete", ParkourCompleteEvent.class);

    private static ArenaParkour instance;

    private ParkourConfig config;

    @Override
    public void onEnable() {
        Version version = Version.of(Bukkit.getPluginManager().getPlugin("BattleArena"));
        if (version.isLessThan("4.0.1")) {
            this.getSLF4JLogger().error("BattleArena version 4.0.1 or higher is required to run ArenaParkour! Disabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;

        this.saveDefaultConfig();

        File configFile = new File(this.getDataFolder(), "config.yml");
        Configuration config = YamlConfiguration.loadConfiguration(configFile);
        try {
            this.config = ArenaConfigParser.newInstance(configFile.toPath(), ParkourConfig.class, config);
        } catch (ParseException e) {
            ParseException.handle(e);

            this.getSLF4JLogger().error("Failed to load Parkour configuration! Disabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        ParkourMessages.init();

        Path dataFolder = this.getDataFolder().toPath();
        Path arenasPath = dataFolder.resolve("arenas");
        if (Files.notExists(arenasPath)) {
            this.saveResource("arenas/parkour.yml", false);
        }

        BattleArenaApi.get().registerArena(this, "Parkour", ParkourArena.class, ParkourArena::new);
    }

    public ParkourConfig getMainConfig() {
        return this.config;
    }

    public static ArenaParkour getInstance() {
        return instance;
    }
}
