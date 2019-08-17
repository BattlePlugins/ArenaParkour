package mc.arena.parkour.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import mc.alk.arena.alib.arenaregenutil.ArenaRegenController;
import mc.alk.arena.alib.arenaregenutil.region.ArenaSelection;
import mc.alk.arena.alib.battlebukkitlib.PlayerUtil;
import mc.alk.arena.alib.bukkitadapter.SoundAdapter;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.events.ArenaEventHandler;
import mc.alk.arena.objects.events.EventPriority;
import mc.alk.arena.objects.teams.ArenaTeam;
import mc.alk.arena.serializers.Persist;
import mc.alk.arena.util.Log;
import mc.alk.arena.util.MessageUtil;
import mc.alk.arena.util.TimeUtil;
import mc.arena.parkour.Parkour;
import mc.arena.parkour.events.ArrivedAtCheckPointEvent;
import mc.arena.parkour.events.ArrivedAtVictoryPointEvent;
import mc.arena.parkour.objects.CheckPoint;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitTask;

public class ParkourArena extends Arena {

    @Persist
    protected List<CheckPoint> checkPoints = new CopyOnWriteArrayList<>();

    @Persist
    protected List<CheckPoint> victoryCheckPoints = new CopyOnWriteArrayList<>();

    private Map<ArenaTeam, CheckPoint> teamCheckPoints = new HashMap<>();
    private Map<UUID, Long> startTimes = new HashMap<>();

    private boolean victorChosen;

    private BukkitTask timerTask;

    public void onOpen() {
        try {
            localInit();
        } catch (Exception e) {
            Log.err(e.getMessage());
            e.printStackTrace();
            getMatch().cancelMatch();
            return;
        }
        teamCheckPoints.clear();
    }

