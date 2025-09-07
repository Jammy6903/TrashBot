package com.jami.Interaction.Fun.WordCount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.jami.Database.infrastructure.mongo.MongoGuildRepo;
import com.jami.Database.infrastructure.mongo.MongoWordRepo;


public class WordCount {
  public static void incrementWords(String message, long guildId) {
    ArrayList<String> words = getWords(message);
    for (String word : words) {
      MongoWordRepo.incrementWord(word);
      MongoGuildRepo.incrementWord(guildId, word);
    }
  }

  public static ArrayList<String> getWords(String message) {
    message = message.toLowerCase();
    message = message.replaceAll("\\p{Punct}", "");
    ArrayList<String> words = new ArrayList<>(Arrays.asList(message.split(" ")));
    words.removeAll(Collections.singleton(""));
    return words;
  }
}
