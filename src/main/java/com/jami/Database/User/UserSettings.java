package com.jami.Database.User;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.jami.Database.GetorDefault;
import com.jami.Database.Enumerators.EnumToString;
import com.jami.Database.Enumerators.Feature;

public class UserSettings {
  private List<Feature> disabledFeatures;

  public UserSettings(Document doc) {
    this.disabledFeatures = GetorDefault.EnumList(doc, "disabledFeatures", Feature.class, new ArrayList<>());
  }

  public void addDisabledFeature(Feature feature) {
    this.disabledFeatures.add(feature);
  }

  public void removeDisabledFeature(Feature feature) {
    this.disabledFeatures.remove(feature);
  }

  public List<Feature> getDisabledFeatures() {
    return disabledFeatures;
  }

  public Document toDocument() {
    return new Document()
        .append("disabledFeatures", EnumToString.getFromList(disabledFeatures));
  }
}
