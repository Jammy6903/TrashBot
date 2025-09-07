package com.jami.bot;

import org.slf4j.Logger;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jami.App;
import com.jami.Interaction.Utilities.GuildLogging.Logging;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Bot {

  private Logger LOGGER = Log.getLogger();

  private static ShardManager shardManager;
  private static final EventWaiter EVENT_WAITER = new EventWaiter();

  public Bot(String TOKEN) {
    startShardManager(TOKEN);
  }

  public void startShardManager(String Token) {
    LOGGER.info("[INFO] Attempting to connect to Discord...");
    while (shardManager == null) {
      try {
        shardManager = DefaultShardManagerBuilder.createDefault(Token)
        .setStatus(OnlineStatus.ONLINE)
        .enableIntents(
          GatewayIntent.GUILD_MEMBERS,
          GatewayIntent.GUILD_MODERATION,
          GatewayIntent.GUILD_WEBHOOKS,
          GatewayIntent.GUILD_INVITES,
          GatewayIntent.GUILD_VOICE_STATES,
          GatewayIntent.GUILD_MESSAGES,
          GatewayIntent.GUILD_MESSAGE_REACTIONS,
          GatewayIntent.GUILD_MESSAGE_TYPING,
          GatewayIntent.DIRECT_MESSAGES,
          GatewayIntent.DIRECT_MESSAGE_REACTIONS,
          GatewayIntent.DIRECT_MESSAGE_TYPING,
          GatewayIntent.MESSAGE_CONTENT,
          GatewayIntent.AUTO_MODERATION_EXECUTION,
          GatewayIntent.GUILD_MESSAGE_POLLS)
        .setEventManagerProvider(id -> new AnnotatedEventManager())
        .addEventListeners(EVENT_WAITER, new EventListeners(), new Logging())
        .setMemberCachePolicy(MemberCachePolicy.ALL)
        .setBulkDeleteSplittingEnabled(true)
        .setShardsTotal(-1)
        .build();
      } catch (Exception e) {
        LOGGER.error("[ERROR] Couldn't start shard manager, reattempting in 10 seconds", e);
        App.sleepQuietly(10000);
      }
    }
    LOGGER.info("[INFO] Connected to Discord");
  }

  public static ShardManager getShardManager() {
    return shardManager;
  }

  public static EventWaiter getEventWaiter() {
    return EVENT_WAITER;
  }

  public void SetBotStatus(String status) {
    if (!status.isEmpty()) {
      shardManager.getShards().forEach(jda -> jda.getPresence().setActivity(Activity.customStatus(status)));
    }
  }

  public int totalGuildCount() {
    return shardManager.getShards().stream()
        .mapToInt(jda -> jda.getGuilds().size())
        .sum();
  }

}
