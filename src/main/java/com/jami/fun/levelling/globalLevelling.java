package com.jami.fun.levelling;

import com.jami.database.user.user;

public class globalLevelling {
  private static int expIncrement = 3;
  private static long expCooldown = 60;

  public static void incrementExp(long userId) {
    user u = new user(userId);

    if (System.currentTimeMillis() - u.getLastMessage() <= expCooldown * 1000) {
      return;
    }

    u.setExp(u.getExp() + expIncrement);
    u.setLastMessage(System.currentTimeMillis());

    u.commit();
  }
}
