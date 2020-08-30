package darth.monthlybaltop;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class BaltopCommand implements Listener {
    
    final private MonthlyBaltop plugin;

    public BaltopCommand(MonthlyBaltop conf) {
        plugin = conf;
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        
        LocalDate currentdate = LocalDate.now();
        
        int loop = 0;
        String[] cmd = event.getMessage().split(" ");
        
        if (cmd[0].equals("/baltop")) {
            
            if (cmd.length > 1) {
                event.setCancelled(true);
            }
            else {
                
                event.setCancelled(true);
                event.getPlayer().sendMessage("---Monthly Baltop---");
                
                String key = currentdate.getMonth().toString() + "-" + currentdate.getYear();
                PlayerNameCache playerNameCache = plugin.getPlayerNameCache();
                
                for(String temp : plugin.getCustomConfig().getConfigurationSection(key).getKeys(false)){
                    
                    loop++;
                    
                    UUID playerUUID = UUID.fromString(temp);
                    String playerName = playerNameCache.getName(playerUUID);
                    
                    double startbal = (double) plugin.getCustomConfig().get(key + "." + temp);
                    event.getPlayer().sendMessage(loop + ") " + playerName + ", " + String.valueOf(MonthlyBaltop.getEconomy().getBalance(playerName) - startbal));
                    if (loop == 10) {
                        return;
                    }
                }
            }
        }
    }
}