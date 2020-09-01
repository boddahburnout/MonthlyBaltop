package darth.monthlybaltop;

import java.util.UUID;

public class TopPlayerRanking implements Comparable{
  
  private UUID uuid;
  private double balance;
  private double startBal;
  
  public TopPlayerRanking(UUID uuid, double balance, double startBal){
    this.uuid = uuid;
    this.balance = balance;
    this.startBal = startBal;
  }
  
  public UUID getUuid(){
    return uuid;
  }
  
  public double getBalance(){
    return balance;
  }
  
  public double getStartBal(){
    return startBal;
  }
  
  @Override
  public int compareTo(Object o){
    if(o instanceof TopPlayerRanking){
      TopPlayerRanking topPlayerRanking = (TopPlayerRanking) o;
      if(topPlayerRanking.getBalance() > this.getBalance()){
        return 1;
      }
      else if(topPlayerRanking.getBalance() < this.getBalance()){
        return -1;
      }
      else {
        return 0;
      }
    }
    else{
      return -1;
    }
  }
}
