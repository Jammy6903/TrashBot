package com.jami.fun.levelling;

import java.awt.Color;

import com.jami.App;
import com.jami.database.guild.guild;
import com.jami.database.guild.guildUser;
import com.jami.database.guild.guildSettings.guildSettings;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class commandsLevelling {
  public static void levellingCommands(SlashCommandInteractionEvent event) {
    guild g = new guild(event.getGuild().getIdLong());
    guildSettings gs = g.getSettings();
    switch (event.getSubcommandName()) {
      case "card":
        long userId = event.getUser().getIdLong();
        if (event.getOption("user") != null) {
          userId = event.getOption("user").getAsUser().getIdLong();
        }
        guildUser gu = g.getUser(userId);
        event.replyEmbeds(generateLevelCard(event.getGuild().getMemberById(userId).getUser(),
            gu.getLevel(), gu.getExp(), gu.getRequiredExp(gs.getLevelBase(), gs.getLevelGrowth())).build()).queue();
        break;
      case "leaderboard":
        break;
    }
  }

  private static EmbedBuilder generateLevelCard(User u, int level, long exp, long requiredExp) {
    return new EmbedBuilder()
        .setColor(Color.decode("#" + App.CONFIG.getBotColor()))
        .setTitle(u.getName() + "'s Levelling Card")
        .setDescription("Level: " + level + " | Exp: " + exp + "/" + requiredExp);
  }

}
