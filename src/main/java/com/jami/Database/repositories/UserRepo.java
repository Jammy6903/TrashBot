package com.jami.Database.repositories;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.jami.App;
import com.jami.Database.User.UserRecord;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

public class UserRepo {
  private static final MongoDatabase database = App.getMongoClient().getDatabase("TRASHBOT");
  private static final MongoCollection<Document> users = database.getCollection("USERS");

  public static UserRecord getById(long id) {
    Document doc = users.find(eq("_id", id)).first();
    if (doc == null) {
      return new UserRecord(id);
    }
    return new UserRecord(doc);
  }

  public static void incrementExp(long id, int exp) {
    users.updateOne(eq("_id", id), Updates.inc("userExp", exp));
  }

  public static void incrementLevel(long id) {
    users.updateOne(eq("_id", id), Updates.inc("userLevel", 1));
  }

  public static void save(UserRecord record) {
    users.updateOne(eq("_id", record.getUserId()), new Document("$set", record.toDocument()),
        new UpdateOptions().upsert(true));
  }
}
