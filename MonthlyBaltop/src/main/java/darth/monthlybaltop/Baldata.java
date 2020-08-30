package darth.monthlybaltop;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.time.LocalDate;

public class Baldata implements Listener {
    final private Monthlybaltop plugin;

    public Baldata(Monthlybaltop conf) {
        plugin = conf;
    }
    LocalDate currentdate = LocalDate.now();
    @EventHandler
    public void Onjoin(PlayerJoinEvent event) {
        if (!plugin.getCustomConfig().contains(currentdate.getMonth().toString()+"-"+currentdate.getYear())) {
            plugin.getCustomConfig().set(currentdate.getMonth().toString()+"-"+currentdate.getYear()+"."+event.getPlayer().getName(), Monthlybaltop.getEconomy().getBalance(event.getPlayer()));
        } else {
            if (plugin.getCustomConfig().contains(currentdate.getMonth().toString()+"-"+currentdate.getYear()+"."+event.getPlayer().getName())) {
                return;
            } else {
                plugin.getCustomConfig().set(currentdate.getMonth().toString()+"-"+currentdate.getYear()+"."+event.getPlayer().getName(), Monthlybaltop.getEconomy().getBalance(event.getPlayer()));
                return;
            }
        }
    }
}
