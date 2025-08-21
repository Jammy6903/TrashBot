package com.jami.Interaction.Fun.WordCount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.jami.Database.Guild.GuildRecord;

public class WordCount {
  public static void incrementWords(String message, GuildRecord g) {
    ArrayList<String> words = getWords(message);
    GlobalWordCount.incrementWordCount(words);
    GuildWordCount.incrementWordCount(words, g);
  }

  public static ArrayList<String> getWords(String message) {
    message = message.toLowerCase();
    message = message.replaceAll("\\p{Punct}", "");
    ArrayList<String> words = new ArrayList<>(Arrays.asList(message.split(" ")));
    words.removeAll(Collections.singleton(""));
    return words;
  }
}
