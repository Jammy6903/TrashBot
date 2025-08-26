package com.jami.Database.repositories;

import static com.mongodb.client.model.Updates.*;

import static com.mongodb.client.model.Filters.eq;

import com.jami.App;
import com.jami.Database.User.UserRecord;
import com.jami.Database.User.UserSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

public class UserRepo {
  private static final MongoDatabase database = App.getMongoClient().getDatabase("TRASHBOT");
  private static final MongoCollection<UserRecord> users = database.getCollection("USERS", UserRecord.class);

  private static final UpdateOptions UPSERT = new UpdateOptions().upsert(true);
  private static final ReplaceOptions REPLACE_UPSERT = new ReplaceOptions().upsert(true);
  private static final FindOneAndUpdateOptions RETURN_UPSERT = new FindOneAndUpdateOptions().upsert(true)
      .returnDocument(ReturnDocument.AFTER);

  public static void ensureIndexes() {
    users.createIndex(Indexes.ascending("userExp"));
    users.createIndex(Indexes.descending("userExp"));
  }

  public static UserRecord getById(long userId) {
    return users.findOneAndUpdate(eq("_id", userId),
        combine(
            setOnInsert("userExp", 0L),
            setOnInsert("userLevel", 0),
            setOnInsert("userLastMessage", System.currentTimeMillis()),
            setOnInsert("userSettings", new UserSettings()),
            setOnInsert("dateCreated", System.currentTimeMillis())),
        RETURN_UPSERT);
  }

  public static void updateLastMessage(long id) {
    users.updateOne(eq("_id", id), set("userLastMessage", System.currentTimeMillis()));
  }

  public static void incrementExp(long id, int exp) {
    users.updateOne(eq("_id", id),
        combine(
            inc("userExp", exp),
            setOnInsert("userLevel", 0),
            setOnInsert("userLastMessage", System.currentTimeMillis()),
            setOnInsert("userSettings", new UserSettings()),
            setOnInsert("dateCreated", System.currentTimeMillis())),
        UPSERT);
  }

  public static void incrementLevel(long id) {
    users.updateOne(eq("_id", id), Updates.inc("userLevel", 1));
  }

  public static void save(UserRecord record) {
    users.replaceOne(eq("_id", record.getUserId()), record, REPLACE_UPSERT);
  }
}
