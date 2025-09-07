package com.jami.Database.repository;

import com.jami.Database.Config.ConfigRecord;

public interface ConfigRepo {
  ConfigRecord getById(String id);

  ConfigRecord getByName(String name);

  void create(String name);

  void save(ConfigRecord record);
}
