package com.jami.Interaction.Fun.Levelling;

import java.awt.Color;

import com.jami.App;
import com.jami.Database.Config.ConfigRecord;
import com.jami.Database.Guild.GuildRecord;
import com.jami.Database.Guild.GuildUserRecord;
import com.jami.Database.Guild.guildSettings.GuildSettings;
import com.jami.Database.Guild.guildSettings.LevellingSettings.LevellingSettings;
import com.jami.Database.repositories.GuildRepo;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CommandsLevelling {
  private static ConfigRecord config = App.getGlobalConfig();

  public static void levellingCommands(SlashCommandInteractionEvent event) {
    long guildId = event.getGuild().getIdLong();

    GuildRecord guild = GuildRepo.getById(guildId);
    GuildSettings guildSettings = guild.getSettings();
    LevellingSettings levellingSettings = guildSettings.getLevellingSettings();

    switch (event.getSubcommandName()) {
      case "card":
        long userId = event.getUser().getIdLong();
        if (event.getOption("user") != null) {
          userId = event.getOption("user").getAsUser().getIdLong();
        }
        GuildUserRecord guildUser = GuildRepo.getUserById(guildId, userId);
        event.replyEmbeds(generateLevelCard(event.getGuild().getMemberById(userId).getUser(),
            guildUser.getLevel(), guildUser.getExp(), Levelling.getRequiredExp(guildUser.getLevel(),
                levellingSettings.getLevelBase(), levellingSettings.getLevelGrowth()))
            .build()).queue();
        break;
      case "leaderboard":
        break;
    }
  }

  private static EmbedBuilder generateLevelCard(User u, int level, long exp, long requiredExp) {
    return new EmbedBuilder()
        .setColor(Color.decode("#" + config.getBotColors().getPrimary()))
        .setTitle(u.getName() + "'s Levelling Card")
        .setDescription("Level: " + level + " | Exp: " + exp + "/" + requiredExp);
  }

}
