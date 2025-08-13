package com.jami.fun.wordCount;

import java.util.List;

import com.jami.database.fun.word;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class commandsWordCount {
  public static void wordCommands(SlashCommandInteractionEvent event) {
    switch (event.getSubcommandName()) {
      case "info":
        break;
      case "leaderboard":
        int page = 0;
        String order = "count";
        boolean reverse = false;
        if (event.getOption("page") != null) {
          page = event.getOption("page").getAsInt();
        }
        if (event.getOption("order") != null) {
          order = event.getOption("order").getAsString();
        }
        if (event.getOption("reverse") != null) {
          reverse = event.getOption("reverse").getAsBoolean();
        }
        EmbedBuilder embed = generateLeaderboard(word.getWords(10, page, order, reverse));
        event.replyEmbeds(embed.build()).queue();
        break;
    }
  }

  private static EmbedBuilder generateLeaderboard(List<word> words) {
    String content = "";

    for (int i = 0; i < words.size(); i++) {
      word w = words.get(i);
      content += String.format("**%d.** %s - %d\n", i + 1, w.getWord(), w.getCount());
    }

    return new EmbedBuilder()
        .setTitle("**Global Word Leaderboard**")
        .setDescription(content);
  }
}
