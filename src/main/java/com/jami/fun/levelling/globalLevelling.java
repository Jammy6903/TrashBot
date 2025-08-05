package com.jami.fun.levelling;

import java.util.Random;

import com.jami.database.user.user;

public class globalLevelling {
  private static int expIncrement = 3;
  private static int expVariation = 1;
  private static long expCooldown = 60;

  public static void incrementExp(long userId) {
    user u = new user(userId);

    if (!u.getSettings().getEnabledFeatures().contains("levelling")) {
      return;
    }

    if (System.currentTimeMillis() - u.getLastMessage() <= expCooldown * 1000) {
      return;
    }

    expIncrement = new Random().nextInt((expIncrement + expVariation) - (expIncrement - expVariation) + 1)
        + (expIncrement - expVariation);
    u.setExp(u.getExp() + expIncrement);
    u.setLastMessage(System.currentTimeMillis());

    u.commit();
  }
}
