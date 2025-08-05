package com.jami.database.guild;

import org.bson.types.ObjectId;
import org.bson.Document;

public class guildWord {
  private ObjectId id;
  private String word;
  private long count;

  public guildWord(org.bson.Document entry, String word) {
    this.id = new ObjectId();
    this.word = word;
    this.count = 0;
    if (entry != null) {
      this.id = entry.getObjectId("_id");
      this.count = entry.getLong("count");
    }
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
