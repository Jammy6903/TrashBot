package com.jami.bot;

import java.util.ArrayList;
import java.util.List;

import com.jami.App;
import com.jami.BotAdmin.Admin;
import com.jami.Database.Config.ConfigRecord;
import com.jami.Database.Enumerators.Feature;
import com.jami.Database.Guild.GuildRecord;
import com.jami.Database.Guild.guildSettings.GuildSettings;
import com.jami.Database.User.UserRecord;
import com.jami.Database.User.UserSettings;
import com.jami.Database.infrastructure.mongo.Mongo;
import com.jami.Database.infrastructure.mongo.MongoGuildRepo;
import com.jami.Database.infrastructure.mongo.MongoUserRepo;
import com.jami.Interaction.Fun.Levelling.CommandsLevelling;
import com.jami.Interaction.Fun.Levelling.Levelling;
import com.jami.Interaction.Fun.WordCount.CommandsWordCount;
import com.jami.Interaction.Fun.WordCount.WordCount;
import com.jami.Interaction.Fun.counting.Counting;
import com.jami.Interaction.Utilities.FeatureRequests.commandsFeatureRequests;
import com.jami.Interaction.Utilities.GuildLogging.Logging;
import com.jami.Interaction.Utilities.Info.CommandsInfo;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class EventListeners {

  @SubscribeEvent
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

    switch (event.getName()) {
      case "featurerequest":
        commandsFeatureRequests.newFeatureRequest(event);
        break;
      case "guild-settings":
        break;
      case "level":
        CommandsLevelling.levellingCommands(event);
        break;
      case "word":
        CommandsWordCount.wordCommands(event);
        break;
      case "info":
        CommandsInfo.infoCommands(event);
        break;
      default:
        event.reply("Unknown command, how the fuck did you even manage that.");
        break;
    }
  }

  @SubscribeEvent
  public void onMessageReceived(MessageReceivedEvent event) {
    User u = event.getAuthor();
    Guild g = event.getGuild();
    Channel c = event.getChannel();
    String m = event.getMessage().getContentRaw();

    if (u.isBot()) {
      return;
    }

    ConfigRecord globalConfig = Mongo.getGlobalConfig();

    if (m.startsWith("a!") && globalConfig.getAdminIds().contains(u.getIdLong())) {
      Admin.adminCommands(event, null);
      return;
    }

    UserRecord userRecord = Mongo.getUserRepo().getById(u.getIdLong());
    UserSettings userSettings = userRecord.getSettings();
    GuildRecord guildRecord = Mongo.getGuildRepo().getById(g.getIdLong());
    GuildSettings guildSettings = guildRecord.getSettings();

    List<List<Feature>> disabledFeaturesLists = new ArrayList<>();
    disabledFeaturesLists.add(globalConfig.getDisabledFeatures());
    disabledFeaturesLists.add(userSettings.getDisabledFeatures());
    disabledFeaturesLists.add(guildSettings.getDisabledFeatures());

    // Levelling
    if (!isDisabled(disabledFeaturesLists, Feature.LEVELLING)) {
      Levelling.incrementExps(event, userRecord, guildSettings.getLevellingSettings());
    }

    // WordCount
    boolean channelDisabled = guildSettings.getWordsDisabledChannels().contains(c.getIdLong());
    if (!isDisabled(disabledFeaturesLists, Feature.WORDS) && !channelDisabled) {
      WordCount.incrementWords(m, g.getIdLong());
    }

    // Message Logging
    if (!isDisabled(disabledFeaturesLists, Feature.LOGGING)) {
      Logging.cacheMessage(event);
    }

    System.out.println(guildSettings.getCountingSettings().getChannelId());

    // Counting
    if (!isDisabled(disabledFeaturesLists, Feature.COUNTING) &&  guildSettings.getCountingSettings().channelMatches(c.getIdLong())) {
      Counting.newCount(event, guildRecord);
    }

  }

  private boolean isDisabled(List<List<Feature>> lists, Feature feature) {
    for (List<Feature> list : lists) {
      if (list.contains(feature)) {
        return true;
      }
    }
    return false;
  }

}
