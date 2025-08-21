package com.jami.Interaction.Fun.WordCount;

import java.util.ArrayList;

import com.jami.Database.Guild.GuildRecord;
import com.jami.Database.Guild.GuildWordRecord;

public class GuildWordCount {
  public static void incrementWordCount(ArrayList<String> words, GuildRecord g) {
    for (String w : words) {
      GuildWordRecord ww = g.getWord(w);
      ww.setCount(ww.getCount() + 1);
    }

    g.commit();
  }
}
