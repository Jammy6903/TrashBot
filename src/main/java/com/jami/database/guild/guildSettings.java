package com.jami.database.guild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

public class guildSettings {

  private int expIncrement;
  private int expVariation;
  private long expCooldown;
  private List<String> enabledFeatures;
  private static List<String> defaultEnabledFeatures = Arrays.asList("levelling", "words");
  private List<Long> wordsDisabledChannels;

  public guildSettings(Document s) {
    this.expIncrement = 3;
    this.expVariation = 1;
    this.expCooldown = 60;
    this.enabledFeatures = new ArrayList<>();
    this.enabledFeatures.addAll(defaultEnabledFeatures);
    this.wordsDisabledChannels = new ArrayList<>();
    if (s != null) {
      this.expIncrement = s.getInteger("expIncrement");
      this.expVariation = s.getInteger("expVariation");
      this.expCooldown = s.getLong("expCooldown");
      if (s.getList("enabledFeatures", String.class) != null) {
        this.enabledFeatures = s.getList("enabledFeatures", String.class);
      }
      this.wordsDisabledChannels = s.getList("wordsDisabledChannels", Long.class);
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
