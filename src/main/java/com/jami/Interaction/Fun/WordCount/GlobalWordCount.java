package com.jami.Interaction.Fun.WordCount;

import java.util.ArrayList;

import com.jami.Database.Fun.WordRecord;

public class GlobalWordCount {

  public static void incrementWordCount(ArrayList<String> words) {
    for (String w : words) {
      WordRecord ww = new WordRecord(w);
      ww.setCount(ww.getCount() + 1);
      ww.commit();
    }
  }
}
