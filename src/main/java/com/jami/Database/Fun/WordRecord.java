package com.jami.Database.Fun;

import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.jami.App;
import com.jami.Database.GetOrDefault;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

public class WordRecord {
  private static final MongoDatabase db = App.getMongoClient().getDatabase("TRASHBOT");
  private static final MongoCollection<Document> words = db.getCollection("words");

  private ObjectId id;
  private String word;
  private long count;
  private long firstUse;

  public WordRecord(String word) {
    Document wordEntry = words.find(eq("word", word)).first();
    if (wordEntry == null) {
      wordEntry = new Document();
    }
    this.id = GetOrDefault.ObjectId(wordEntry, "_id");
    this.word = word;
    this.count = GetOrDefault.Long(wordEntry, "count", 0L);
    this.firstUse = GetOrDefault.Long(wordEntry, "firstUse", System.currentTimeMillis());
  }

  private WordRecord(ObjectId id, String word, long count, long firstUse) {
    this.id = id;
    this.word = word;
    this.count = count;
    this.firstUse = firstUse;
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

  public long getFirstUse() {
    return firstUse;
  }

  public static List<WordRecord> getWords(int amount, int page, String order, boolean reverseOrder) {
    int sortDirection = reverseOrder ? 1 : -1;
    int skip = page * amount;

    try (MongoCursor<Document> cursor = words.find()
        .sort(new Document(order, sortDirection))
        .skip(skip)
        .limit(amount)
        .iterator()) {

      List<WordRecord> results = new ArrayList<>();
      while (cursor.hasNext()) {
        Document doc = cursor.next();
        long firstUse = GetOrDefault.Long(doc, "firstUse", 0L);
        results.add(
            new WordRecord(doc.getObjectId("_id"), doc.getString("word"), doc.getLong("count"), firstUse));
      }
      return results;
    }
  }

  public void commit() {
    Document w = new Document("$set", new Document()
        .append("_id", id)
        .append("word", word)
        .append("count", count)
        .append("firstUse", firstUse));
    try {
      UpdateOptions opts = new UpdateOptions().upsert(true);
      words.updateOne(eq("_id", id), w, opts);
    } catch (Exception e) {
      System.out.println("[ERROR] MongoDB Write Error - " + e);
    }
  }
}
