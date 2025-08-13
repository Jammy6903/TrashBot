package com.jami.database.user;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class userSettings {
  private List<String> disabledFeatures;

  public userSettings(Document s) {
    if (s == null) {
      s = new Document();
    }
    this.disabledFeatures = s.getList("disabledFeatures", String.class, new ArrayList<>());
  }

  public List<String> getDisabledFeatures() {
    return disabledFeatures;
  }

  public void setDisabledFeatures(List<String> features) {
    this.disabledFeatures = features;
  }

  public void addDisabledFeature(String feature) {
    this.disabledFeatures.add(feature);
  }

  public void removeDisabledFeature(String feature) {
    this.disabledFeatures.remove(feature);
  }

  public Document toDocument() {
    return new Document()
        .append("disabledFeatures", disabledFeatures);
  }
}
