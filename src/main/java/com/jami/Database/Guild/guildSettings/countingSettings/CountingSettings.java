package com.jami.Database.Guild.guildSettings.countingSettings;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class CountingSettings {

  @BsonProperty("channelId")
  private Long channelId;

  @BsonProperty("allowMultiPost")
  private boolean allowMultiPost = false;

  @BsonExtraElements
  private Document legacyValues;

  public CountingSettings() {
  }

  // channelId

  public void setChannelId(Long id) {
    this.channelId = id;
  }

  public Long getChannelId() {
    return channelId;
  }

  // allowMultiPost

  public void setAllowMultiPost(boolean b) {
    this.allowMultiPost = b;
  }

  public boolean isAllowMultiPost() {
    return allowMultiPost;
  }
}
