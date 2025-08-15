package com.jami.utilities.guildAdmin;

import java.util.List;

import org.bson.Document;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class commandsSettings {
  public static void guildSettingsCommand(SlashCommandInteractionEvent event) {
    settings s = new settings(event.getGuild().getIdLong());
    switch (event.getSubcommandName()) {
      case "add-levelling-role":
        s.addLevellingRole(event.getOption("role").getAsRole().getIdLong(), event.getOption("level").getAsInt());
        event.reply("Levelling role added").queue();
        break;
      case "remove-levelling-role":
        if (s.removeLevellingRole(event.getOption("id").getAsString())) {
          event.reply("Levelling role removed").queue();
        } else {
          event.reply("ERROR: Does that entry exist?").queue();
        }
        break;
      case "list-levelling-roles":
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Levelling Roles:");
        String message = "";
        List<Document> lrs = s.listLevellingRoles();
        for (Document lr : lrs) {
          String id = lr.getObjectId("_id").toString();
          String level = lr.getInteger("levelRequirement").toString();
          Role role = event.getGuild().getRoleById(lr.getLong("roleId"));
          message += "Level: " + level + " | Role: " + role.getAsMention() + " | ID: " + id + "\n";
        }
        embed.setDescription(message);
        event.replyEmbeds(embed.build()).queue();
        break;
      case "list-words-disabled-channels":
        EmbedBuilder embed2 = new EmbedBuilder();
        embed2.setTitle("Words Disabled Channels:");
        String message2 = "";
        for (long id : s.getWordsDisabledChannels()) {
          message2 += event.getJDA().getChannelById(TextChannel.class, id).getName() + " (" + String.valueOf(id)
              + ")\n";
        }
        embed2.setDescription(message2);
        event.replyEmbeds(embed2.build()).queue();
        break;
      case "add-words-disabled-channel":
        s.addWordsDisabledChannel(event.getOption("channel").getAsChannel().getIdLong());
        event.reply("Channel added to disabled list");
        break;
      case "remove-words-disabled-channel":
        s.removeWordsDisabledChannel(event.getOption("channel").getAsChannel().getIdLong());
        event.reply("Channel removed from disabled list");
        break;
    }
  }
}
