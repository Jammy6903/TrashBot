package com.jami.Interaction.Utilities.Info;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CommandsInfo {
  public static void infoCommands(SlashCommandInteractionEvent event) {
    Info i = new Info(event);
    switch (event.getSubcommandName()) {
      case "bot":
        i.bot();
        break;
      case "guild":
        i.guild();
        break;
      case "member":
        Member m = event.getMember();
        if (event.getOption("member") != null) {
          m = event.getOption("member").getAsMember();
        }
        i.member(m);
        break;
      case "user":
        User u = event.getUser();
        if (event.getOption("user-id") != null) {
          u = event.getJDA().getUserById(event.getOption("user-id").getAsInt());
        }
        i.user(u);
        break;
    }
  }
}
