package com.jami;

import java.util.List;

import com.jami.fun.levelling.commandsLevelling;
import com.jami.fun.levelling.levelling;
import com.jami.fun.wordCount.commandsWordCount;
import com.jami.fun.wordCount.wordCount;
import com.jami.utilities.featureRequests.commandsFeatureRequests;
import com.jami.utilities.guildAdmin.commandsSettings;
import com.jami.utilities.info.commandsInfo;
import com.jami.botAdmin.commandsAdmin;
import com.jami.database.guild.guild;
import com.jami.database.guild.guildSettings.guildSettings;
import com.jami.database.user.user;

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
        commandsSettings.guildSettingsCommand(event);
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

    guild gg = new guild(g.getIdLong());
    guildSettings gs = gg.getSettings();

    List<String> userDisabledFetures = new user(u.getIdLong()).getSettings().getDisabledFeatures();

    // Levelling
    if (!App.CONFIG.getDisabledFeatures().contains("levelling") && !userDisabledFetures.contains("levelling")
        && !gs.getDisabledFeatures().contains("levelling")) {
      levelling.incrementExps(event, gg, u.getIdLong());
    }

    // WordCount
    if (!App.CONFIG.getDisabledFeatures().contains("words") && !userDisabledFetures.contains("words")
        && !gs.getDisabledFeatures().contains("words") && !gs.getWordsDisabledChannels().contains(c.getIdLong())) {
      wordCount.incrementWords(event.getMessage().getContentRaw(), gg);
    }

    gg.commit();
  }

}
