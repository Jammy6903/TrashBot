package com.jami.Database.repositories;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.jami.App;
import com.jami.Database.Fun.WordRecord;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

public class WordRepo {
  private static final MongoDatabase database = App.getMongoClient().getDatabase("TRASHBOT");
  private static final MongoCollection<Document> words = database.getCollection("WORDS");

  public static WordRecord getById(String id) {
    Document doc = words.find(eq("_id", new ObjectId(id))).first();
    if (doc == null) {
      return null;
    }
    return new WordRecord(doc);
  }

  public static WordRecord getByWord(String word) {
    Document doc = words.find(eq("word", word)).first();
    if (doc == null) {
      return new WordRecord(word);
    }
    return new WordRecord(doc);
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
        results.add(new WordRecord(doc));
      }
      return results;
    }
  }

  public static void incrementWord(String word) {
    words.updateOne(eq("word", word), Updates.inc("count", 1));
  }

  public static void save(WordRecord record) {
    words.updateOne(eq("_id", record.getId()), new Document("$set", record.toDocument()),
        new UpdateOptions().upsert(true));
  }

}
