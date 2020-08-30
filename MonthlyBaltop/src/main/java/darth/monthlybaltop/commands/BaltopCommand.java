package darth.monthlybaltop.commands;

import darth.monthlybaltop.MonthlyBaltop;
import darth.monthlybaltop.PlayerNameCache;
import darth.monthlybaltop.TopPlayerRanking;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BaltopCommand implements Listener {
    
    final private MonthlyBaltop plugin;
    
    private static List<TopPlayerRanking> topPlayerRankings = new ArrayList<>();

    public BaltopCommand(MonthlyBaltop conf) {
        plugin = conf;
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        
        LocalDate currentdate = LocalDate.now();
        
        String[] cmd = event.getMessage().split(" ");
        
        if (cmd[0].equals("/baltop")) {
            
            if (cmd.length > 1) {
                event.setCancelled(true);
            }
            else {
                
                event.setCancelled(true);
                event.getPlayer().sendMessage("---Monthly Baltop---");
                PlayerNameCache playerNameCache = plugin.getPlayerNameCache();
    
                //If we haven't cached top ranked players
                if(topPlayerRankings.isEmpty()){
    
                    String key = currentdate.getMonth().toString() + "-" + currentdate.getYear();
    
                    for(String temp : plugin.getCustomConfig().getConfigurationSection(key).getKeys(false)){
                        
                        UUID playerUUID = UUID.fromString(temp);
        
                        double startbal = (double) plugin.getCustomConfig().get(key + "." + temp);
                        
                        topPlayerRankings.add(new TopPlayerRanking(playerUUID, startbal));
                    }
                    
                    topPlayerRankings.sort(TopPlayerRanking::compareTo);
                }
                
                //Loop through top 10 or if there are less ranking than 10, then all rankings and display them
                for(int i = 0; i < Math.min(10, topPlayerRankings.size()); i++){
                    
                    TopPlayerRanking topPlayerRanking = topPlayerRankings.get(i);
                    
                    String playerName = playerNameCache.getName(topPlayerRanking.getUuid());
                    event.getPlayer().sendMessage((i + 1) + ") " + playerName + ", " + String.valueOf(MonthlyBaltop.getEconomy().getBalance(playerName) - topPlayerRanking.getBalance()));
                }
            }
        }
    }
}