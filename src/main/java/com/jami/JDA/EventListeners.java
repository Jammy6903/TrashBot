package com.jami.JDA;

import java.util.ArrayList;
import java.util.List;

import com.jami.App;
import com.jami.BotAdmin.CommandsAdmin;
import com.jami.Database.Feature;
import com.jami.Database.Guild.GuildRecord;
import com.jami.Database.Guild.guildSettings.GuildSettings;
import com.jami.Database.User.UserRecord;
import com.jami.Database.User.UserSettings;
import com.jami.Interaction.Fun.Levelling.CommandsLevelling;
import com.jami.Interaction.Fun.Levelling.Levelling;
import com.jami.Interaction.Fun.WordCount.CommandsWordCount;
import com.jami.Interaction.Fun.WordCount.WordCount;
import com.jami.Interaction.Utilities.FeatureRequests.commandsFeatureRequests;
import com.jami.Interaction.Utilities.GuildLogging.Logging;
import com.jami.Interaction.Utilities.GuildOptions.CommandsSettings;
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

    if (App.getGlobalConfig().getDisabledCommands().contains(event.getName())) {
      event.reply("Sorry! That command is disabled at the moment.").queue();
      return;
    }

    switch (event.getName()) {
      case "featurerequest":
        commandsFeatureRequests.newFeatureRequest(event);
        break;
      case "guild-settings":
        new CommandsSettings(event).start();
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

    if (m.startsWith("a!") && App.getGlobalConfig().getAdminIds().contains(u.getIdLong())) {
      CommandsAdmin.adminCommands(event, null);
      return;
    }

    UserRecord uu = new UserRecord(u.getIdLong());
    UserSettings us = uu.getSettings();
    GuildRecord gg = new GuildRecord(g.getIdLong());
    GuildSettings gs = gg.getSettings();

    List<List<Feature>> disabledFeaturesLists = new ArrayList<>();
    disabledFeaturesLists.add(App.getGlobalConfig().getDisabledFeatures());
    disabledFeaturesLists.add(us.getDisabledFeatures());
    disabledFeaturesLists.add(gs.getDisabledFeatures());

    // Levelling
    if (checkDisabled(disabledFeaturesLists, Feature.LEVELLING)) {
      Levelling.incrementExps(event, gg, u.getIdLong());
    }

    // WordCount
    if (checkDisabled(disabledFeaturesLists, Feature.WORDS)
        && !gs.getWordsDisabledChannels().contains(c.getIdLong())) {
      WordCount.incrementWords(event.getMessage().getContentRaw(), gg);
    }

    // Message Logging
    if (checkDisabled(disabledFeaturesLists, Feature.LOGGING)) {
      Logging.cacheMessage(event);
    }

    gg.commit();
  }

  private boolean checkDisabled(List<List<Feature>> lists, Feature feature) {
    for (List<Feature> list : lists) {
      if (list.contains(feature)) {
        return false;
      }
    }
    return true;
  }

}
