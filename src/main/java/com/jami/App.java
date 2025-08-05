package com.jami;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class App {

  private static JDA jda;
  private static final EventWaiter eventWaiter = new EventWaiter();
  private static final File config = new File("src/main/resources/config.properties");
  private static Properties Config = new Properties();
  public static MongoClient mongoClient;

  public static void main(String[] args) {
    try {
      Config.load(new FileInputStream(config));
      mongoClient = MongoClients.create(Config.getProperty("DATABASE_URI"));
      if (args.length == 1) {
        new App().start(args[0]);
      } else {
        new App().start(Config.getProperty("TOKEN"));
      }
    } catch (Exception e) {
      System.out.println("Bot startup failed: ");
    }
  }

  private void start(String Token) throws Exception {
    jda = JDABuilder.createDefault(Token)
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
        .addEventListeners(eventWaiter, new eventListeners())
        .build();

    if (Config.getProperty("STATUS") != null) {
      jda.getPresence().setActivity(Activity.customStatus(Config.getProperty("STATUS")));
    }

    getCommands(jda);
  }

  public static EventWaiter getWaiter() {
    return eventWaiter;
  }

  private static void getCommands(JDA jda) {

    // WordCount Commands

    OptionData wordCountChoices = new OptionData(OptionType.STRING, "where", "Which database to look in", false)
        .addChoice("global", "Every server")
        .addChoice("guild", "Just this guild")
        .addChoice("user", "just you");

    SubcommandData wordCountLeaderboard = new SubcommandData("leaderboard", "See the leaderboard of words used")
        .addOption(OptionType.INTEGER, "index", "where to start the leaderboard", false)
        .addOptions(wordCountChoices);

    // Levelling Commands

    OptionData globalOrGuild = new OptionData(OptionType.STRING, "where",
        "Global level info or guild level info (defaults to guild)", false)
        .addChoice("global", "Across all guilds")
        .addChoice("guild", "Only in this guild");

    SubcommandData levelScore = new SubcommandData("score", "See your current level and experience")
        .addOption(OptionType.USER, "user", "Pick a user to see their level and experience", false)
        .addOptions(globalOrGuild);

    jda.updateCommands().addCommands(
        Commands.slash("wordcount", "Commands relating to word counts")
            .addSubcommands(wordCountLeaderboard),
        Commands.slash("level", "Commands relating to levelling")
            .addSubcommands(levelScore))
        .queue();
  }
}
