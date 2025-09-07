package com.jami.Database.infrastructure.mongo;

import static com.mongodb.client.model.Updates.*;

import static com.mongodb.client.model.Filters.eq;

import com.jami.Database.User.UserRecord;
import com.jami.Database.User.UserSettings;
import com.jami.Database.repository.UserRepo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

public class MongoUserRepo implements UserRepo {
  private static final UpdateOptions UPSERT = new UpdateOptions().upsert(true);
  private static final ReplaceOptions REPLACE_UPSERT = new ReplaceOptions().upsert(true);
  private static final FindOneAndUpdateOptions RETURN_UPSERT = new FindOneAndUpdateOptions().upsert(true)
      .returnDocument(ReturnDocument.AFTER);

  private MongoCollection<UserRecord> users;

  public MongoUserRepo() {
    this.users = Mongo.getDatabase().getCollection("USERS", UserRecord.class);
  }

  @Override
  public UserRecord getById(long userId) {
    return users.findOneAndUpdate(eq("_id", userId),
        combine(
            setOnInsert("userExp", 0L),
            setOnInsert("userLevel", 0),
            setOnInsert("userLastMessage", System.currentTimeMillis()),
            setOnInsert("userSettings", new UserSettings()),
            setOnInsert("dateCreated", System.currentTimeMillis())),
        RETURN_UPSERT);
  }

  @Override
  public void updateLastMessage(long id) {
    users.updateOne(eq("_id", id), set("userLastMessage", System.currentTimeMillis()));
  }

  @Override
  public void incrementExp(long id, int exp) {
    users.updateOne(eq("_id", id),
        combine(
            inc("userExp", exp),
            setOnInsert("userLevel", 0),
            setOnInsert("userLastMessage", System.currentTimeMillis()),
            setOnInsert("userSettings", new UserSettings()),
            setOnInsert("dateCreated", System.currentTimeMillis())),
        UPSERT);
  }

  @Override
  public void incrementLevel(long id) {
    users.updateOne(eq("_id", id), Updates.inc("userLevel", 1));
  }

  @Override
  public void save(UserRecord record) {
    users.replaceOne(eq("_id", record.getUserId()), record, REPLACE_UPSERT);
  }
}
