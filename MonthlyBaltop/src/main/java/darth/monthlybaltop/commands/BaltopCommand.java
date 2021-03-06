package darth.monthlybaltop.commands;

import darth.monthlybaltop.MonthlyBaltop;
import darth.monthlybaltop.PlayerNameCache;
import darth.monthlybaltop.TopPlayerRanking;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

public class BaltopCommand implements Listener, CommandExecutor {

    final private MonthlyBaltop plugin;

    private static List<TopPlayerRanking> topPlayerRankings = new ArrayList<>();

    private static Map<UUID, Integer> playertoRankMap = new HashMap<>();

    public BaltopCommand(MonthlyBaltop conf) {
        plugin = conf;

        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.cfgm.reloadMonthData();
                List<TopPlayerRanking> newList = new ArrayList<>();

                for (TopPlayerRanking topPlayerRanking : topPlayerRankings) {
                    UUID uuid = topPlayerRanking.getUuid();
                    double monthBal = MonthlyBaltop.getEconomy().getBalance(Bukkit.getOfflinePlayer(uuid)) - topPlayerRanking.getStartBal();
                    TopPlayerRanking newRanking = new TopPlayerRanking(uuid, monthBal, topPlayerRanking.getStartBal());
                    newList.add(newRanking);
                }
                newList.sort(TopPlayerRanking::compareTo);

                for (int i = 0; i < newList.size(); i++) {
                    playertoRankMap.put(topPlayerRankings.get(i).getUuid(), i + 1);
                }

                topPlayerRankings = newList;
            }
        }.runTaskTimer(conf, 1 * 60 * 20, 1 * 20 * 60);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        plugin.loadConfigManager();
        LocalDate currentdate = LocalDate.now();
        String[] cmd = event.getMessage().split(" ");
        if (cmd[0].equalsIgnoreCase("/baltop")) {
            if (cmd.length > 1) {
                event.setCancelled(true);
            } else {

                event.setCancelled(true);
                event.getPlayer().sendMessage("");
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('$', plugin.getConfig().getString("header")));
                PlayerNameCache playerNameCache = plugin.getPlayerNameCache();

                //If we haven't cached top ranked players
                try {
                    if (topPlayerRankings.isEmpty() || plugin.cfgm.isMarkedForChange()) {

                        String key = currentdate.getMonth().toString() + "-" + currentdate.getYear();

                        for (String temp : plugin.cfgm.getMonthData().getConfigurationSection(key).getKeys(false)) {

                            UUID playerUUID = UUID.fromString(temp);

                            double startbal = (double) plugin.cfgm.getMonthData().get(key + "." + temp);

                            double monthbal = MonthlyBaltop.getEconomy().getBalance(Bukkit.getOfflinePlayer(playerUUID)) - startbal;

                            topPlayerRankings.add(new TopPlayerRanking(playerUUID, monthbal, startbal));
                        }
                    }

                } catch (IllegalArgumentException error) {

                }
                topPlayerRankings.sort(TopPlayerRanking::compareTo);

                plugin.cfgm.unmarkForChange();


                //Loop through top 10 or if there are less ranking than 10, then all rankings and display them
                for (int i = 0; i < Math.min(8, topPlayerRankings.size()); i++) {

                    TopPlayerRanking topPlayerRanking = topPlayerRankings.get(i);

                    String playerName = playerNameCache.getName(topPlayerRanking.getUuid());
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('$', plugin.getConfig().getString("number-color")) + (i + 1) + ") " + ChatColor.translateAlternateColorCodes('$', plugin.getConfig().getString("name-color")) + playerName + ChatColor.translateAlternateColorCodes('$', plugin.getConfig().getString("money-color")) + ", $" + String.valueOf(round(topPlayerRanking.getBalance(), 2)));
                }
                event.getPlayer().sendMessage("");
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ranking")) {
            Player p = (Player) sender;
            LocalDate currentdate = LocalDate.now();
            String key = currentdate.getMonth().toString() + "-" + currentdate.getYear();
            double monthbal = MonthlyBaltop.getEconomy().getBalance(p) - (double) plugin.cfgm.getMonthData().get(key + "." + p.getUniqueId());
            if (playertoRankMap.get(p.getUniqueId()) == null) {
                sender.sendMessage("Ranking is still calculated please try again");
                return true;
            }
            p.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('$', plugin.getConfig().getString("number-color"))+playertoRankMap.get(p.getUniqueId())+") "+ ChatColor.translateAlternateColorCodes('$', plugin.getConfig().getString("name-color")) + p.getPlayer().getName() + ChatColor.translateAlternateColorCodes('$', plugin.getConfig().getString("money-color"))+ ", "+ String.valueOf(round(monthbal, 2)));
        }
        if (command.getName().equalsIgnoreCase("btupdate")) {
            plugin.cfgm.reloadMonthData();
            List<TopPlayerRanking> newList = new ArrayList<>();

            for (TopPlayerRanking topPlayerRanking : topPlayerRankings) {
                UUID uuid = topPlayerRanking.getUuid();
                double monthBal = MonthlyBaltop.getEconomy().getBalance(Bukkit.getOfflinePlayer(uuid)) - topPlayerRanking.getStartBal();
                TopPlayerRanking newRanking = new TopPlayerRanking(uuid, monthBal, topPlayerRanking.getStartBal());
                newList.add(newRanking);
            }
            newList.sort(TopPlayerRanking::compareTo);

            for (int i = 0; i < newList.size(); i++) {
                playertoRankMap.put(topPlayerRankings.get(i).getUuid(), i + 1);
            }

            topPlayerRankings = newList;
            sender.sendMessage("Baltop updated!");
        }
        return true;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}