package com.jami.fun;

import java.util.ArrayList;

import org.bson.Document;

import com.jami.database.word;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class commands {
  public class wordCountCommands {
    public static void leaderboard(SlashCommandInteractionEvent event) {
      int db;

      int index = 0;
      if (event.getOption("index") != null) {
        index = event.getOption("index").getAsInt();
      }
      // get top words
      ArrayList<Document> words = word.getTopWords(index);

      String message = "";

      for (int i = 0; i < 10; i++) {
        message += i + ". " + words.get(i).getString("word") + " - " + words.get(i).getInteger("count") + "\n";
      }

      event.reply(message).queue();

      // send to user
    }
  }
}
