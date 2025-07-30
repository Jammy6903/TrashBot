package com.jami.database;

import org.bson.Document;

import static com.jami.App.mongoClient;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

public class word {
  private static final MongoDatabase db = mongoClient.getDatabase("TRASHBOT");
  private static final MongoCollection<Document> words = db.getCollection("words");

  private ObjectId id;
  private String word;
  private long count;

  private word(String word) {
    this.id = new ObjectId();
    this.word = word;
    this.count = 0;
  }

  private void setId(ObjectId id) {
    this.id = id;
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

  public static word getWord(String wordd) {
    Document entry = words.find(eq("word", wordd)).first();
    word w = new word(wordd);
    if (entry != null) {
      w.setId(entry.getObjectId("_id"));
      w.setCount(entry.getLong("count"));
    }
    return w;
  }

  public static ArrayList<Document> getTopWords(int index) {
    ArrayList<Document> results = new ArrayList<>();
    words.find().sort(descending("count")).into(results);

    ArrayList<Document> wos = new ArrayList<>();
    int x = 0;
    for (int i = index; i < index + 10; i++) {
      wos.set(x, results.get(i));
    }

    return wos;
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
