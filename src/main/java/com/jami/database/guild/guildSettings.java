package com.jami.database.guild;

import org.bson.Document;

public class guildSettings {

  int expIncrement;
  int expVariation;
  long expCooldown;

  public guildSettings(Document s) {
    if (s != null) {
      this.expIncrement = s.getInteger("expIncrement");
      this.expVariation = s.getInteger("expVariation");
      this.expCooldown = s.getLong("expCooldown");
    } else {
      this.expIncrement = 3;
      this.expVariation = 1;
      this.expCooldown = 60;
    }
  }

  public void setExpIncrement(int exp) {
    this.expIncrement = exp;
  }

  public int getExpIncrement() {
    return expIncrement;
  }

  public void setExpVariation(int var) {
    this.expVariation = var;
  }

  public int getExpVariation() {
    return expVariation;
  }

  public void setExpCooldown(long cool) {
    this.expCooldown = cool;
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
