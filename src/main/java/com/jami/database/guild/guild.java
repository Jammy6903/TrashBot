package com.jami.database.guild;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;

import static com.jami.App.mongoClient;

public class guild {
  private static final MongoDatabase db = mongoClient.getDatabase("TRASHBOT");
  private static final MongoCollection<Document> guilds = db.getCollection("guilds");
  private static final MongoDatabase guildUsersDb = mongoClient.getDatabase("TRASHBOT_GUILD_USERS");
  private static final MongoDatabase guildWordsDb = mongoClient.getDatabase("TRASHBOT_GUILD_WORDS");
  private static final MongoDatabase guildPunishmentsDb = mongoClient.getDatabase("TRASHBOT_GUILD_PUNISHMENTS");

  private long guildId;
  private guildSettings guildSettings;
  private ArrayList<guildUser> userList;
  private ArrayList<guildWord> wordList;
  private ArrayList<guildPunishment> punishmentList;
  private MongoCollection<Document> guildUsers;
  private MongoCollection<Document> guildWords;
  private MongoCollection<Document> guildPunishments;

  public guild(long id) {
    Document entry = guilds.find(eq("_id", id)).first();
    this.guildId = id;
    if (entry != null) {
      this.guildSettings = new guildSettings(entry.get("settings", Document.class));
    } else {
      this.guildSettings = new guildSettings(null);
    }
    this.userList = new ArrayList<>();
    this.wordList = new ArrayList<>();

    guildUsersDb.createCollection(String.valueOf(id));
    this.guildUsers = guildUsersDb.getCollection(String.valueOf(id));
    guildWordsDb.createCollection(String.valueOf(id));
    this.guildWords = guildWordsDb.getCollection(String.valueOf(id));
    guildPunishmentsDb.createCollection(String.valueOf(id));
    this.guildPunishments = guildWordsDb.getCollection(String.valueOf(id));
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
    Document user = guildUsers.find(eq("_id", id)).first();
    guildUser gu = new guildUser(null, id);
    if (user != null) {
      gu = new guildUser(user, id);
    }
    this.userList.add(gu);
    return gu;
  }

  public guildWord getWord(String w) {
    Document word = guildWords.find(eq("word", w)).first();
    guildWord gw = new guildWord(null, w);
    if (word != null) {
      gw = new guildWord(word, w);
    }
    this.wordList.add(gw);
    return gw;
  }

  public guildPunishment getPunishment(String punishmentId) {
    Document punishment = guildPunishments.find(eq("_id", punishmentId)).first();
    if (punishment == null) {
      return null;
    }
    guildPunishment gp = new guildPunishment(punishment);
    this.punishmentList.add(gp);
    return gp;
  }

  public void commit() {
    Document guild = new Document("$set", new Document()
        .append("_id", guildId)
        .append("settings", guildSettings.toDocument()));
    try {
      UpdateOptions opts = new UpdateOptions().upsert(true);
      guilds.updateOne(eq("_id", guildId), guild, opts);
      for (guildUser gu : userList) {
        guildUsers.updateOne(eq("_id", gu.getId()), gu.toDocument(), opts);
      }
      for (guildWord gw : wordList) {
        guildWords.updateOne(eq("_id", gw.getId()), gw.toDocument(), opts);
      }
    } catch (Exception e) {
      System.out.println("[ERROR] MongoDB Write Error - " + e);
    }
  }
}
