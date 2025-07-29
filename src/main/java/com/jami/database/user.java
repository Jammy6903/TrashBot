package com.jami.database;

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
  private settings userSettings;

  private user(long id) {
    this.userId = id;
    this.userExp = 0;
    this.userLevel = 1;
    this.userSettings = new settings();
  }

  public static class settings {
    boolean exampleSetting;

    public settings() {
      this.exampleSetting = true;
    }

    public void setSettings(Document settings) {
      this.setExample(settings.getBoolean("example"));
    }

    public void setExample(boolean ex) {
      this.exampleSetting = ex;
    }

    public boolean getExample() {
      return exampleSetting;
    }

    public Document commit() {
      Document s = new Document()
          .append("example", exampleSetting);
      return s;
    }
  }

  /**
   * @brief Gets user Document from MongoDB Collection, if user does not exist,
   *        just uses default values, otherwise uses values from database.
   */

  public static user getUser(long id) {
    Document entry = users.find(eq("_id", id)).first();
    user u = new user(id);
    if (entry != null) {
      u.setExp(entry.getLong("exp"));
      u.setLevel(entry.getInteger("level"));
      settings s = u.getSettings();
      s.setSettings(entry.get("settings", Document.class));
    }
    return u;
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

  public settings getSettings() {
    return userSettings;
  }

  /**
   * @brief Commits user object to database, if the user exists, updates existing
   *        entry, otherwise create new entry.
   */

  public void commit() {
    Document u = new Document("$set", new Document()
        .append("_id", userId)
        .append("exp", userExp)
        .append("level", userLevel)
        .append("settings", userSettings.commit()));
    try {
      UpdateOptions opts = new UpdateOptions().upsert(true);
      users.updateOne(eq("_id", userId), u, opts);
    } catch (Exception e) {
      System.out.println("[ERROR] MongoDB Write Error - " + e);
    }
  }
}
