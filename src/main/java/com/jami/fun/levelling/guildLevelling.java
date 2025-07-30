package com.jami.fun.levelling;

import com.jami.database.guild;
import com.jami.database.guild.settings;
import com.jami.database.guild.user;

public class guildLevelling {
  public static void incrementExp(long guildId, long userId) {
    guild g = guild.getGuild(guildId);
    settings s = g.getSettings();
    user u = g.getUser(userId);
    long userExp = u.getExp() + s.getExpIncrement();
    u.setExp(userExp);
    u.commit();
    g.commit();
  }
}
