package com.jami.database.guild.guildSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.jami.database.getOrDefault;

public class guildSettings {
  private int expIncrement;
  private int expVariation;
  private long expCooldown;
  private List<String> enabledFeatures;
  private List<Long> wordsDisabledChannels;
  private List<Document> levellingRoles;

  private static int defaultExpIncrement = 3;
  private static int defaultExpVariation = 1;
  private static long defaultExpCooldown = 60;
  private static List<String> defaultEnabledFeatures = Arrays.asList("levelling", "words");
  private static List<Long> defaultWordsDisabledChannels = new ArrayList<>();
  private static List<Document> defaultLevellingRoles = new ArrayList<>();

  public guildSettings(Document s) {
    if (s == null) {
      s = new Document();
    }
    this.expIncrement = s.getInteger("expIncrement", defaultExpIncrement);
    this.expVariation = s.getInteger("expVariation", defaultExpVariation);
    this.expCooldown = getOrDefault.Long(s, "expCooldown", defaultExpCooldown);
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

  public void setExpCooldown(int cooldown) {
    this.expCooldown = cooldown;
  }

  public long getExpCooldown() {
    return expCooldown;
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
        .append("enabledFeatures", enabledFeatures)
        .append("wordsDisabledChannels", wordsDisabledChannels);
  }
}
