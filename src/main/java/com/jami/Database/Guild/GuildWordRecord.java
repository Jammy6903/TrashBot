package com.jami.Database.Guild;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.jami.Database.GetOrDefault;

public class GuildWordRecord {
  private ObjectId id;
  private String word;
  private long count;
  private long firstUse;

  public GuildWordRecord(Document entry, String word) {
    if (entry == null) {
      entry = new Document();
    }
    this.id = GetOrDefault.ObjectId(entry, "_id");
    this.word = word;
    this.count = GetOrDefault.Long(entry, "count", 0L);
    this.firstUse = GetOrDefault.Long(entry, "firstUse", System.currentTimeMillis());
  }

  public GuildWordRecord(ObjectId id, String word, long count, long firstUse) {
    this.id = id;
    this.word = word;
    this.count = count;
    this.firstUse = firstUse;
  }

  public ObjectId getId() {
    return id;
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
    return new Document("$set", new Document()
        .append("_id", id)
        .append("word", word)
        .append("count", count)
        .append("firstUse", firstUse));
  }
}
