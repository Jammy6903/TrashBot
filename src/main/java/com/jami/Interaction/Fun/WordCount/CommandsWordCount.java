package com.jami.Interaction.Fun.WordCount;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.jami.App;
import com.jami.Database.Guild.GuildRecord;
import com.jami.Database.Guild.GuildWordRecord;
import com.jami.JDA.Wiktionary;
import com.jami.Database.Fun.WordRecord;

import de.tudarmstadt.ukp.jwktl.JWKTL;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEdition;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEntry;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryPage;
import de.tudarmstadt.ukp.jwktl.api.IWiktionarySense;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CommandsWordCount {
  public static void wordCommands(SlashCommandInteractionEvent event) {
    switch (event.getSubcommandName()) {
      case "info":
        infoCommand(event);
        break;
      case "leaderboard":
        leaderboardCommand(event);
        break;
    }
  }

  private static void infoCommand(SlashCommandInteractionEvent event) {
    String word = event.getOption("word").getAsString().toLowerCase().replaceAll("\\p{Punct}", "");

    if (Arrays.asList(word.split(" ")).size() > 1) {
      event.reply("Please only enter one word").setEphemeral(true).queue();
      return;
    }

    boolean global = false;
    OptionMapping optGlobal = event.getOption("global");
    if (optGlobal != null) {
      global = optGlobal.getAsBoolean();
    }

    String definition = "**Definitions:**\n";

    try {
      IWiktionaryEdition wkt = JWKTL.openEdition(Wiktionary.getWiktionary());
      try {
        IWiktionaryPage page = wkt.getPageForWord(word);
        IWiktionaryEntry entry = page.getEntry(0);
        for (IWiktionarySense sense : entry.getSenses()) {
          definition += "- " + sense.getGloss().getPlainText() + "\n";
        }
      } catch (Exception e) {
        definition = "No Definitions :(";
      }
      wkt.close();
    } catch (Exception e) {
      System.out.println(e);
      event.reply("Dictionary parsing in progress, this command wont work for the time being.").queue();
      return;
    }

    EmbedBuilder embed;

    if (global) {
      embed = globalWordInfo(new WordRecord(word), definition);
    } else {
      GuildRecord g = new GuildRecord(event.getGuild().getIdLong());
      String guildName = event.getGuild().getName();
      embed = guildWordInfo(g, guildName, g.getWord(word), definition);
    }

    event.replyEmbeds(embed.build()).queue();
  }

  private static void leaderboardCommand(SlashCommandInteractionEvent event) {
    int page = 0;
    String order = "count";
    boolean reverse = false;
    boolean global = false;

    OptionMapping optPage = event.getOption("page");
    OptionMapping optOrder = event.getOption("order");
    OptionMapping optReverse = event.getOption("reverse");
    OptionMapping optGlobal = event.getOption("global");

    if (optPage != null) {
      page = optPage.getAsInt();
    }
    if (optOrder != null) {
      order = optOrder.getAsString();
    }
    if (optOrder != null) {
      reverse = optReverse.getAsBoolean();
    }
    if (optGlobal != null) {
      global = optGlobal.getAsBoolean();
    }

    EmbedBuilder embed;

    if (global) {
      embed = generateGlobalLeaderboard(WordRecord.getWords(10, page, order, reverse));
    } else {
      GuildRecord g = new GuildRecord(event.getGuild().getIdLong());
      String guildName = event.getGuild().getName();
      embed = generateGuildLeaderboard(g, guildName, g.getWords(10, page, order, reverse));
    }

    event.replyEmbeds(embed.build()).queue();
  }

  private static EmbedBuilder globalWordInfo(WordRecord word, String definition) {
    Date firstUse = new Date(word.getFirstUse());
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");

    return new EmbedBuilder()
        .setColor(Color.getColor(App.getGlobalConfig().getBotColor()))
        .setTitle("**\"" + word.getWord() + "\" Stats**")
        .setDescription(definition)
        .addField("Uses", String.valueOf(word.getCount()), true)
        .addField("First Used", df.format(firstUse), true)
        .setFooter("Data gathered from Aug 2025 - " + word.getId());
  }

  private static EmbedBuilder guildWordInfo(GuildRecord g, String guildName, GuildWordRecord word, String definition) {
    Date firstUse = new Date(word.getFirstUse());
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");

    Date firstJoined = new Date(g.getFirstJoined());
    SimpleDateFormat df2 = new SimpleDateFormat("MMM yyyy");

    return new EmbedBuilder()
        .setColor(Color.getColor(App.getGlobalConfig().getBotColor()))
        .setTitle("**\"" + word.getWord() + "\" Stats in " + guildName + "**")
        .setDescription(definition)
        .addField("Uses", String.valueOf(word.getCount()), true)
        .addField("First Used", df.format(firstUse), true)
        .setFooter("Data gathered from " + df2.format(firstJoined) + " - " + word.getId());
  }

  private static EmbedBuilder generateGlobalLeaderboard(List<WordRecord> words) {
    String content = "";

    for (int i = 0; i < words.size(); i++) {
      WordRecord w = words.get(i);
      Date firstUse = new Date(w.getFirstUse());
      SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
      content += String.format("**%d.** %s - Used **%d** times - First used %s\n", i + 1, w.getWord(), w.getCount(),
          df.format(firstUse));
    }

    return new EmbedBuilder()
        .setColor(Color.getColor(App.getGlobalConfig().getBotColor()))
        .setTitle("**Global Word Leaderboard**")
        .setDescription(content)
        .setFooter("Data gathered from Aug 2025");
  }

  private static EmbedBuilder generateGuildLeaderboard(GuildRecord g, String guildName, List<GuildWordRecord> words) {
    String content = "";

    for (int i = 0; i < words.size(); i++) {
      GuildWordRecord w = words.get(i);
      Date date = new Date(w.getFirstUse());
      SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
      content += String.format("**%d.** %s - Used **%d** times - First used %s\n", i + 1, w.getWord(), w.getCount(),
          df.format(date));
    }

    Date firstJoined = new Date(g.getFirstJoined());
    SimpleDateFormat df = new SimpleDateFormat("MMM yyyy");

    return new EmbedBuilder()
        .setColor(Color.getColor(App.getGlobalConfig().getBotColor()))
        .setTitle("**" + guildName + " Word Leaderboard**")
        .setDescription(content)
        .setFooter("Data gathered from " + df.format(firstJoined));
  }
}
