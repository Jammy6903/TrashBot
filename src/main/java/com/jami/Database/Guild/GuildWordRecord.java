package com.jami.Database.Guild;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class GuildWordRecord {

  @BsonId
  private ObjectId recordId;

  @BsonProperty("guildId")
  private long guildId;

  @BsonProperty("word")
  private String word;

  @BsonProperty("count")
  private long count;

  @BsonProperty("dateCreated")
  private long dateCreated;

  @BsonExtraElements
  private Document legacyValues;

  public GuildWordRecord() {
  }

  // RecordId

  public ObjectId getRecordId() {
    return recordId;
  }

  // GuildId

  public long getGuildId() {
    return guildId;
  }

  // WordId

  public String getWord() {
    return word;
  }

  // Count

  public void setCount(long c) {
    this.count = c;
  }

  public long getCount() {
    return count;
  }

  // DateCreated

  public long getDateCreated() {
    return dateCreated;
  }
}
