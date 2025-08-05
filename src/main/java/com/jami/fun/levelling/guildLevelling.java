package com.jami.fun.levelling;

import java.util.Random;

import com.jami.database.guild.guild;
import com.jami.database.guild.guildUser;
import com.jami.database.user.user;
import com.jami.database.guild.guildSettings;

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

    long expIncrement = new Random()
        .nextLong((s.getExpIncrement() + s.getExpVariation()) - (s.getExpIncrement() - s.getExpVariation()) + 1)
        + (s.getExpIncrement() - s.getExpVariation());
    gu.setExp(gu.getExp() + expIncrement);
    gu.setUserLastMessage(System.currentTimeMillis());

    g.commit();
  }
}
