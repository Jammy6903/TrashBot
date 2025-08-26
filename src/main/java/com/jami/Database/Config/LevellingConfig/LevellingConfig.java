package com.jami.Database.Config.LevellingConfig;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class LevellingConfig {

  @BsonProperty("expIncrement")
  private int expIncrement;

  @BsonProperty("expVariation")
  private int expVariation;

  @BsonProperty("expCooldown")
  private int expCooldown;

  @BsonProperty("levelBase")
  private long levelBase;

  @BsonProperty("levelGrowth")
  private double levelGrowth;

  public LevellingConfig() {
  }

  public LevellingConfig(boolean d) {
    this.expIncrement = 3;
    this.expVariation = 1;
    this.expCooldown = 60;
    this.levelBase = 200;
    this.levelGrowth = 1.5;
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
