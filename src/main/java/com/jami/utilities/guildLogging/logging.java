package com.jami.utilities.guildLogging;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.jami.App;
import com.jami.database.LogType;
import com.jami.database.guild.guild;
import com.jami.database.guild.guildSettings.guildSettings;
import com.jami.database.guild.guildSettings.loggingChannels.loggingChannel;

public class logging {

  private static final Cache<Long, CachedMessage> messageCache = Caffeine.newBuilder()
      .expireAfterWrite(1, TimeUnit.DAYS)
      .build();

  public void sendLogEntry(LogType type, Guild guild, MessageEmbed content) {
    guild g = new guild(guild.getIdLong());
    guildSettings gs = g.getSettings();
    List<loggingChannel> channels = gs.getLoggingChannelsByLogType(type);

    if (channels.size() == 0) {
      return;
    }

    for (loggingChannel channel : channels) {
      guild.getTextChannelById(channel.getLogChannel()).sendMessageEmbeds(content).queue();
    }
  }

  public static void cacheMessage(MessageReceivedEvent event) {
    if (event.getAuthor().isBot()) {
      return;
    }

    long id = event.getAuthor().getIdLong();
    String content = event.getMessage().getContentRaw();
    OffsetDateTime timestamp = event.getMessage().getTimeCreated();
    List<Attachment> attachments = event.getMessage().getAttachments();

    List<String> attachmentNames = new ArrayList<>();
    for (Attachment attachment : attachments) {
      attachmentNames.add(attachment.getFileName());
    }

    CachedMessage cache = new CachedMessage(id, content, timestamp, attachmentNames);
    messageCache.put(event.getMessageIdLong(), cache);
  }

  @SubscribeEvent
  public void messageDelete(MessageDeleteEvent event) {
    long messageId = event.getMessageIdLong();

    CachedMessage message = messageCache.getIfPresent(messageId);
    long userId = message.getAuthorId();
    Guild g = event.getGuild();
    User u = App.getShardManager().getUserById(userId);
    Member m = g.getMemberById(userId);

    EmbedBuilder embed = new EmbedBuilder()
        .setTitle(String.format("Message deleted in %s", event.getChannel().getAsMention()))
        .setAuthor(String.format("Unkown User - %d", userId))
        .setDescription(message.getContent())
        .setFooter(String.format("Message ID: %d • %s", messageId, "")); // Add timestamp

    if (u != null) {
      String avUrl = u.getAvatarUrl();
      String name = u.getName();
      if (m != null && m.getNickname() != null) {
        embed.setAuthor(String.format("%s (%s) - %d", name, m.getNickname(), userId), avUrl, avUrl);
      } else {
        embed.setAuthor(String.format("%s - %d", name, userId), avUrl, avUrl);
      }
    } else {
      embed.setAuthor(String.format("Unkown User - %d", userId));
    }

    sendLogEntry(LogType.MESSAGE_DELETED, g, embed.build());
  }

  @SubscribeEvent
  public void messageEdit(MessageUpdateEvent event) {

  }
}
