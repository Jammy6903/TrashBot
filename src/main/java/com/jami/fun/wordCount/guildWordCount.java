package com.jami.fun.wordCount;

import java.util.ArrayList;

import com.jami.database.guild.guild;
import com.jami.database.guild.guildSettings.*;
import com.jami.database.guild.guildWord;

public class guildWordCount {
  public static void incrementWordCount(ArrayList<String> words, long guildId, long channelId) {
    guild g = new guild(guildId);
    guildSettings s = g.getSettings();

    if (!s.getEnabledFeatures().contains("words")) {
      return;
    }

    if (s.getWordsDisabledChannels() == null) {
    } else if (s.getWordsDisabledChannels().contains(channelId)) {
      return;
    }

    for (String w : words) {
      guildWord ww = g.getWord(w);
      ww.setCount(ww.getCount() + 1);
    }

    g.commit();
  }
}
