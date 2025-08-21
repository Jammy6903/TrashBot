package com.jami.Interaction.Fun.Levelling;

import com.jami.Database.Guild.GuildRecord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Levelling {
  public static void incrementExps(MessageReceivedEvent event, GuildRecord g, long userId) {
    GlobalLevelling global = new GlobalLevelling(userId);
    global.incrementExp();

    GuildLevelling guild = new GuildLevelling(event, g);
    if (guild.incrementExp()) {
      guild.announceLevelUp();
    }
  }
}
