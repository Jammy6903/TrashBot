package com.jami.Database.Guild.guildSettings.countingSettings;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class CountingSettings {

  @BsonProperty("channelId")
  private long channelId;

  @BsonProperty("allowMultiPost")
  private boolean allowMultiPost;

  @BsonExtraElements
  private Document legacyValues;

  public CountingSettings() {
  }

  // channelId

  public void setChannelId(long id) {
    this.channelId = id;
  }

  public long getChannelId() {
    return channelId;
  }

  // allowMultiPost

  public void setAllowMultiPost(boolean b) {
    this.allowMultiPost = b;
  }

  public boolean getALlowMultiPost() {
    return allowMultiPost;
  }
}
