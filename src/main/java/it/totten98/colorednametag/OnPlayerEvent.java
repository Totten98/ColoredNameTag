package it.totten98.colorednametag;

import java.util.ArrayList;
import lombok.Getter;
import org.bukkit.ChatColor;
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
    
    private ArrayList<String> ranks;
    private ArrayList<String> permessi;
    private ArrayList<ChatColor> colori;
    
    public OnPlayerEvent(ColoredNameTag c) {
       //
        this.permessi = c.getPermissionArray();
        this.ranks = c.getRanksArray();
        this.colori = c.getColorArray();
       //
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
        
        instance = this;
        
        Scoreboard board = p.getScoreboard();
        
        for(int i = 0; i < ranks.size(); i++) {
            if(p.hasPermission(permessi.get(i))) {
                Team team = c.getTeam(board, ranks.get(i), colori.get(i));
                team.addEntry(p.getName());
                
                break;
            }
        }
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
            if (team != null) {
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
}
