package com.jami.Database.Guild;

import org.bson.Document;
import org.bson.types.ObjectId;

public class GuildWordRecord {
  private ObjectId recordId;
  private long guildId;
  private String word;
  private long count;
  private long firstUse;

  public GuildWordRecord(Document doc) {
    this.recordId = doc.getObjectId("_id");
    this.guildId = doc.getLong("guildId");
    this.word = doc.getString("word");
    this.count = doc.getLong("count");
    this.firstUse = doc.getLong("firstUse");
  }

  public GuildWordRecord(long guildId, String word) {
    this.recordId = new ObjectId();
    this.guildId = guildId;
    this.word = word;
    this.count = 0;
    this.firstUse = System.currentTimeMillis();
  }

  public String getRecordId() {
    return recordId.toString();
  }

  public long getGuildId() {
    return guildId;
  }

  public String getWord() {
    return word;
  }

  public void setCount(long c) {
    this.count = c;
  }

  public long getCount() {
    return count;
  }

  public long getFirstUse() {
    return firstUse;
  }

  public Document toDocument() {
    return new Document()
        .append("_id", recordId)
        .append("guildId", guildId)
        .append("word", word)
        .append("count", count)
        .append("firstUse", firstUse);
  }
}
