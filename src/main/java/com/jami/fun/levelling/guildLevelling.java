package com.jami.fun.levelling;

import java.util.Random;

import com.jami.database.guild.guild;
import com.jami.database.guild.guildUser;
import com.jami.database.user.user;
import com.jami.database.guild.guildSettings.*;

public class guildLevelling {
  public static void incrementExp(long guildId, long userId) {
    guild g = new guild(guildId);
    guildSettings s = g.getSettings();
    guildUser gu = g.getUser(userId);
    user u = new user(userId);

    // Check if user has levelling feature enabled
    if (!u.getSettings().getEnabledFeatures().contains("levelling")) {
      return;
    }

    // Check if cooldown time has elapsed since users last message
    if (System.currentTimeMillis() - gu.getUserLastMessage() <= s.getExpCooldown() * 1000) {
      return;
    }

    // Increment exp by guild values, check for level up and increment level
    int einc = s.getExpIncrement();
    int evar = s.getExpVariation();

    long expIncrement = new Random().nextLong((einc + evar) - (einc - evar) + 1) + (einc - evar);
    long setExp = gu.getExp() + expIncrement;

    gu.setExp(setExp);
    if (isLevelUp(setExp)) {
      gu.incrementLevel();
    }

    // Set last message to current millis
    gu.setUserLastMessage(System.currentTimeMillis());

    g.commit();
  }

  private static boolean isLevelUp(long exp) {
    return false;
  }
}
