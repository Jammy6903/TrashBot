package com.jami.Database.repository;

import com.jami.Database.User.UserRecord;

public interface UserRepo {
  UserRecord getById(long userId);

  void updateLastMessage(long userId);

  void incrementExp(long userId, int exp);

  void incrementLevel(long userId);

  void save(UserRecord record);
}
