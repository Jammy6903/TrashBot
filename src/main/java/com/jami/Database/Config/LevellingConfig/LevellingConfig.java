package com.jami.Database.Config.LevellingConfig;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class LevellingConfig {

  @BsonProperty("expIncrement")
  private int expIncrement = 3;

  @BsonProperty("expVariation")
  private int expVariation = 1;

  @BsonProperty("expCooldown")
  private int expCooldown = 60;

  @BsonProperty("levelBase")
  private long levelBase = 200;

  @BsonProperty("levelGrowth")
  private double levelGrowth = 1.5;

  @BsonExtraElements
  private Document legacyValues;

  public LevellingConfig() {
  }

  // Exp

  public void setExpIncrement(int exp) {
    this.expIncrement = exp;
  }

  public int getExpIncrement() {
    return expIncrement;
  }

  public void setExpVariation(int exp) {
    this.expVariation = exp;
  }

  public int getExpVariation() {
    return expVariation;
  }

  public void setExpCooldown(int cooldown) {
    this.expCooldown = cooldown;
  }

  public int getExpCooldown() {
    return expCooldown;
  }

  // Level

  public void setLevelBase(long exp) {
    this.levelBase = exp;
  }

  public long getLevelBase() {
    return levelBase;
  }

  public void setLevelGrowth(double multiplier) {
    this.levelGrowth = multiplier;
  }

  public double getLevelGrowth() {
    return levelGrowth;
  }

}
