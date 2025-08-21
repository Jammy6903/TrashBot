package com.jami.JDA;

import com.jami.App;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.ShardManager;

public class JDATools {
  public static void SetBotStatus(String status) {
    ShardManager sm = App.getShardManager();
    if (!status.isEmpty()) {
      sm.getShards().forEach(jda -> jda.getPresence().setActivity(Activity.customStatus(status)));
    }
  }

  public static int totalGuildCount() {
    ShardManager sm = App.getShardManager();
    return sm.getShards().stream()
        .mapToInt(jda -> jda.getGuilds().size())
        .sum();
  }
}
