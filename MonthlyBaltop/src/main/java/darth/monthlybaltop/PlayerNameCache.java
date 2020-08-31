package darth.monthlybaltop;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerNameCache{

  private Map<UUID, String> uuidToNameMap;

  public PlayerNameCache(MonthlyBaltop plugin){
    uuidToNameMap = new HashMap<>();
  }

  /**
   * Updates the stored name for a player
   * @param uuid The {@link UUID} to store
   * @param name The {@link String} player name to be mapped to the uuid
   */
  public void updateName(UUID uuid, String name){
    uuidToNameMap.put(uuid, name);
  }

  /**
   * Gets the player name associated with the {@link UUID}.
   * If there is no name stored, then it tries to get it.
   *
   * If the {@link UUID} provided links to a player who hasn't
   * logged in before, null is returned
   * @param uuid The {@link UUID} of the player to get the name for
   */
  public String getName(UUID uuid){
    if(!uuidToNameMap.containsKey(uuid)){
      storeNameFromUUID(uuid);
    }

    return uuidToNameMap.get(uuid);
  }

  /**
   * Attempts to store the name from the {@link UUID} provided
   * @param uuid The {@link UUID} to get the player name from
   */
  private void storeNameFromUUID(UUID uuid){
    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

    //If a player is not online and they haven't player before then we store null instead
    if(!offlinePlayer.isOnline() && !offlinePlayer.hasPlayedBefore()){
      uuidToNameMap.put(uuid, "null");
    }
    else{
      uuidToNameMap.put(uuid, offlinePlayer.getName());
    }
  }
}