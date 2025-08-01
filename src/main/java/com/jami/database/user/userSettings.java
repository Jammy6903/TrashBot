package com.jami.database.user;

import java.util.List;

import org.bson.Document;

public class userSettings {
  private List<String> enabledFeatures;

  public userSettings(Document s) {
    if (s != null) {
      this.enabledFeatures = s.getList("enabledFeatures", String.class);
    } else {
      this.enabledFeatures = null;
    }
  }

  public List<String> getEnabledFeatures() {
    return this.enabledFeatures;
  }

  public void setEnabledFeatures(List<String> ef) {
    this.enabledFeatures = ef;
  }

  public Document toDocument() {
    return new Document()
        .append("enabledFeatures", enabledFeatures);
  }
}
