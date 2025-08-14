package com.jami.database.guild;

import org.bson.Document;

import com.jami.database.getOrDefault;
import com.jami.database.guild.guildSettings.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;

import static com.jami.App.mongoClient;

public class guild {
  private static final MongoDatabase db = mongoClient.getDatabase("TRASHBOT");
  private static final MongoCollection<Document> guilds = db.getCollection("guilds");
  private static final MongoDatabase guildUsersDb = mongoClient.getDatabase("TRASHBOT_GUILD_USERS");
  private static final MongoDatabase guildWordsDb = mongoClient.getDatabase("TRASHBOT_GUILD_WORDS");
  private static final MongoDatabase guildPunishmentsDb = mongoClient.getDatabase("TRASHBOT_GUILD_PUNISHMENTS");

  private long guildId;
  private guildSettings guildSettings;
  private long firstJoined;

  private ArrayList<guildUser> userList;
  private ArrayList<guildWord> wordList;
  private ArrayList<guildPunishment> punishmentList;
  private MongoCollection<Document> guildUsers;
  private MongoCollection<Document> guildWords;
  private MongoCollection<Document> guildPunishments;

  public guild(long id) {
    Document entry = guilds.find(eq("_id", id)).first();
    if (entry == null) {
      entry = new Document();
    }
    this.guildId = getOrDefault.Long(entry, "_id", id);
    this.guildSettings = getOrDefault.guildSettings(entry, "settings");
    this.firstJoined = getOrDefault.Long(entry, "firstJoined", System.currentTimeMillis());

    this.userList = new ArrayList<>();
    this.wordList = new ArrayList<>();

    this.guildUsers = getCollection(guildUsersDb, id);
    this.guildWords = getCollection(guildWordsDb, id);
    this.guildPunishments = getCollection(guildPunishmentsDb, id);
  }

  private MongoCollection<Document> getCollection(MongoDatabase db, long id) {
    String collectionName = String.valueOf(id);
    db.createCollection(collectionName);
    return db.getCollection(collectionName);
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

  public long getFirstJoined() {
    return firstJoined;
  }

  public guildUser getUser(long id) {
    Document user = guildUsers.find(eq("_id", id)).first();
    guildUser gu = new guildUser(user, id);
    this.userList.add(gu);
    return gu;
  }

  public guildWord getWord(String w) {
    Document word = guildWords.find(eq("word", w)).first();
    guildWord gw = new guildWord(word, w);
    this.wordList.add(gw);
    return gw;
  }

  public List<guildWord> getWords(int amount, int page, String order, boolean reverseOrder) {
    int sortDirection = reverseOrder ? 1 : -1;
    int skip = page * amount;

    try (MongoCursor<Document> cursor = guildWords.find()
        .sort(new Document(order, sortDirection))
        .skip(skip)
        .limit(amount)
        .iterator()) {

      List<guildWord> results = new ArrayList<>();
      while (cursor.hasNext()) {
        Document doc = cursor.next();
        long firstUse = getOrDefault.Long(doc, "firstUse", 0L);
        results.add(
            new guildWord(doc.getObjectId("_id"), doc.getString("word"), doc.getLong("count"),
                firstUse));
      }
      return results;
    }
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
