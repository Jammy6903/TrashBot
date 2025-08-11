package com.jami;

import java.util.List;

import com.jami.fun.levelling.globalLevelling;
import com.jami.fun.levelling.guildLevelling;
import com.jami.fun.wordCount.wordCount;
import com.jami.utilities.featureRequests.commandsFeatureRequests;
import com.jami.database.user.user;
import com.jami.fun.levelling.commandsLevelling;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class eventListeners {

  @SubscribeEvent
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    switch (event.getName()) {
      case "featurerequest":
        commandsFeatureRequests.newFeatureRequest(event);
        break;
      case "level":
        commandsLevelling c = new commandsLevelling(event);
        c.go();
        break;
    }
  }

  @SubscribeEvent
  public void onMessageReceived(MessageReceivedEvent event) {
    User u = event.getAuthor();
    Guild g = event.getGuild();
    Channel c = event.getChannel();

    if (u.isBot()) {
      return;
    }

    List<String> userEnabledFeatures = new user(u.getIdLong()).getSettings().getEnabledFeatures();

    // Levelling
    if (userEnabledFeatures.contains("levelling")) {
      globalLevelling.incrementExp(event.getAuthor().getIdLong());

      guildLevelling gl = new guildLevelling(event);
      if (gl.incrementExp()) {
        gl.announceLevelUp();
      }
      gl.commit();
    }

    // WordCount
    if (userEnabledFeatures.contains("words")) {
      wordCount.incrementWords(event.getMessage().getContentRaw(), g.getIdLong(), c.getIdLong());
    }
  }

}
