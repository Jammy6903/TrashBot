package com.jami.database.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;

import com.jami.database.Feature;

public class userSettings {
  private List<Feature> disabledFeatures;

  public userSettings(Document s) {
    if (s == null) {
      s = new Document();
    }
    List<String> featureStrings = s.getList("disabledFeatures", String.class, new ArrayList<>());
    this.disabledFeatures = featureStrings.stream()
        .map(Feature::valueOf) // converts "MESSAGE_DELETED" -> LogType.MESSAGE_DELETED
        .collect(Collectors.toList());
  }

  public List<Feature> getDisabledFeatures() {
    return disabledFeatures;
  }

  public void setDisabledFeatures(List<Feature> features) {
    this.disabledFeatures = features;
  }

  public void addDisabledFeature(Feature feature) {
    this.disabledFeatures.add(feature);
  }

  public void removeDisabledFeature(Feature feature) {
    this.disabledFeatures.remove(feature);
  }

  public Document toDocument() {
    return new Document()
        .append("disabledFeatures", disabledFeatures);
  }
}
