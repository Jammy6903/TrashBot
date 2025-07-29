package com.jami.database;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;

import static com.jami.App.mongoClient;

public class guild {
  private static final MongoDatabase db = mongoClient.getDatabase("TRASHBOT");
  private static final MongoCollection<Document> guilds = db.getCollection("guilds");

  private long guildId;
  private settings guildSettings;
  private Document guildUsers;
  private List<Document> guildUsersUpdates;

  private guild(long id) {
    this.guildId = id;
    this.guildSettings = new settings();
    this.guildUsers = new Document();
    this.guildUsersUpdates = new ArrayList<Document>();
  }

  public static class user {
    private long userId;
    private long userExp;
    private int userLevel;

    public user(long id) {
      this.userId = id;
      this.userExp = 0;
      this.userLevel = 0;
    }

    public void setUser(Document user) {
      this.userId = user.getLong("_id");
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
      Document u = new Document()
          .append("_id", userId)
          .append("exp", userExp)
          .append("level", userLevel);
      setUser(u);
    }
  }

  public class settings {
    boolean exampleSetting;

    public settings() {
      this.exampleSetting = true;
    }

    public void setSettings(Document settings) {
      this.exampleSetting = settings.getBoolean("example");
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
    Document entry = guildUsers.get(eq("_id", id), Document.class);
    if (entry != null) {
      u.setUser(entry);
    }
    return u;
  }

  public void setUser(Document u) {
    guildUsersUpdates.add(u);
  }

  public void commit() {

  }

}
