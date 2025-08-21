package com.jami.Interaction.Fun.Levelling;

import java.util.Random;

import com.jami.App;
import com.jami.Database.User.UserRecord;

public class GlobalLevelling {
  private UserRecord u;
  private long userExp;
  private int userLevel;
  private long userLastMessage;

  public GlobalLevelling(long userId) {
    this.u = new UserRecord(userId);
    this.userExp = u.getExp();
    this.userLevel = u.getLevel();
    this.userLastMessage = u.getLastMessage();
  }

  public void incrementExp() {

    int inc = App.getGlobalConfig().getExpIncrement();
    int var = App.getGlobalConfig().getExpVariation();
    long cool = App.getGlobalConfig().getExpCooldown();

    long base = App.getGlobalConfig().getLevelBase();
    double growth = App.getGlobalConfig().getLevelGrowth();

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
