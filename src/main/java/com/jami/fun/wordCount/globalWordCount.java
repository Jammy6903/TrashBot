package com.jami.fun.wordCount;

import java.util.ArrayList;

import com.jami.database.word;

public class globalWordCount {

  public static void incrementWordCount(ArrayList<String> words) {
    for (String w : words) {
      word ww = new word(w);
      ww.setCount(ww.getCount() + 1);
      ww.commit();
    }
  }
}
