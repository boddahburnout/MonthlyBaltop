package darth.monthlybaltop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private MonthlyBaltop plugin = MonthlyBaltop.getPlugin(MonthlyBaltop.class);

    private boolean markedForChange = false;
    private FileConfiguration monthData;
    private File monthdataFile;

    public void setup() {
        if(!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        monthdataFile = new File(plugin.getDataFolder(), "monthdata.yml");

        if(!monthdataFile.exists()) {
            try {
                monthdataFile.createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Couldn't create monthdata.yml file");
            }
        }

        monthData = YamlConfiguration.loadConfiguration(monthdataFile);
    }
    
    public FileConfiguration getMonthData() {
        return monthData;
    }
    
    /**
     * Checks to see if there has been an update to player data resulting in the need for resorting
     * @return True if the data needs resorting
     */
    public boolean isMarkedForChange(){
        return markedForChange;
    }
    
    /**
     * Marks {@link #isMarkedForChange()} to return true
     */
    public void markForChange(){
        markedForChange = true;
    }
    
    /**
     * Marks {@link #isMarkedForChange()} to return false
     */
    public void unmarkForChange(){
        markedForChange = false;
    }
    public void saveMonthData() {
        try {
            monthData.save(monthdataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void reloadMonthData() {
        monthData = YamlConfiguration.loadConfiguration(monthdataFile);
    }
}
