package com.jami.database.guild;

import org.bson.Document;

public class guildSettings {

  private int expIncrement;
  private int expVariation;
  private long expCooldown;

  public guildSettings(Document s) {
    if (s == null) {
      s = new Document();
      this.expIncrement = 3;
      this.expVariation = 1;
      this.expCooldown = 60;
    } else {
      this.expIncrement = s.getInteger("expIncrement");
      this.expVariation = s.getInteger("expVariation");
      this.expCooldown = s.getLong("expCooldown");
    }
  }

  public void setExpIncrement(int exp) {
    this.expIncrement = exp;
  }

  public int getExpIncrement() {
    return expIncrement;
  }

  public void setExpVariation(int variation) {
    this.expVariation = variation;
  }

  public int getExpVariation() {
    return expVariation;
  }

  public void setExpCooldown(int cooldown) {
    this.expCooldown = cooldown;
  }

  public long getExpCooldown() {
    return expCooldown;
  }

  public Document toDocument() {
    return new Document()
        .append("expIncrement", expIncrement)
        .append("expVariation", expVariation)
        .append("expCooldown", expCooldown);
  }
}
