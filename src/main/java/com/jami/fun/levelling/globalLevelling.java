package com.jami.fun.levelling;

import java.util.Random;

import com.jami.database.user.user;

public class globalLevelling {
  private user u;
  private long userExp;
  private int userLevel;
  private long userLastMessage;

  // To-do, set these variables somewhere else

  private static int inc = 3;
  private static int var = 1;
  private static long cooldown = 60;

  private static long levelBase = 200;
  private static double levelGrowth = 1.5;

  public globalLevelling(long userId) {
    this.u = new user(userId);
    this.userExp = u.getExp();
    this.userLevel = u.getLevel();
    this.userLastMessage = u.getLastMessage();
  }

  public void incrementExp() {

    if (System.currentTimeMillis() - userLastMessage <= cooldown * 1000) {
      return;
    }

    userExp = userExp + new Random().nextInt((inc + var) - (inc - var) + 1) + (inc - var);

    u.setExp(userExp);

    u.setLastMessage(System.currentTimeMillis());

    if (u.getRequiredExp(levelBase, levelGrowth) >= userExp) {
      userLevel++;
      u.setLevel(userLevel);
    }

    u.commit();
  }
}
