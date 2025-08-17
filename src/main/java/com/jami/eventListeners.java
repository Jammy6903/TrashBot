package com.jami;

import java.util.ArrayList;
import java.util.List;

import com.jami.fun.levelling.commandsLevelling;
import com.jami.fun.levelling.levelling;
import com.jami.fun.wordCount.commandsWordCount;
import com.jami.fun.wordCount.wordCount;
import com.jami.utilities.featureRequests.commandsFeatureRequests;
import com.jami.utilities.guildLogging.logging;
import com.jami.utilities.guildOptions.commandsSettings;
import com.jami.utilities.info.commandsInfo;
import com.jami.botAdmin.commandsAdmin;
import com.jami.database.Feature;
import com.jami.database.guild.guild;
import com.jami.database.guild.guildSettings.guildSettings;
import com.jami.database.user.user;
import com.jami.database.user.userSettings;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class eventListeners {

  @SubscribeEvent
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

    if (App.CONFIG.getDisabledCommands().contains(event.getName())) {
      event.reply("Sorry! That command is disabled at the moment.").queue();
      return;
    }

    switch (event.getName()) {
      case "featurerequest":
        commandsFeatureRequests.newFeatureRequest(event);
        break;
      case "guild-settings":
        new commandsSettings(event).start();
        break;
      case "level":
        commandsLevelling.levellingCommands(event);
        break;
      case "word":
        commandsWordCount.wordCommands(event);
        break;
      case "info":
        commandsInfo.infoCommands(event);
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

    if (m.startsWith("a!") && App.CONFIG.getAdminIds().contains(u.getIdLong())) {
      commandsAdmin.adminCommands(event, null);
      return;
    }

    user uu = new user(u.getIdLong());
    userSettings us = uu.getSettings();
    guild gg = new guild(g.getIdLong());
    guildSettings gs = gg.getSettings();

    List<List<Feature>> disabledFeaturesLists = new ArrayList<>();
    disabledFeaturesLists.add(App.CONFIG.getDisabledFeatures());
    disabledFeaturesLists.add(us.getDisabledFeatures());
    disabledFeaturesLists.add(gs.getDisabledFeatures());

    // Levelling
    if (checkDisabled(disabledFeaturesLists, Feature.LEVELLING)) {
      levelling.incrementExps(event, gg, u.getIdLong());
    }

    // WordCount
    if (checkDisabled(disabledFeaturesLists, Feature.WORDS)
        && !gs.getWordsDisabledChannels().contains(c.getIdLong())) {
      wordCount.incrementWords(event.getMessage().getContentRaw(), gg);
    }

    // Message Logging
    if (checkDisabled(disabledFeaturesLists, Feature.LOGGING)) {
      logging.cacheMessage(event);
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
