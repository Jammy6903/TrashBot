package com.jami.utilities.guildLogging;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
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
import com.jami.database.guild.guildSettings.loggingChannels.LogType;

public class logging {

  private final Cache<Long, CachedMessage> messageCache = Caffeine.newBuilder()
      .expireAfterWrite(1, TimeUnit.DAYS)
      .build();

  public void sendLogEntry(LogType type, Guild g, EmbedBuilder content) {

  }

  @SubscribeEvent
  public void messageSent(MessageReceivedEvent event) {
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
    LogType type = LogType.MESSAGE_DELETED;
  }

  @SubscribeEvent
  public void messageEdit(MessageUpdateEvent event) {

  }
}
