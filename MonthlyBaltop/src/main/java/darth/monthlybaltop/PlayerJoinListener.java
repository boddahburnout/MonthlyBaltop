package darth.monthlybaltop;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.time.LocalDate;

public class PlayerJoinListener implements Listener {
    
    final private MonthlyBaltop plugin;

    public PlayerJoinListener(MonthlyBaltop conf) {
        plugin = conf;
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        
        LocalDate currentdate = LocalDate.now();
        
        String key = currentdate.getMonth().toString() + "-" + currentdate.getYear();
        String playerKey = key + "." + event.getPlayer().getUniqueId();
        
        plugin.getPlayerNameCache().updateName(event.getPlayer().getUniqueId(), event.getPlayer().getName());
    
        if (!plugin.getCustomConfig().contains(key)) {
            plugin.getCustomConfig().set(playerKey, MonthlyBaltop.getEconomy().getBalance(event.getPlayer()));
        }
        else {
            if (plugin.getCustomConfig().contains(playerKey)) {
                return;
            }
            else {
                plugin.getCustomConfig().set(playerKey, MonthlyBaltop.getEconomy().getBalance(event.getPlayer()));
                return;
            }
        }
    }
}
