package it.totten98.colorednametag;

import java.io.File;
import java.util.ArrayList;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

/**
 *
 * @author Totten98
 */
public class ColoredNameTag extends JavaPlugin {
    
    @Getter private static ColoredNameTag instance;

    public static final String CONFIG = "config.yml";

    private ArrayList<String> permessi = new ArrayList<>();
    private ArrayList<String> ranks = new ArrayList<>();
    private ArrayList<ChatColor> colori = new ArrayList<>();
    
    //Parte quando si accende
    @Override
    public void onEnable() {
        instance = this;
        
        createConfig();
        getConfigData();
        addOnlinePlayers();
        
        new OnPlayerEvent(this);
    }
    
    //Parte quando si spegne
    @Override
    public void onDisable() {
        clearTeams();
    }
    
    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
                saveDefaultConfig();
            }
            
            File file = new File(getDataFolder(), CONFIG);
            
            if (!file.exists()) 
            {
                saveDefaultConfig();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void getConfigData() {
        ConfigurationSection configurationSection = this.getConfig().getConfigurationSection("groups");
        if (configurationSection != null) {
            for (String rank : configurationSection.getKeys(false)) {
                ranks.add(rank);
                permessi.add(getPermission(rank));
                colori.add(getColor(rank));
            }
        }
    }
    
    public void addOnlinePlayers() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboard b = p.getScoreboard();
            for(int i = 0; i < ranks.size(); i++) {
                if(p.hasPermission(permessi.get(i))) {
                    Team team = getTeam(b, ranks.get(i), colori.get(i));
                    team.addEntry(p.getName());
                    break;
                }
            }  
        }
    }
    
    public Team getTeam(Scoreboard scoreboard, String name, ChatColor c) {
       if(scoreboard.getTeam(name) == null) {
           Team team = scoreboard.registerNewTeam(name);
           team.setColor(c);
           
           return team;
       }
       else {
           return scoreboard.getTeam(name);
       }
    }
    
    public String getPermission(String groupName) {
        return this.getConfig().getString("groups." + groupName + ".permission");
    }
    
    public ChatColor getColor(String groupName) {
        String color = this.getConfig().getString("groups." + groupName + ".color");
        return ChatColor.valueOf(color);
    }
    
    public void clearTeams() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager != null) {
            Scoreboard board = manager.getMainScoreboard();
            for (String rank : ranks) {
                if (board.getTeam(rank) != null) {
                    Team team = board.getTeam(rank);
                    if (team != null) {
                        team.unregister();
                    }
                }
            }
        }
    }
    
    public ArrayList<String> getRanksArray() {
        return ranks;
    }
    
    public ArrayList<String> getPermissionArray() {
        return permessi;
    }
    
    public ArrayList<ChatColor> getColorArray() {
        return colori;
    }
}