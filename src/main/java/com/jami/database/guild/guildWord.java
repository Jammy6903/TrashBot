package com.jami.database.guild;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.jami.database.getOrDefault;

public class guildWord {
  private ObjectId id;
  private String word;
  private long count;

  public guildWord(Document entry, String word) {
    if (entry == null) {
      entry = new Document();
    }
    this.id = getOrDefault.ObjectId(entry, "_id");
    this.word = word;
    this.count = getOrDefault.Long(entry, "count", 0L);
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

  public Document toDocument() {
    return new Document("$set", new Document()
        .append("_id", id)
        .append("word", word)
        .append("count", count));
  }
}
