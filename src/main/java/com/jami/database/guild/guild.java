package com.jami.database.guild;

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
  private guildSettings guildSettings;
  private Document guildUsers;

  public guild(long id) {
    Document entry = guilds.find(eq("_id", id)).first();
    this.guildId = id;
    if (entry != null) {
      this.guildSettings = new guildSettings(entry.get("settings", Document.class));
      this.guildUsers = entry.get("users", Document.class);
    } else {
      this.guildSettings = new guildSettings(null);
      this.guildUsers = new Document();
    }
  }

  public long getGuildId() {
    return guildId;
  }

  public void setSettings(guildSettings s) {
    this.guildSettings = s;
  }

  public guildSettings getSettings() {
    return guildSettings;
  }

  public void setUsers(Document u) {
    this.guildUsers = u;
  }

  public Document getUsers() {
    return guildUsers;
  }

  public guildUser getUser(long id) {
    Document us = guildUsers.get(String.valueOf(id), Document.class);
    return new guildUser(us, id);
  }

  public void ammendUser(long id, Document u) {
    guildUsers.append(String.valueOf(id), u);
  }

  public void commit() {
    Document g = new Document("$set", new Document()
        .append("_id", guildId)
        .append("settings", guildSettings.toDocument())
        .append("users", guildUsers));
    try {
      UpdateOptions opts = new UpdateOptions().upsert(true);
      guilds.updateOne(eq("_id", guildId), g, opts);
    } catch (Exception e) {
      System.out.println("[ERROR] MongoDB Write Error - " + e);
    }
  }
}
