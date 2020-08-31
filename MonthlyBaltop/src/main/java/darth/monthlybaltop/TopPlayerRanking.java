package darth.monthlybaltop;

import java.util.UUID;

public class TopPlayerRanking implements Comparable{
  
  private UUID uuid;
  private double balance;
  
  public TopPlayerRanking(UUID uuid, double balance){
    this.uuid = uuid;
    this.balance = balance;
  }
  
  public UUID getUuid(){
    return uuid;
  }
  
  public double getBalance(){
    return balance;
  }
  
  @Override
  public int compareTo(Object o){
    if(o instanceof TopPlayerRanking){
      TopPlayerRanking topPlayerRanking = (TopPlayerRanking) o;
      if(topPlayerRanking.getBalance() > this.getBalance()){
        return -1;
      }
      else if(topPlayerRanking.getBalance() < this.getBalance()){
        return 1;
      }
      else {
        return 0;
      }
    }
    else{
      return 1;
    }
  }
}
