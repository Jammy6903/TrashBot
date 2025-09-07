package com.jami.Database.Guild.fun;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Counting {

  @BsonProperty("currentNumber")
  private long currentNumber = 0;

  @BsonProperty("currentNumberUser")
  private Long currentNumberUser;

  @BsonProperty("highestNumber")
  private long highestNumber = 0;

  @BsonExtraElements
  private Document legacyValues;

  public Counting() {
  }

  // currentNumber

  public void setCurrentNumber(long number) {
    this.currentNumber = number;
  }

  public long getCurrentNumber() {
    return currentNumber;
  }

  // currentNumberUser

  public void setCurrentNumberUser(Long userId) {
    this.currentNumberUser = userId;
  }

  public Long getCurrentNumberUser() {
    return currentNumberUser;
  }

  // highestNumber

  public void setHighestNumber(long number) {
    this.highestNumber = number;
  }

  public long getHighestNumber() {
    return highestNumber;
  }
}
