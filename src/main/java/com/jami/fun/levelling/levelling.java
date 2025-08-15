package com.jami.fun.levelling;

import com.jami.database.guild.guild;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class levelling {
  public static void incrementExps(MessageReceivedEvent event, guild g, long userId) {
    globalLevelling global = new globalLevelling(userId);
    global.incrementExp();

    guildLevelling guild = new guildLevelling(event, g);
    if (guild.incrementExp()) {
      guild.announceLevelUp();
    }
  }
}
