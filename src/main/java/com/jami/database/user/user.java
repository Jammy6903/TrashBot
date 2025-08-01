package com.jami.database.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

import static com.jami.App.mongoClient;
import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

/**
 * user
 */
public class user {
  private static final MongoDatabase db = mongoClient.getDatabase("TRASHBOT");
  private static final MongoCollection<Document> users = db.getCollection("users");

  private long userId;
  private long userExp;
  private int userLevel;
  private long userLastMessage;
  private userSettings userSettings;

  public user(long id) {
    Document u = users.find(eq("_id", id)).first();
    if (u != null) {
      this.userId = id;
      this.userExp = u.getLong("userExp");
      this.userLevel = u.getInteger("userLevel");
      this.userLastMessage = u.getLong("userLastMessage");
      this.userSettings = new userSettings(u.get("settings", Document.class));
    } else {
      this.userId = id;
      this.userExp = 0;
      this.userLevel = 0;
      this.userLastMessage = 0;
      this.userSettings = new userSettings(null);
    }
  }

  public void setExp(long exp) {
    this.userExp = exp;
  }

  public long getExp() {
    return userExp;
  }

  public void setLevel(int level) {
    this.userLevel = level;
  }

  public int getLevel() {
    return userLevel;
  }

  public void setLastMessage(long time) {
    this.userLastMessage = time;
  }

  public long getLastMessage() {
    return userLastMessage;
  }

  public userSettings getSettings() {
    return userSettings;
  }

  public void setSettings(userSettings s) {
    this.userSettings = s;
  }

  /**
   * @brief Commits user object to database, if the user exists, updates existing
   *        entry, otherwise create new entry.
   */

  public void commit() {
    Document u = new Document("$set", new Document()
        .append("_id", userId)
        .append("userExp", userExp)
        .append("userLevel", userLevel)
        .append("userLastMessage", userLastMessage)
        .append("settings", userSettings.toDocument()));
    try {
      UpdateOptions opts = new UpdateOptions().upsert(true);
      users.updateOne(eq("_id", userId), u, opts);
    } catch (Exception e) {
      System.out.println("[ERROR] MongoDB Write Error - " + e);
    }
  }
}
