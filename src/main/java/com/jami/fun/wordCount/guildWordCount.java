package com.jami.fun.wordCount;

import java.util.ArrayList;

import com.jami.database.guild.guild;
import com.jami.database.guild.guildWord;

public class guildWordCount {
  public static void incrementWordCount(ArrayList<String> words, guild g) {
    for (String w : words) {
      guildWord ww = g.getWord(w);
      ww.setCount(ww.getCount() + 1);
    }

    g.commit();
  }
}
