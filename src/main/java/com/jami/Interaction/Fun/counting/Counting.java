package com.jami.Interaction.Fun.counting;

import com.jami.Database.Guild.GuildRecord;
import com.jami.Database.infrastructure.mongo.MongoGuildRepo;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Counting {
  public static void newCount(MessageReceivedEvent event, GuildRecord guildRecord) {
    String message = event.getMessage().getContentRaw();
    long guildId = event.getGuild().getIdLong();
    long currentNumber = guildRecord.getCounting().getCurrentNumber();
    long getCurrentNumberUser = guildRecord.getCounting().getCurrentNumberUser();

    long newNumber;
    try {
      newNumber = Long.valueOf(message.substring(0, message.indexOf(" ")));
    } catch (Exception e) {
      return;
    }

    if (guildRecord.getCounting().getCurrentNumberUser() != null
        && guildRecord.getCounting().getCurrentNumberUser() != event.getAuthor().getIdLong()) {

    }

    if (newNumber == (currentNumber + 1)) {
      incrementCount(guildId, newNumber);
      event.getMessage().addReaction(Emoji.fromUnicode("✅")).queue();
      return;
    }

    resetCount(guildId);
    event.getMessage()
        .reply("**WRONG NUMBER!** The next number was " + currentNumber + 1 + ". Start counting from 1 again.");

  }

  private static void resetCount(long guildId) {
    MongoGuildRepo.setCount(guildId, 0);
  }

  private static void incrementCount(long guildId, long newNumber) {
    MongoGuildRepo.setCount(guildId, newNumber);
  }
}
