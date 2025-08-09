package com.jami.database.guild;

import org.bson.Document;
import org.bson.types.ObjectId;

public class guildWord {
  private ObjectId id;
  private String word;
  private long count;

  public guildWord(Document entry, String word) {
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
