package com.jami.Interaction.Utilities.WelcomeMessage;

import com.jami.Database.Guild.GuildRecord;
import com.jami.Database.Guild.guildSettings.WelcomeMessageSettings.WelcomeMessageSettings;
import com.jami.Database.infrastructure.mongo.MongoGuildRepo;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class WelcomeMessage {
  public static void sendWelcome(GuildMemberJoinEvent event) {
    /*
     * Variables :
     * $userName$ = plain user name
     * $globalNickName$ = global nick name
     * $userMention$ = mentions user
     * $userId$ = users ID
     * $guildName$ = guild name
     * $guildId$ = guild Id
     * $channel:id$ = channel of id mention
     * $memberCount$ = guild member count
     */

    Member m = event.getMember();
    User u = event.getUser();
    Guild g = event.getGuild();

    GuildRecord guildRecord = MongoGuildRepo.getById(g.getIdLong());
    WelcomeMessageSettings wms = guildRecord.getSettings().getWelcomeMessageSettings();

    String welcomeMessage = wms.getWelcomeMessage();
    welcomeMessage = welcomeMessage.replaceAll("$userName$", u.getName());
    welcomeMessage = welcomeMessage.replaceAll("$globalNickName$", u.getGlobalName());
    welcomeMessage = welcomeMessage.replaceAll("$userMention$", m.getAsMention());
    welcomeMessage = welcomeMessage.replaceAll("$userId$", m.getId());
    welcomeMessage = welcomeMessage.replaceAll("$guildName$", g.getName());
    welcomeMessage = welcomeMessage.replaceAll("$guildId$", g.getId());

    while (welcomeMessage.contains("$channel")) {
      int firstIndex = welcomeMessage.indexOf("$channel") + 9;
      int secondIndex = welcomeMessage.indexOf("$", firstIndex);

      String channelId = welcomeMessage.substring(firstIndex, secondIndex);
      TextChannel channel = g.getTextChannelById(Long.valueOf(channelId));

      String regex = "\\$channel:" + channelId + "\\$";
      if (channel != null) {
        welcomeMessage = welcomeMessage.replaceFirst(regex, channel.getAsMention());
      } else {
        welcomeMessage = welcomeMessage.replaceFirst(regex, "Unknown Text Channel");
      }
    }

  }
}
