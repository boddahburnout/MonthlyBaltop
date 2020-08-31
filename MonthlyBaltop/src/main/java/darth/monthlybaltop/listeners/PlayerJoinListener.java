package darth.monthlybaltop.listeners;

import darth.monthlybaltop.MonthlyBaltop;
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

        if (!plugin.cfgm.getMonthData().contains(key)) {
            plugin.cfgm.getMonthData().set(playerKey, MonthlyBaltop.getEconomy().getBalance(event.getPlayer()));
            plugin.cfgm.saveMonthData();
            plugin.cfgm.reloadMonthData();
            plugin.cfgm.markForChange();
        }
        else {
            if (plugin.cfgm.getMonthData().contains(playerKey)) {
                return;
            }
            else {
                plugin.cfgm.getMonthData().set(playerKey, MonthlyBaltop.getEconomy().getBalance(event.getPlayer()));
                plugin.cfgm.saveMonthData();
                plugin.cfgm.reloadMonthData();
                plugin.cfgm.markForChange();
                return;
            }
        }
    }
}
