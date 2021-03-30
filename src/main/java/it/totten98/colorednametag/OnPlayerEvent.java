package it.totten98.colorednametag;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 *
 * @author Totten98
 */
public class OnPlayerEvent implements Listener {

    @Getter private static ColoredNameTag c;
    @Getter private static OnPlayerEvent instance;
    
    public OnPlayerEvent(ColoredNameTag c) {
        OnPlayerEvent.c = c;
        c.getServer().getPluginManager().registerEvents(this, c);
    }

    /**
     * Called when a player joins the server
     * @param event the join event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        c.addPlayerToTeam(p);
    }

    /**
     * Called when a player quits from the server
     * @param event the quit event
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        Scoreboard board = p.getScoreboard();

        for (Team team : board.getTeams()) {
            if (team.hasEntry(p.getName())) {
                team.removeEntry(p.getName());
                if(team.getEntries().toString().equals("[]")) {
                    team.unregister();
                }
                break;
            }
        }
    }
}
