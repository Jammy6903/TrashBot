package com.jami.Database.repositories;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.jami.App;
import com.jami.Database.Fun.WordRecord;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Sorts.*;

public class WordRepo {
  private static final MongoDatabase database = App.getMongoClient().getDatabase("TRASHBOT");
  private static final MongoCollection<WordRecord> words = database.getCollection("WORDS", WordRecord.class);

  private static final UpdateOptions UPSERT = new UpdateOptions().upsert(true);
  private static final ReplaceOptions REPLACE_UPSERT = new ReplaceOptions().upsert(true);
  private static final FindOneAndUpdateOptions RETURN_UPSERT = new FindOneAndUpdateOptions().upsert(true)
      .returnDocument(ReturnDocument.AFTER);

  public static void ensureIndexes() {
    words.createIndex(Indexes.ascending("word"), new IndexOptions().unique(true));
    words.createIndex(Indexes.descending("word"));
  }

  public static WordRecord getById(String id) {
    return words.find(eq("_id", new ObjectId(id))).first();
  }

  public static WordRecord getByWord(String word) {
    return words.findOneAndUpdate(eq("word", word),
        combine(setOnInsert("firstUse", System.currentTimeMillis()),
            setOnInsert("_id", new ObjectId()),
            setOnInsert("count", 0L)),
        RETURN_UPSERT);
  }

  public static List<WordRecord> getWords(int amount, int page, String order, boolean reverseOrder) {
    Set<String> allowed = Set.of("word", "count", "firstUse");
    String key = allowed.contains(order) ? order : "count";

    var sort = reverseOrder ? ascending(key) : descending(key);
    int skip = Math.max(0, page) * Math.max(1, amount);

    try (MongoCursor<WordRecord> cursor = words.find()
        .sort(sort)
        .skip(skip)
        .limit(amount)
        .iterator()) {
      List<WordRecord> results = new ArrayList<>();
      while (cursor.hasNext()) {
        results.add(cursor.next());
      }
      return results;
    }
  }

  public static void incrementWord(String word) {
    words.updateOne(eq("word", word),
        Updates.combine(Updates.inc("count", 1), Updates.setOnInsert("firstUse", System.currentTimeMillis())),
        UPSERT);
  }

  public static void save(WordRecord record) {
    words.replaceOne(eq("_id", record.getId()), record, REPLACE_UPSERT);
  }

}
