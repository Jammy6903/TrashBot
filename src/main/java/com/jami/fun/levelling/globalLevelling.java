package com.jami.fun.levelling;

import com.jami.database.user;

public class globalLevelling {
  private static int expIncrement = 3;

  public static void incrementExp(long userId) {
    user u = user.getUser(userId);
    long exp = u.getExp() + expIncrement;
    u.setExp(exp);
    u.commit();
  }
}
