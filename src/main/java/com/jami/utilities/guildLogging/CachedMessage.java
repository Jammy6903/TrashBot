package com.jami.utilities.guildLogging;

import java.time.OffsetDateTime;
import java.util.List;

public class CachedMessage {
  private final long authorId;
  private final String content;
  private final OffsetDateTime timestamp;
  private final List<String> attachments;

  public CachedMessage(long authorId, String content, OffsetDateTime timestamp, List<String> attachments) {
    this.authorId = authorId;
    this.content = content;
    this.timestamp = timestamp;
    this.attachments = attachments;
  }

  public long getAuthorId() {
    return authorId;
  }

  public String getContent() {
    return content;
  }

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public List<String> getAttachments() {
    return attachments;
  }
}
