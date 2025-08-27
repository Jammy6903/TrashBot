package com.jami.Database.Fun;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class WordRecord {

  @BsonId
  private ObjectId recordId;

  @BsonProperty("word")
  private String word;

  @BsonProperty("count")
  private long count;

  @BsonProperty("firstUse")
  private long firstUse;

  @BsonExtraElements
  private Document legacyValues;

  public WordRecord() {
  }

  // ID

  public ObjectId getId() {
    return recordId;
  }

  // Word

  public void setWord(String word) {
    this.word = word;
  }

  public String getWord() {
    return word;
  }

  // Count

  public void setCount(long count) {
    this.count = count;
  }

  public long getCount() {
    return count;
  }

  // FirstUse

  public long getFirstUse() {
    return firstUse;
  }

}
