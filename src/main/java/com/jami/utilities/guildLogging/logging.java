package com.jami.utilities.guildLogging;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

  private void getUserAvatar(EmbedBuilder embed, User u, Member m) {
    if (u != null) {
      String avUrl = u.getAvatarUrl();
      String name = u.getName();
      if (m != null && m.getNickname() != null) {
        embed.setAuthor(String.format("%s (%s) - %d", name, m.getNickname(), u.getIdLong()), avUrl, avUrl);
      } else {
        embed.setAuthor(String.format("%s - %d", name, u.getIdLong(), avUrl, avUrl));
      }
    }
  }

  @SubscribeEvent
  public void messageDelete(MessageDeleteEvent event) {
    long messageId = event.getMessageIdLong();

    CachedMessage message = messageCache.getIfPresent(messageId);
    if (message == null) {
      return;
    }
    long userId = message.getAuthorId();
    Guild g = event.getGuild();
    User u = App.getShardManager().getUserById(userId);
    Member m = g.getMemberById(userId);

    EmbedBuilder embed = new EmbedBuilder()
        .setTitle(String.format("Message deleted in %s", event.getChannel().getAsMention()))
        .setAuthor(String.format("Unkown User - %d", userId))
        .setDescription(message.getContent())
        .setFooter(String.format("Message ID: %d • %s", messageId, "")); // Add timestamp

    if (message.getAttachments().size() > 0) {
      String attachments = "";
      for (String attachment : message.getAttachments()) {
        attachments += String.format("%s, ", attachment);
      }
      attachments = attachments.substring(0, attachments.length() - 2);
      embed.addField("Attachments", attachments, false);
    }

    getUserAvatar(embed, u, m);

    sendLogEntry(LogType.MESSAGE_DELETED, g, embed.build());
  }

  @SubscribeEvent
  public void messageEdit(MessageUpdateEvent event) {
    if (event.getAuthor().isBot()) {
      return;
    }

    long messageId = event.getMessageIdLong();
    System.out.println("test");

    CachedMessage message = messageCache.getIfPresent(messageId);
    String newMessage = event.getMessage().getContentRaw();

    messageCache.put(messageId, new CachedMessage(message, newMessage));

    long userId = event.getAuthor().getIdLong();
    Guild g = event.getGuild();
    User u = App.getShardManager().getUserById(userId);
    Member m = g.getMemberById(userId);

    EmbedBuilder embed = new EmbedBuilder()
        .setTitle(String.format("Message edited in %s", event.getChannel().getAsMention()))
        .setAuthor(String.format("Unknown User - %d", userId))
        .setDescription(
            String.format("**Original Message:**\n%s\n\n**Updated Message:**\n%s\n\n[Jump to message](%s)",
                message.getContent(), newMessage, event.getJumpUrl()))
        .setFooter(String.format("Message ID: %d • %s", messageId, ""));

    getUserAvatar(embed, u, m);

    sendLogEntry(LogType.MESSAGE_EDITED, g, embed.build());
  }

  @SubscribeEvent
  public void bulkMessageDelete(MessageBulkDeleteEvent event) {
    List<Long> messageIds = event.getMessageIds().stream().map(Long::parseLong).collect(Collectors.toList());
    List<CachedMessage> messages = new ArrayList<>();

    for (Long id : messageIds) {
      messages.add(messageCache.getIfPresent(id));
    }

    String file = "";

    for (CachedMessage m : messages) {
      String username = "Unkown User";
      long userId = m.getAuthorId();
      User u = App.getShardManager().getUserById(userId);
      if (u != null) {
        username = u.getName();
      }
      file += String.format("[%s] (%s) %s: %s\n", m.getTimestamp().toString(), userId, username, m.getContent());
    }

    ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getBytes(StandardCharsets.UTF_8));

    guild g = new guild(event.getGuild().getIdLong());
    guildSettings gs = g.getSettings();
    List<loggingChannel> channels = gs.getLoggingChannelsByLogType(LogType.MESSAGE_DELETED);

    if (channels.size() == 0) {
      return;
    }

    for (loggingChannel channel : channels) {
      event.getGuild().getTextChannelById(channel.getLogChannel())
          .sendMessage(String.format("Bulk messages deleted in %s", event.getChannel().getAsMention()))
          .addFiles(FileUpload.fromData(inputStream, "messages.txt")).queue();
    }
  }

  @SubscribeEvent
  public void memberJoin(GuildMemberJoinEvent event) {
    User u = event.getUser();
    EmbedBuilder embed = new EmbedBuilder()
        .setTitle(String.format("📥 Member Joined %s", event.getGuild().getName()))
        .setDescription(String.format("%s - %s (%d)", u.getAsMention(), u.getName(), u.getIdLong()));
    sendLogEntry(LogType.MEMBER_JOIN_LEAVE, event.getGuild(), embed.build());
  }

  @SubscribeEvent
  public void memberLeaveOrKick(GuildMemberRemoveEvent event) {
    User u = event.getUser();
    EmbedBuilder embed = new EmbedBuilder()
        .setTitle(String.format("📤 Member Left %s", event.getGuild().getName()))
        .setDescription(String.format("%s - %s (%d)", u.getAsMention(), u.getName(), u.getIdLong()));
    sendLogEntry(LogType.MEMBER_JOIN_LEAVE, event.getGuild(), embed.build());
  }
}
