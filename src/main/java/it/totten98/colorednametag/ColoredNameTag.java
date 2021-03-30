package it.totten98.colorednametag;

import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
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

    private static OnPlayerEvent onPlayerEvent = null;

    private static Chat chat = null;

    //Parte quando si accende
    @Override
    public void onEnable() {
        instance = this;

        setupChat();
        addOnlinePlayers();

        onPlayerEvent = new OnPlayerEvent(this);
    }
    
    //Parte quando si spegne
    @Override
    public void onDisable() {
        clearTeams();
    }
    
    public void addOnlinePlayers() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            addPlayerToTeam(p);
        }
    }
    
    public void clearTeams() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager != null) {
            Scoreboard board = manager.getMainScoreboard();
            for (Team rank : board.getTeams()) {
                if (board.getTeam(rank.getName()) != null) {
                    Team team = board.getTeam(rank.getName());
                    if (team != null) {
                        team.unregister();
                    }
                }
            }
        }
    }

    public void addPlayerToTeam(Player player) {

        ColorTranslator c = new ColorTranslator();

        Scoreboard b = player.getScoreboard();
        String pg = getChat().getPrimaryGroup(player);
        String pn = player.getName();
        Team team;
        // TODO: 30/03/21 Add color from perms
        if (getChat().getPlayerPrefix(player) != getChat().getGroupPrefix(player.getWorld(), pg)) {
            Team t = b.getTeam(pn);
            if (t == null) {
                t = b.registerNewTeam(pn);
                t.setPrefix(c.colorText(getChat().getPlayerPrefix(player)));
                t.setSuffix(c.colorText(getChat().getPlayerSuffix(player)));
            }
            team = t;
        } else {
            Team t = b.getTeam(pg);
            if (t == null) {
                t = b.registerNewTeam(pg);
                t.setPrefix(c.colorText(getChat().getGroupPrefix(player.getWorld(), pg)));
                t.setSuffix(c.colorText(getChat().getGroupSuffix(player.getWorld(), pg)));
            }
            team = t;
        }
        team.addEntry(player.getName());
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    public static Chat getChat() {
        return chat;
    }

}