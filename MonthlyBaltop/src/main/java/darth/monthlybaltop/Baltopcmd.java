package darth.monthlybaltop;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.time.LocalDate;
import java.util.Set;

public class Baltopcmd implements Listener {
    final private Monthlybaltop plugin;

    public Baltopcmd(Monthlybaltop conf) {
        plugin = conf;
    }

    LocalDate currentdate = LocalDate.now();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        int loop = 0;
        String[] cmd = event.getMessage().split(" ");
        if (cmd[0].equals("/baltop")) {
            if (cmd.length > 1) {
                event.setCancelled(true);
            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage("---Monthly Baltop---");
                Set<String> players = plugin.getCustomConfig().getConfigurationSection(currentdate.getMonth().toString() + "-" + currentdate.getYear()).getKeys(true);
                for (String temp : players) {
                    loop = loop + 1;
                    double startbal = (double) plugin.getCustomConfig().get(currentdate.getMonth().toString() + "-" + currentdate.getYear() + "." + temp);
                    event.getPlayer().sendMessage(loop+") "+temp+", "+String.valueOf(Monthlybaltop.getEconomy().getBalance(temp) - startbal));
                    if (loop == 10) {
                        return;
                    }
                }
            }
        }
    }
}