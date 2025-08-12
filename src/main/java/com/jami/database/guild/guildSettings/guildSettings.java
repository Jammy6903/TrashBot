package com.jami.database.guild.guildSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bson.Document;

import com.jami.database.getOrDefault;

public class guildSettings {
  private int expIncrement;
  private int expVariation;
  private long expCooldown;
  private long levelBase;
  private double levelGrowth;
  private List<Integer> roleLevels;
  private List<Document> levellingRoles;
  private List<String> enabledFeatures;
  private List<Long> wordsDisabledChannels;

  private static int defaultExpIncrement = 3;
  private static int defaultExpVariation = 1;
  private static long defaultExpCooldown = 60;
  private static long defaultLevelBase = 200;
  private static double defaultLevelGrowth = 1.5;
  private static List<Integer> defaultRoleLevels = new ArrayList<>();
  private static List<Document> defaultLevellingRoles = new ArrayList<>();
  private static List<String> defaultEnabledFeatures = Arrays.asList("levelling", "words");
  private static List<Long> defaultWordsDisabledChannels = new ArrayList<>();

  public guildSettings(Document s) {
    if (s == null) {
      s = new Document();
    }
    this.expIncrement = s.getInteger("expIncrement", defaultExpIncrement);
    this.expVariation = s.getInteger("expVariation", defaultExpVariation);
    this.expCooldown = getOrDefault.Long(s, "expCooldown", defaultExpCooldown);
    this.levelBase = getOrDefault.Long(s, "levelBase", defaultLevelBase);
    this.levelGrowth = getOrDefault.Double(s, "levelGrowth", defaultLevelGrowth);
    this.roleLevels = s.getList("roleLevels", Integer.class, defaultRoleLevels);
    this.levellingRoles = s.getList("levellingRoles", Document.class, defaultLevellingRoles);
    this.enabledFeatures = s.getList("enabledFeatures", String.class, defaultEnabledFeatures);
    this.wordsDisabledChannels = s.getList("wordsDisabledChannels", Long.class, defaultWordsDisabledChannels);
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

  public void setExpCooldown(long cooldown) {
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

  public void setRoleLevels(List<Integer> levels) {
    this.roleLevels = levels;
  }

  public List<Integer> getRoleLevels() {
    return roleLevels;
  }

  public void addRoleLevel(int level) {
    this.roleLevels.add(level);
  }

  public void setLevellingRoles(List<Document> docs) {
    this.levellingRoles = docs;
  }

  public void addLevellingRole(Document doc) {
    this.levellingRoles.add(doc);
  }

  public List<Document> getLevellingRoles() {
    return levellingRoles;
  }

  public void setEnabledFeatures(List<String> features) {
    this.enabledFeatures = features;
  }

  public List<String> getEnabledFeatures() {
    return enabledFeatures;
  }

  public void setWordsDisabledChannels(List<Long> channels) {
    this.wordsDisabledChannels = channels;
  }

  public List<Long> getWordsDisabledChannels() {
    return wordsDisabledChannels;
  }

  public Document toDocument() {
    return new Document()
        .append("expIncrement", expIncrement)
        .append("expVariation", expVariation)
        .append("expCooldown", expCooldown)
        .append("levelBase", levelBase)
        .append("levelGrowth", levelGrowth)
        .append("roleLevels", roleLevels)
        .append("levellingRoles", levellingRoles)
        .append("enabledFeatures", enabledFeatures)
        .append("wordsDisabledChannels", wordsDisabledChannels);
  }
}
