package com.jami.fun.wordCount;

import java.util.ArrayList;
import java.util.Arrays;

// To-Do maybe, check dictionary for word validity.

public class messageWordSplit {
  public static ArrayList<String> getWords(String message) {
    message = message.toLowerCase();
    message = message.replaceAll("\\p{Punct}", "");
    ArrayList<String> words = new ArrayList<>(Arrays.asList(message.split(" ")));
    return words;
  }
}
