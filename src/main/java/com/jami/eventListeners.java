package com.jami;

import org.jetbrains.annotations.NotNull;

import com.jami.fun.levelling.globalLevelling;
import com.jami.fun.levelling.guildLevelling;
import com.jami.fun.wordCount.globalWordCount;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class eventListeners extends ListenerAdapter {

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    event.deferReply().queue();
    switch (event.getName()) {
    }
  }

  @Override
  public void onMessageReceived(@NotNull MessageReceivedEvent event) {
    if (event.getAuthor().isBot()) {
      return;
    }

    // Levelling
    globalLevelling.incrementExp(event.getAuthor().getIdLong());
    guildLevelling.incrementExp(event.getGuild().getIdLong(), event.getAuthor().getIdLong());

    // WordCount
    globalWordCount.incrementWordCount(event.getMessage().getContentRaw());
  }

  @Override
  public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

  }

}
