package com.jami.database;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

import static com.mongodb.client.model.Filters.*;

import static com.jami.App.mongoClient;

public class guild {
  private static final MongoDatabase db = mongoClient.getDatabase("TRASHBOT");
  private static final MongoCollection<Document> guilds = db.getCollection("guilds");

  private long guildId;
  private settings guildSettings;
  private Document guildUsers;

  private guild(long id) {
    this.guildId = id;
    this.guildSettings = new settings();
    this.guildUsers = new Document();
  }

  public class user {
    private long userId;
    private long userExp;
    private int userLevel;

    public user(long id) {
      this.userId = id;
      this.userExp = 0;
      this.userLevel = 0;
    }

    public void setUser(Document user, long id) {
      System.out.println(user);
      this.userId = id;
      this.userExp = user.getLong("exp");
      this.userLevel = user.getInteger("level");
    }

    public long getId() {
      return userId;
    }

    public void setId(long id) {
      this.userId = id;
    }

    public long getExp() {
      return this.userExp;
    }

    public void setExp(long exp) {
      this.userExp = exp;
    }

    public int getLevel() {
      return this.userLevel;
    }

    public void setLevel(int level) {
      this.userLevel = level;
    }

    public void commit() {
      Document u = new Document("$set", new Document("users." + userId, new Document()
          .append("_id", userId)
          .append("exp", userExp)
          .append("level", userLevel)));
      try {
        UpdateOptions opts = new UpdateOptions().upsert(true);
        guilds.updateOne(eq("_id", guildId), u, opts);
      } catch (Exception e) {
        System.out.println("[ERROR] MongoDB Write Error - " + e);
      }
    }
  }

  public class settings {
    int expIncrement;

    public settings() {
      this.expIncrement = 3;
    }

    public void setSettings(Document settings) {
      this.expIncrement = settings.getInteger("expIncrement");
    }

    public void setExpIncrement(int exp) {
      this.expIncrement = exp;
    }

    public int getExpIncrement() {
      return expIncrement;
    }

    public Document commit() {
      Document s = new Document()
          .append("expIncrement", expIncrement);
      return s;
    }
  }

  public static guild getGuild(long id) {
    Document entry = guilds.find(eq("_id", id)).first();
    guild g = new guild(id);
    if (entry != null) {
      settings s = g.getSettings();
      s.setSettings(entry.get("settings", Document.class));
      g.guildUsers = entry.get("users", Document.class);
    }
    return g;
  }

  public settings getSettings() {
    return guildSettings;
  }

  public user getUser(long id) {
    user u = new user(id);
    Document entry = guildUsers.get(exists(String.valueOf(id)), Document.class);
    System.out.println(guildUsers);
    System.out.println(entry);
    if (entry != null) {
      u.setUser(entry, id);
    }
    return u;
  }

  public void commit() {
    Document g = new Document("$set", new Document()
        .append("_id", guildId)
        .append("settings", guildSettings.commit()));
    try {
      UpdateOptions opts = new UpdateOptions().upsert(true);
      guilds.updateOne(eq("_id", guildId), g, opts);
    } catch (Exception e) {
      System.out.println("[ERROR] MongoDB Write Error - " + e);
    }
  }

}
