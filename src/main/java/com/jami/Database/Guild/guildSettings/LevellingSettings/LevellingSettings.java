package com.jami.Database.Guild.guildSettings.LevellingSettings;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.jami.Database.GetorDefault;
import com.jami.Database.Guild.guildSettings.LevellingSettings.LevellingRole.LevellingRole;

public class LevellingSettings {
  private int expIncrement;
  private int expVariation;
  private int expCooldown;
  private long levelBase;
  private double levelGrowth;
  private List<LevellingRole> levellingRoles = new ArrayList<>();
  private List<Long> disabledChannels;

  public LevellingSettings(Document doc) {
    this.expIncrement = GetorDefault.Integer(doc, "expIncrement", 3);
    this.expVariation = GetorDefault.Integer(doc, "expVariation", 1);
    this.expCooldown = GetorDefault.Integer(doc, "expCooldown", 60);
    this.levelBase = GetorDefault.Long(doc, "levelBase", 200L);
    this.levelGrowth = GetorDefault.Double(doc, "levelGrowth", 1.5);
    for (Document lr : doc.getList("levellingRoles", Document.class, new ArrayList<>())) {
      this.levellingRoles.add(new LevellingRole(lr));
    }
    this.disabledChannels = GetorDefault.List(doc, "disabledChannels", Long.class, new ArrayList<>());
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

  public void setLevelBase(long base) {
    this.levelBase = base;
  }

  public long getLevelBase() {
    return levelBase;
  }

  public void setLevelGrowth(double growth) {
    this.levelGrowth = growth;
  }

  public double getLevelGrowth() {
    return levelGrowth;
  }

  public void addLevellingRole(LevellingRole role) {
    this.levellingRoles.add(role);
  }

  public void removeLevellingRole(LevellingRole role) {
    this.levellingRoles.remove(role);
  }

  public List<LevellingRole> getLevellingRoles() {
    return levellingRoles;
  }

  public void addDisabledChannel(long channelId) {
    this.disabledChannels.add(channelId);
  }

  public void removeDisabledChannel(long channelId) {
    this.disabledChannels.remove(channelId);
  }

  public List<Long> getDisabledChannels() {
    return disabledChannels;
  }

  public Document toDocument() {
    List<Document> lrs = new ArrayList<>();
    for (LevellingRole role : levellingRoles) {
      lrs.add(role.toDocument());
    }
    return new Document()
        .append("expIncrement", expIncrement)
        .append("expVariation", expVariation)
        .append("expCooldown", expCooldown)
        .append("levelBase", levelBase)
        .append("levelGrowth", levelGrowth)
        .append("levellingRoles", lrs)
        .append("disabledChannels", disabledChannels);
  }
}
