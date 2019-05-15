/*
 * This plugins is usable end editable only by 
 * NaossCommunity & Totten98
 */
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
public class OnPlayerEvent implements Listener
{
    ColoredNametag c = ColoredNametag.getInstance();
    
    @Getter private static OnPlayerEvent instance;
    
    private ArrayList<String> ranks;
    private ArrayList<String> permessi;
    private ArrayList<ChatColor> colori;
    
    public OnPlayerEvent(ColoredNametag c)
    {
       //
        this.permessi = c.getPermissionArray();
        this.ranks = c.getRanksArray();
        this.colori = c.getColorArray();
       //
        this.c = c;
        c.getServer().getPluginManager().registerEvents(this, c);
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        
        instance = this;
        
        Scoreboard board = p.getScoreboard();
        
        for(int i = 0; i < ranks.size(); i++)
        {
            if(p.hasPermission(permessi.get(i)))
            {
                Team team = c.getTeam(board, ranks.get(i), colori.get(i));
                team.addEntry(p.getName());
                
                break;
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Player p = event.getPlayer();
        
        Scoreboard board = p.getScoreboard();
        
        for(int i = 0; i < ranks.size(); i++)
        {
            Team team = board.getTeam(ranks.get(i));
            if (team != null) {
                if (team.hasEntry(p.getName()))
                {
                    team.removeEntry(p.getName());
                    if("[]".equals(team.getEntries().toString()))
                        team.unregister();
                    break;
                }
            }
        }
    }
}
