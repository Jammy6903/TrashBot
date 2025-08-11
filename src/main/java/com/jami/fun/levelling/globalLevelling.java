package com.jami.fun.levelling;

import java.util.Random;

import com.jami.database.user.user;

public class globalLevelling {
  private static int inc = 3;
  private static int var = 1;
  private static long cool = 60;

  public static void incrementExp(long userId) {
    user u = new user(userId);

    // Check if user has levelling feature enabled
    if (!u.getSettings().getEnabledFeatures().contains("levelling")) {
      return;
    }

    // Check if cooldown time has elapsed since users last message
    if (System.currentTimeMillis() - u.getLastMessage() <= cool * 1000) {
      return;
    }

    // Increment exp by set values, check for level up and increment level
    long expIncrement = new Random().nextInt((inc + var) - (inc - var) + 1) + (inc - var);
    long setExp = u.getExp() + expIncrement;

    u.setExp(setExp);
    if (isLevelUp(setExp)) {
      u.incrementLevel();
    }

    // set last message to current millis
    u.setLastMessage(System.currentTimeMillis());

    u.commit();
  }

  private static boolean isLevelUp(long exp) {
    return false;
  }
}
