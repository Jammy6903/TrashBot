package com.jami.Interaction.Fun.Levelling;

import java.util.Random;

import com.jami.App;
import com.jami.Database.Config.LevellingConfig.LevellingConfig;
import com.jami.Database.Guild.GuildUserRecord;
import com.jami.Database.Guild.guildSettings.LevellingSettings.LevellingSettings;
import com.jami.Database.User.UserRecord;
import com.jami.Database.infrastructure.mongo.MongoGuildRepo;
import com.jami.Database.infrastructure.mongo.MongoUserRepo;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Levelling {
  public static void incrementExps(MessageReceivedEvent event, UserRecord userRecord,
      LevellingSettings guildLevellingSettings) {
    long guildId = event.getGuild().getIdLong();
    long userId = event.getAuthor().getIdLong();
    LevellingConfig globalLevellingSettings = App.getGlobalConfig().getLevellingConfig();
    GuildUserRecord guildUserRecord = MongoGuildRepo.getUserById(guildId, userId);

    global(globalLevellingSettings, userRecord, userId);

    if (guild(guildLevellingSettings, guildUserRecord, guildId, userId)) {
      EmbedBuilder embed = new EmbedBuilder()
          .setTitle("LEVEL UP!")
          .setDescription(String.format("You've reached level %d!", guildUserRecord.getLevel() + 1));
      event.getMessage().replyEmbeds(embed.build()).queue();
    }
  }

  private static void global(LevellingConfig config, UserRecord user, long userId) {
    if (user.getUserLastMessage() < System.currentTimeMillis() + (config.getExpCooldown() * 1000)) {
      return;
    }

    int inc = variate(config.getExpIncrement(), config.getExpVariation());
    MongoUserRepo.incrementExp(userId, inc);
    if (user.getUserExp() + inc >= getRequiredExp(user.getUserLevel(), config.getLevelBase(),
        config.getLevelGrowth())) {
      MongoUserRepo.incrementLevel(userId);
    }
  }

  private static boolean guild(LevellingSettings settings, GuildUserRecord user, long guildId, long userId) {
    if (user.getUserLastMessage() < System.currentTimeMillis() + (settings.getExpCooldown() * 1000)) {
      return false;
    }

    int inc = variate(settings.getExpIncrement(), settings.getExpVariation());
    MongoGuildRepo.incrementExp(guildId, userId, inc);
    if (user.getExp() + inc >= getRequiredExp(user.getLevel(), settings.getLevelBase(), settings.getLevelGrowth())) {
      MongoGuildRepo.incrementLevel(guildId, userId);
      return true;
    }
    return false;
  }

  private static int variate(int inc, int var) {
    return new Random().nextInt((inc + var) - (inc - var) + 1) + (inc - var);
  }

  public static long getRequiredExp(int userLevel, long levelBase, double levelGrowth) {
    return (long) Math.floor(levelBase * Math.pow(userLevel + 1, levelGrowth));
  }
}
