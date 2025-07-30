package com.jami.fun.wordCount;

import java.util.ArrayList;

import com.jami.database.word;

public class globalWordCount {

  public static void incrementWordCount(String message) {
    ArrayList<String> words = messageWordSplit.getWords(message);

    for (String w : words) {
      word ww = word.getWord(w);
      ww.setCount(ww.getCount() + 1);
      ww.commit();
    }
  }
}
