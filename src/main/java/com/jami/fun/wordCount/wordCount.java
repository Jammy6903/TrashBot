package com.jami.fun.wordCount;

import java.util.ArrayList;
import java.util.Arrays;

// To-Do maybe, check dictionary for word validity.

public class wordCount {
  public static void incrementWords(String message, long guildId, long channelId) {
    ArrayList<String> words = getWords(message);
    globalWordCount.incrementWordCount(words);
    guildWordCount.incrementWordCount(words, guildId, channelId);
  }

  public static ArrayList<String> getWords(String message) {
    message = message.toLowerCase();
    message = message.replaceAll("\\p{Punct}", "");
    ArrayList<String> words = new ArrayList<>(Arrays.asList(message.split(" ")));
    return words;
  }
}
