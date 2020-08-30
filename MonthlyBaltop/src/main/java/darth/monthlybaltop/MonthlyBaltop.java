package darth.monthlybaltop;

import darth.monthlybaltop.commands.BaltopCommand;
import darth.monthlybaltop.listeners.PlayerJoinListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import java.util.logging.Logger;

public final class MonthlyBaltop extends JavaPlugin {

    private File monthDataFile;
    private FileConfiguration monthData;
    private PlayerNameCache playerNameCache;
    
    private static Economy econ = null;
    private static final Logger log = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new BaltopCommand(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        
        playerNameCache = new PlayerNameCache(this);

        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        //config.yml
        File file = new File(getDataFolder() + File.separator + "config.yml");
        if (!file.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        } else {
            saveConfig();
            reloadConfig();
        }
        File dataFolder = new File(getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        createCustomConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public FileConfiguration getCustomConfig() {
        return this.monthData;
    }
    
    public PlayerNameCache getPlayerNameCache(){
        return this.playerNameCache;
    }

    private void createCustomConfig() {
        LocalDate currentdate = LocalDate.now();
    
        monthDataFile = new File(getDataFolder(), currentdate.getMonth().toString() + "-" + currentdate.getYear() + ".yml");
        if (!monthDataFile.exists()) {
            monthDataFile.getParentFile().mkdirs();
            saveResource( "monthdata.yml", false);
        }
        monthData = new YamlConfiguration();
        try {
            monthData.load(monthDataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
