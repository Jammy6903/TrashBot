package com.jami.Database.User;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonProperty;

import com.jami.Database.Enumerators.Feature;

public class UserSettings {

  @BsonProperty("disabledFeatures")
  private List<Feature> disabledFeatures = new ArrayList<>();

  @BsonExtraElements
  private Document legacyValues;

  public UserSettings() {
  }

  // DisabledFeatures

  public void addDisabledFeature(Feature feature) {
    this.disabledFeatures.add(feature);
  }

  public void removeDisabledFeature(Feature feature) {
    this.disabledFeatures.remove(feature);
  }

  public List<Feature> getDisabledFeatures() {
    return disabledFeatures;
  }

}
