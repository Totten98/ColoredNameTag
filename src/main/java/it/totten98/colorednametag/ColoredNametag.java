package it.totten98.colorednametag;

import java.io.File;
import java.util.ArrayList;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
    

/**
 *
 * @author Totten98
 */
public class ColoredNametag extends JavaPlugin {
    
    @Getter private static ColoredNametag instance;
    
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
            
            File file = new File(getDataFolder(), "config.yml");  
            
            if (!file.exists()) 
            {
                saveDefaultConfig();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    
    public void getConfigData()
    {
        for (String rank : this.getConfig().getConfigurationSection("groups").getKeys(false)){
            ranks.add(rank);
            permessi.add(getPermission(rank));
            colori.add(getColor(rank));
        }
    }
    
    public void addOnlinePlayers() {
        
        for (Player p : Bukkit.getOnlinePlayers()) {
            
            Scoreboard b = p.getScoreboard();
        
            for(int i = 0; i < ranks.size(); i++)
            {
                if(p.hasPermission(permessi.get(i)))
                {
                    Team team = getTeam(b, ranks.get(i), colori.get(i));
                    team.addEntry(p.getName());

                    break;
                }
            }  
        }
    }
    
    public Team getTeam(Scoreboard scoreboard, String name, ChatColor c) {
       if(scoreboard.getTeam(name) == null)
       {
           Team team = scoreboard.registerNewTeam(name);
           team.setColor(c);
           
           return team;
       }
       else
       {           
           return scoreboard.getTeam(name);
       }
    }
    
    public String getPermission(String groupName){
        return this.getConfig().getString("groups." + groupName + ".permission");
    }
    
    public ChatColor getColor(String groupName){
        String color = this.getConfig().getString("groups." + groupName + ".color");
        
        ChatColor c = ChatColor.valueOf(color);
        
        return c;
    }
    
    public void clearTeams()
    {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        for (int i = 0; i < ranks.size(); i++) 
        {
            if(board.getTeam(ranks.get(i)) != null)
            {
                Team team = board.getTeam(ranks.get(i));
                team.unregister();
            }
        }
    }
    
    public ArrayList<String> getRanksArray()
    {
        return ranks;
    }
    
    public ArrayList<String> getPermissionArray()
    {
        return permessi;
    }
    
    public ArrayList<ChatColor> getColorArray()
    {
        return colori;
    }
}