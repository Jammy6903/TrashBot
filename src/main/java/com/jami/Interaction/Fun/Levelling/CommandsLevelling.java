package com.jami.Interaction.Fun.Levelling;

import java.awt.Color;

import com.jami.App;
import com.jami.Database.Guild.GuildRecord;
import com.jami.Database.Guild.GuildUserRecord;
import com.jami.Database.Guild.guildSettings.GuildSettings;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CommandsLevelling {
  public static void levellingCommands(SlashCommandInteractionEvent event) {
    GuildRecord g = new GuildRecord(event.getGuild().getIdLong());
    GuildSettings gs = g.getSettings();
    switch (event.getSubcommandName()) {
      case "card":
        long userId = event.getUser().getIdLong();
        if (event.getOption("user") != null) {
          userId = event.getOption("user").getAsUser().getIdLong();
        }
        GuildUserRecord gu = g.getUser(userId);
        event.replyEmbeds(generateLevelCard(event.getGuild().getMemberById(userId).getUser(),
            gu.getLevel(), gu.getExp(), gu.getRequiredExp(gs.getLevelBase(), gs.getLevelGrowth())).build()).queue();
        break;
      case "leaderboard":
        break;
    }
  }

  private static EmbedBuilder generateLevelCard(User u, int level, long exp, long requiredExp) {
    return new EmbedBuilder()
        .setColor(Color.decode("#" + App.getGlobalConfig().getBotColor()))
        .setTitle(u.getName() + "'s Levelling Card")
        .setDescription("Level: " + level + " | Exp: " + exp + "/" + requiredExp);
  }

}
