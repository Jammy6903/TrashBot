package com.jami.fun.levelling;

import java.util.Random;

import com.jami.App;
import com.jami.database.user.user;

public class globalLevelling {
  private user u;
  private long userExp;
  private int userLevel;
  private long userLastMessage;

  public globalLevelling(long userId) {
    this.u = new user(userId);
    this.userExp = u.getExp();
    this.userLevel = u.getLevel();
    this.userLastMessage = u.getLastMessage();
  }

  public void incrementExp() {

    int inc = App.CONFIG.getExpIncrement();
    int var = App.CONFIG.getExpVariation();
    long cool = App.CONFIG.getExpCooldown();

    long base = App.CONFIG.getLevelBase();
    double growth = App.CONFIG.getLevelGrowth();

    if (System.currentTimeMillis() - userLastMessage <= cool * 1000) {
      return;
    }

    userExp = userExp + new Random().nextInt((inc + var) - (inc - var) + 1) + (inc - var);

    u.setExp(userExp);

    u.setLastMessage(System.currentTimeMillis());

    if (u.getRequiredExp(base, growth) >= userExp) {
      userLevel++;
      u.setLevel(userLevel);
    }

    u.commit();
  }
}
