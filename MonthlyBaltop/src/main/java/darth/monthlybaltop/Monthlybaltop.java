package darth.monthlybaltop;

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

public final class Monthlybaltop extends JavaPlugin {

    private File monthdataFile;
    private FileConfiguration monthdata;
    private static Economy econ = null;
    private static final Logger log = Logger.getLogger("Minecraft");
    LocalDate currentdate = LocalDate.now();

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new Baltopcmd(this), this);
        Bukkit.getPluginManager().registerEvents(new Baldata(this), this);

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
        return this.monthdata;
    }

    private void createCustomConfig() {
        monthdataFile = new File(getDataFolder(), currentdate.getMonth().toString() + "-" + currentdate.getYear() + ".yml");
        if (!monthdataFile.exists()) {
            monthdataFile.getParentFile().mkdirs();
            saveResource( "monthdata.yml", false);
        }
        monthdata = new YamlConfiguration();
        try {
            monthdata.load(monthdataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
