package com.jami.database.user;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;

public class userSettings {
  private List<String> enabledFeatures;

  private static List<String> defaultEnabledFeatures = Arrays.asList("levelling", "words");

  public userSettings(Document s) {
    this.enabledFeatures = s.getList("enabledFeatures", String.class, defaultEnabledFeatures);
  }

  public List<String> getEnabledFeatures() {
    return enabledFeatures;
  }

  public void setEnabledFeatures(List<String> features) {
    this.enabledFeatures = features;
  }

  public Document toDocument() {
    return new Document()
        .append("enabledFeatures", enabledFeatures);
  }
}
