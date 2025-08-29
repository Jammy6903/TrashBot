package com.jami.Database.Guild.guildSettings.LevellingSettings;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonProperty;

import com.jami.Database.Guild.guildSettings.LevellingSettings.LevellingRole.LevellingRole;

public class LevellingSettings {

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

  @BsonProperty("levellingRoles")
  private List<LevellingRole> levellingRoles = new ArrayList<>();

  @BsonProperty("disabledChannels")
  private List<Long> disabledChannels = new ArrayList<>();

  @BsonExtraElements
  private Document legacyValues;

  public LevellingSettings() {
  }

  // ExpIncrement

  public void setExpIncrement(int exp) {
    this.expIncrement = exp;
  }

  public int getExpIncrement() {
    return expIncrement;
  }

  // ExpVariation

  public void setExpVariation(int variation) {
    this.expVariation = variation;
  }

  public int getExpVariation() {
    return expVariation;
  }

  // ExpCooldown

  public void setExpCooldown(int cooldown) {
    this.expCooldown = cooldown;
  }

  public int getExpCooldown() {
    return expCooldown;
  }

  // LevelBase

  public void setLevelBase(long base) {
    this.levelBase = base;
  }

  public long getLevelBase() {
    return levelBase;
  }

  // LevelGrowth

  public void setLevelGrowth(double growth) {
    this.levelGrowth = growth;
  }

  public double getLevelGrowth() {
    return levelGrowth;
  }

  // LevellingRoles

  public void addLevellingRole(LevellingRole role) {
    this.levellingRoles.add(role);
  }

  public void removeLevellingRole(LevellingRole role) {
    this.levellingRoles.remove(role);
  }

  public List<LevellingRole> getLevellingRoles() {
    return levellingRoles;
  }

  public void setLevellingRoles(List<LevellingRole> levellingRoles) {
    this.levellingRoles = levellingRoles;
  }

  // DisabledChannels

  public void addDisabledChannel(long channelId) {
    this.disabledChannels.add(channelId);
  }

  public void removeDisabledChannel(long channelId) {
    this.disabledChannels.remove(channelId);
  }

  public List<Long> getDisabledChannels() {
    return disabledChannels;
  }

  public void setDisabledChannels(List<Long> disabledChannels) {
    this.disabledChannels = disabledChannels;
  }
}
