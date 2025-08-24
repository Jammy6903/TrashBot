package com.jami.Database.Fun;

import org.bson.Document;

import org.bson.types.ObjectId;

public class WordRecord {

  private ObjectId recordId;
  private String word;
  private long count;
  private long firstUse;

  public WordRecord(Document doc) {
    this.recordId = doc.getObjectId("_id");
    this.word = doc.getString("word");
    this.count = doc.getLong("count");
    this.firstUse = doc.getLong("firstUse");
  }

  public WordRecord(String word) {
    this.recordId = new ObjectId();
    this.word = word;
    this.count = 0;
    this.firstUse = System.currentTimeMillis();
  }

  public void newRecordId() {
    this.recordId = new ObjectId();
  }

  public String getId() {
    return recordId.toString();
  }

  public void setWord(String word) {
    this.word = word;
  }

  public String getWord() {
    return word;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public long getCount() {
    return count;
  }

  public void setFirstUser(long time) {
    this.firstUse = time;
  }

  public long getFirstUse() {
    return firstUse;
  }

  public Document toDocument() {
    return new Document()
        .append("_id", recordId)
        .append("word", word)
        .append("count", count)
        .append("firstUse", firstUse);
  }
}
