package com.jami.fun.levelling;

import com.jami.database.guild.guild;
import com.jami.database.guild.guildUser;
import com.jami.database.user.user;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class commandsLevelling {

  private SlashCommandInteractionEvent event;
  private String subCommand;
  private User userOption;
  private String whereOption;

  public commandsLevelling(SlashCommandInteractionEvent e) {
    this.event = e;
    this.subCommand = e.getSubcommandName();
    if (e.getOption("user") != null) {
      userOption = e.getOption("user").getAsUser();
    } else {
      userOption = e.getUser();
    }
    if (e.getOption("where") != null) {
      whereOption = e.getOption("where").getAsString();
    }
  }

  public void go() {
    switch (subCommand) {
      case "score":
        if (whereOption == "global") {
          scoreGlobal();
        }
        scoreCommand();
        break;
    }
  }

  public void scoreCommand() {
    guild g = new guild(event.getGuild().getIdLong());
    guildUser u = g.getUser(userOption.getIdLong());
    long exp = u.getExp();
    long level = u.getLevel();
    event.reply("Level: " + level + " | Exp: " + exp);
  }

  private void scoreGlobal() {
    user u = new user(userOption.getIdLong());
    long exp = u.getExp();
    long level = u.getLevel();
    event.reply("Level: " + level + " | Exp: " + exp);
  }

}
