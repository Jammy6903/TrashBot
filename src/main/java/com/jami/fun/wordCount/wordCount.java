package com.jami.fun.wordCount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.jami.database.guild.guild;

public class wordCount {
  public static void incrementWords(String message, guild g) {
    ArrayList<String> words = getWords(message);
    globalWordCount.incrementWordCount(words);
    guildWordCount.incrementWordCount(words, g);
  }

  public static ArrayList<String> getWords(String message) {
    message = message.toLowerCase();
    message = message.replaceAll("\\p{Punct}", "");
    ArrayList<String> words = new ArrayList<>(Arrays.asList(message.split(" ")));
    words.removeAll(Collections.singleton(""));
    return words;
  }
}
