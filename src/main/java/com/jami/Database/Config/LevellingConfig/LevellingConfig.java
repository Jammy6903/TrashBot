package com.jami.Database.Config.LevellingConfig;

import org.bson.Document;

import com.jami.Database.GetorDefault;

public class LevellingConfig {

  private int expIncrement;
  private int expVariation;
  private int expCooldown;
  private long levelBase;
  private double levelGrowth;

  private static int defaultExpIncrement = 3;
  private static int defaultExpVariation = 1;
  private static int defaultExpCooldown = 60;
  private static long defaultLevelBase = 200;
  static double defaultlevelGrowth = 1.5;

  public LevellingConfig(Document doc) {
    this.expIncrement = GetorDefault.Integer(doc, "expIncrement", defaultExpIncrement);
    this.expVariation = GetorDefault.Integer(doc, "expVariation", defaultExpVariation);
    this.expCooldown = GetorDefault.Integer(doc, "expCooldown", defaultExpCooldown);
    this.levelBase = GetorDefault.Long(doc, "levelBase", defaultLevelBase);
    this.levelGrowth = GetorDefault.Double(doc, "levelGrowth", defaultlevelGrowth);
  }

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

  public Document toDocument() {
    return new Document()
        .append("expIncrement", expIncrement)
        .append("expVariation", expVariation)
        .append("expCooldown", expCooldown)
        .append("levelBase", levelBase)
        .append("levelGrowth", levelGrowth);
  }
}
