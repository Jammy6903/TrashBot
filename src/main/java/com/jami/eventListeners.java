package com.jami;

import org.jetbrains.annotations.NotNull;

import com.jami.fun.levelling.globalLevelling;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class eventListeners extends ListenerAdapter {

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

  }

  @Override
  public void onMessageReceived(@NotNull MessageReceivedEvent event) {
    if (event.getAuthor().isBot()) {
      return;
    }
    globalLevelling.incrementExp(event.getAuthor().getIdLong());
  }

  @Override
  public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

  }

}
