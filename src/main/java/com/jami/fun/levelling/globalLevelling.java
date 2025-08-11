package com.jami.fun.levelling;

import java.util.Random;

import com.jami.database.user.user;

public class globalLevelling {
  private static int inc = 3;
  private static int var = 1;
  private static long cool = 60;

  private static long levelBase = 200;
  private static double levelGrowth = 1.5;

  public static void incrementExp(long userId) {
    user u = new user(userId);

    // Check if cooldown time has elapsed since users last message
    if (System.currentTimeMillis() - u.getLastMessage() <= cool * 1000) {
      return;
    }

    // Increment exp by set values, check for level up and increment level
    long expIncrement = new Random().nextInt((inc + var) - (inc - var) + 1) + (inc - var);
    long setExp = u.getExp() + expIncrement;

    u.setExp(setExp);
    if (isLevelUp(setExp, u.getLevel())) {
      u.incrementLevel();
    }

    // set last message to current millis
    u.setLastMessage(System.currentTimeMillis());

    u.commit();
  }

  private static boolean isLevelUp(long exp, int level) {
    if (exp >= Math.floor(levelBase * Math.pow(level + 1, levelGrowth))) {
      return true;
    }
    return false;
  }
}
