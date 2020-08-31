package darth.monthlybaltop.commands;

import darth.monthlybaltop.ConfigManager;
import darth.monthlybaltop.MonthlyBaltop;
import darth.monthlybaltop.PlayerNameCache;
import darth.monthlybaltop.TopPlayerRanking;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

        plugin.loadConfigManager();

        
        LocalDate currentdate = LocalDate.now();
        
        String[] cmd = event.getMessage().split(" ");
        
        if (cmd[0].equalsIgnoreCase("/baltop")) {
            
            if (cmd.length > 1) {
                event.setCancelled(true);
            }
            else {
                
                event.setCancelled(true);
                event.getPlayer().sendMessage("");
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('$', plugin.getConfig().getString("header")));
                PlayerNameCache playerNameCache = plugin.getPlayerNameCache();
    
                //If we haven't cached top ranked players
                if(topPlayerRankings.isEmpty()){
    
                    String key = currentdate.getMonth().toString() + "-" + currentdate.getYear();
                    
                    for(String temp : plugin.cfgm.getMonthData().getConfigurationSection(key).getKeys(false)){
                        
                        UUID playerUUID = UUID.fromString(temp);
        
                        double startbal = (double) plugin.cfgm.getMonthData().get(key + "." + temp);
                        
                        topPlayerRankings.add(new TopPlayerRanking(playerUUID, startbal));
                    }
                    
                    topPlayerRankings.sort(TopPlayerRanking::compareTo);
                }
                
                //Loop through top 10 or if there are less ranking than 10, then all rankings and display them
                for(int i = 0; i < Math.min(10, topPlayerRankings.size()); i++){
                    
                    TopPlayerRanking topPlayerRanking = topPlayerRankings.get(i);
                    
                    String playerName = playerNameCache.getName(topPlayerRanking.getUuid());
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes ('$', plugin.getConfig().getString("number-color")) + (i + 1) + ") " + ChatColor.translateAlternateColorCodes ('$', plugin.getConfig().getString("name-color")) + playerName + ChatColor.translateAlternateColorCodes ('$', plugin.getConfig().getString("money-color")) + ", $" + String.valueOf(round(MonthlyBaltop.getEconomy().getBalance(playerName) - topPlayerRanking.getBalance(), 2)));
                }
                event.getPlayer().sendMessage("");
            }
        }
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}