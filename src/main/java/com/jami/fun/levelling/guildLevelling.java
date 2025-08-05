package com.jami.fun.levelling;

import com.jami.database.guild.guild;
import com.jami.database.guild.guildUser;
import com.jami.database.guild.guildSettings;

public class guildLevelling {
  public static void incrementExp(long guildId, long userId) {
    guild g = new guild(guildId);
    guildSettings settings = g.getSettings();
    guildUser user = g.getUser(userId);

    // Check if cooldown time has elapsed since users last message
    // if (System.currentTimeMillis() - user.getUserLastMessage() <=
    // settings.getExpCooldown() * 1000) {
    // return;
    // }

    user.setExp(user.getExp() + settings.getExpIncrement());
    user.setUserLastMessage(System.currentTimeMillis());

    g.commit();
  }
}
