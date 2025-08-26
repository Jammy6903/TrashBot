package com.jami.Database.Guild.guildSettings.LevellingSettings;

import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonProperty;

import com.jami.Database.Guild.guildSettings.LevellingSettings.LevellingRole.LevellingRole;

public class LevellingSettings {

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

  @BsonProperty("levellingRoles")
  private List<LevellingRole> levellingRoles = new ArrayList<>();

  @BsonProperty("disabledChannels")
  private List<Long> disabledChannels = new ArrayList<>();

  public LevellingSettings() {
  }

  public LevellingSettings(boolean d) {
    this.expIncrement = 3;
    this.expVariation = 1;
    this.expCooldown = 60;
    this.levelBase = 200L;
    this.levelGrowth = 1.5;
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
}
