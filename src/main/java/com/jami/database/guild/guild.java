package com.jami.database.guild;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.addToSet;

import java.util.ArrayList;
import java.util.List;

import static com.jami.App.mongoClient;

public class guild {
  private static final MongoDatabase db = mongoClient.getDatabase("TRASHBOT");
  private static final MongoCollection<Document> guilds = db.getCollection("guilds");

  private long guildId;
  private guildSettings guildSettings;
  private ArrayList<guildUser> userList;

  public guild(long id) {
    Document entry = guilds.find(eq("_id", id)).first();
    this.guildId = id;
    if (entry != null) {
      this.guildSettings = new guildSettings(entry.get("settings", Document.class));
    } else {
      this.guildSettings = new guildSettings(null);
    }
    this.userList = new ArrayList<>();
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

  public guildUser getUser(long id) {
    List<Document> users = (List<Document>) guilds.find(eq("_id", guildId)).first().get("users");
    System.out.println(users);

    guildUser gu = new guildUser(users, id);
    return gu;
  }

  // public guildUser getUser(long id) {
  // Document us = guildUsers.get(String.valueOf(id), Document.class);
  // return new guildUser(us, id);
  // }

  // public void ammendUser(long id, Document u) {
  // ammendedUsers.append(String.valueOf(id), u);
  // }

  public void commit() {
    Document guild = new Document("$set", new Document()
        .append("_id", guildId)
        .append("settings", guildSettings.toDocument()));
    try {
      UpdateOptions opts = new UpdateOptions().upsert(true);
      guilds.updateOne(eq("_id", guildId), guild, opts);
      for (guildUser gu : userList) {
        Bson update = addToSet("users", gu.toDocument());
        guilds.updateOne(eq("_id", guildId), update, opts);
      }
    } catch (Exception e) {
      System.out.println("[ERROR] MongoDB Write Error - " + e);
    }
  }
}
