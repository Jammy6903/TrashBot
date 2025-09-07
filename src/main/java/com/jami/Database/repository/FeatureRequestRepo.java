package com.jami.Database.repository;

import com.jami.Database.Utilities.FeatureRequestRecord;

public interface FeatureRequestRepo {
  FeatureRequestRecord getById(String id);

  String createRequest(String title, String description, String userName, long userId);

  void save(FeatureRequestRecord record);
}
