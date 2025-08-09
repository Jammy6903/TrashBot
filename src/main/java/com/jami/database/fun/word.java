package com.jami.database.fun;

import org.bson.Document;

import static com.jami.App.mongoClient;
import static com.mongodb.client.model.Filters.eq;

import java.util.List;

import static com.mongodb.client.model.Filters.*;

import org.bson.types.ObjectId;

import com.jami.database.getOrDefault;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

public class word {
  private static final MongoDatabase db = mongoClient.getDatabase("TRASHBOT");
  private static final MongoCollection<Document> words = db.getCollection("words");

  private ObjectId id;
  private String word;
  private long count;

  public word(String word) {
    Document wordEntry = words.find(eq("word", word)).first();
    this.id = getOrDefault.ObjectId(wordEntry, "_id");
    this.word = word;
    this.count = getOrDefault.Long(wordEntry, "count", 0L);
  }

  public String getId() {
    return id.toString();
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

  public static List<word> getWords(long amount, int index, String order, boolean reverseOrder) {
    if (amount > words.countDocuments()) {
      amount = words.countDocuments();
    }
    return null;
  }

  public void commit() {
    Document w = new Document("$set", new Document()
        .append("_id", id)
        .append("word", word)
        .append("count", count));
    try {
      UpdateOptions opts = new UpdateOptions().upsert(true);
      words.updateOne(eq("_id", id), w, opts);
    } catch (Exception e) {
      System.out.println("[ERROR] MongoDB Write Error - " + e);
    }
  }
}
