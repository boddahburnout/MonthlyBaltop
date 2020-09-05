package darth.monthlybaltop;

import darth.monthlybaltop.commands.BaltopCommand;
import darth.monthlybaltop.listeners.PlayerJoinListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import java.util.logging.Logger;

public final class MonthlyBaltop extends JavaPlugin {

    public ConfigManager cfgm;
    private PlayerNameCache playerNameCache;

    private static Economy econ = null;
    private static final Logger log = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new BaltopCommand(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        this.getCommand("ranking").setExecutor(new BaltopCommand(this));
        this.getCommand("btupdate").setExecutor(new BaltopCommand(this));

        playerNameCache = new PlayerNameCache(this);

        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        //config.yml
        File file = new File(getDataFolder() + File.separator + "config.yml");
        if (!file.exists()) {
            getConfig().addDefault("header", "$6---Monthly Baltop---");
            getConfig().addDefault("number-color", "$6");
            getConfig().addDefault("name-color", "$f");
            getConfig().addDefault("money-color", "$6");
            getConfig().options().copyDefaults(true);
            saveConfig();
        } else {
            saveConfig();
            reloadConfig();
        }
        loadConfigManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadConfigManager() {
        cfgm = new ConfigManager();
        cfgm.setup();
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

    public PlayerNameCache getPlayerNameCache() {
        return this.playerNameCache;
    }

}