    @Override
    protected void onStart() {
        timerTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Parkour.getSelf(), () -> {
            for (Player player : getAliveBukkitPlayers()) {
                String time = TimeUtil.convertMillisToString(System.currentTimeMillis() - startTimes.get(player.getUniqueId()));
                PlayerUtil.sendActionBarText(player, ChatColor.GOLD + "Parkour time: " + ChatColor.YELLOW + MessageUtil.decolorChat(MessageUtil.colorChat(time)));
            }
        }, 20, 20);
    }

    @Override
    protected void onComplete() {
        timerTask.cancel();
        startTimes.clear();
    }

    @Override
    protected void onEnter(ArenaPlayer player, ArenaTeam team) {
        startTimes.put(player.getID(), System.currentTimeMillis());
    }

    @Override
    protected void onCancel() {
        timerTask.cancel();
        startTimes.clear();
    }

    private void localInit() {
        if ((this.victoryCheckPoints == null) || (this.victoryCheckPoints.isEmpty())) {
            Log.err("[ParkourArena] " + getName() + " is missing victory points! please reset them");
        }

        victorChosen = false;
        for (Player player : getAliveBukkitPlayers()) {
            startTimes.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    @ArenaEventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (((event.getFrom().getBlockX() == event.getTo().getBlockX()) && (event.getFrom().getBlockY() == event.getTo().getBlockY()) && (event.getFrom().getBlockZ() == event.getTo().getBlockZ()))) {
            return;
        }
        for (CheckPoint checkPoint : this.checkPoints) {
            if (checkPoint.contains(event.getTo())) {
                ArenaTeam t = getTeam(event.getPlayer());
                CheckPoint oldCp = teamCheckPoints.get(t);
                if ((oldCp != null) && (oldCp.getNumber() >= checkPoint.getNumber())) {
                    break;
                }
                ArrivedAtCheckPointEvent arrivedEvent = new ArrivedAtCheckPointEvent(event.getPlayer(), t, checkPoint);

                Bukkit.getPluginManager().callEvent(arrivedEvent);
                if (arrivedEvent.isCancelled()) {
                    break;
                }
                arrivedAtCheckPoint(arrivedEvent.getPlayer(), t, arrivedEvent.getCheckPoint());

                break;
            }
        }
        for (CheckPoint victoryCheckPoint : this.victoryCheckPoints) {
            if (victoryCheckPoint.contains(event.getTo())) {
                ArenaTeam t = getTeam(event.getPlayer());
                ArrivedAtVictoryPointEvent arrivedEvent = new ArrivedAtVictoryPointEvent(event.getPlayer(), t, victoryCheckPoint);

                Bukkit.getPluginManager().callEvent(arrivedEvent);
                if (arrivedEvent.isCancelled()) {
                    return;
                }
                arrivedAtVictoryPoint(t);

                return;
            }
        }
    }

    @ArenaEventHandler(priority=EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event)  {
        ArenaTeam t = getTeam(event.getPlayer());
        if (t == null) {
            return;
        }
        CheckPoint cp = teamCheckPoints.get(t);
        if (cp == null) {
            return;
        }
        event.setRespawnLocation(cp.getSpawnPoint());
    }

    private void arrivedAtVictoryPoint(ArenaTeam team) {
        if (victorChosen)
            return;

        setWinner(team);
        victorChosen = true;
    }

    private void arrivedAtCheckPoint(Player player, ArenaTeam team, CheckPoint checkPoint) {
        int i = checkPoint.getNumber();
        if (team.size() > 1) {
            team.sendMessage("&6" + player.getName() + "&e made it to check point &6#" + (i + 1) + "&e!");
        } else {
            team.sendMessage("&eYou made it to check point &6#" + (i + 1) + "&e!");
        }

        player.playSound(player.getLocation(), SoundAdapter.getSound("LEVEL_UP"), 1f, 1f);
        teamCheckPoints.put(team, checkPoint);
    }

    public void addCheckPoint(Player p, int i, boolean bool)  {
        ArenaSelection sel = ArenaRegenController.getSelection(p);
        if (sel == null) {
            throw new IllegalStateException(ChatColor.RED + "Please select an area first using WorldEdit.");
        }
        CheckPoint cp = new CheckPoint(sel.getMinimumPoint(), sel.getMaximumPoint(), p.getLocation(), i, bool);
        if (i < this.checkPoints.size()) {
            this.checkPoints.set(i, cp);
        } else if (i == this.checkPoints.size()) {
            this.checkPoints.add(cp);
        } else {
            throw new IllegalStateException("&cYou need to set checkpoint " + (this.checkPoints.size() + 1) + " before setting this checkpoint!");
        }
    }

    public void addVictoryPoint(Player p, int i, boolean bool) {
        ArenaSelection sel = ArenaRegenController.getSelection(p);
        if (sel == null) {
            throw new IllegalStateException(ChatColor.RED + "Please select an area first using WorldEdit.");
        }
        CheckPoint cp = new CheckPoint(sel.getMinimumPoint(), sel.getMaximumPoint(), p.getLocation(), i, bool);
        if (i < this.victoryCheckPoints.size()) {
            this.victoryCheckPoints.set(i, cp);
        } else if (i == this.victoryCheckPoints.size()) {
            this.victoryCheckPoints.add(cp);
        } else {
            throw new IllegalStateException("&cYou need to set the victory checkpoint " + (this.victoryCheckPoints.size() + 1) + " before setting this victory checkpoint!");
        }
    }

    public void clearCheckPoints() {
        this.checkPoints.clear();
    }

    public void clearVictoryPoints() {
        this.checkPoints.clear();
    }

    public boolean valid() {
        return (this.victoryCheckPoints != null) && (!this.victoryCheckPoints.isEmpty());
    }

    public List<String> getInvalidReasons() {
        List<String> reasons = new ArrayList<>();
        if ((this.victoryCheckPoints == null) || (this.victoryCheckPoints.isEmpty())) {
            reasons.add("You need to specify a victory point!");
        }
        return reasons;
    }

    public CheckPoint getTeamsCheckPoint(ArenaTeam team) {
        return teamCheckPoints.get(team);
    }

    public CheckPoint getCheckPoint(int index) {
        return this.checkPoints.get(index);
    }
}
