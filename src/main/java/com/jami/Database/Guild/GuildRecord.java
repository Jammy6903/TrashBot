package com.jami.Database.Guild;

import org.bson.Document;

import com.jami.App;
import com.jami.Database.GetOrDefault;
import com.jami.Database.Guild.guildSettings.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;

public class GuildRecord {
  private static final MongoDatabase db = App.getMongoClient().getDatabase("TRASHBOT");
  private static final MongoCollection<Document> guilds = db.getCollection("guilds");
  private static final MongoDatabase guildUsersDb = App.getMongoClient().getDatabase("TRASHBOT_GUILD_USERS");
  private static final MongoDatabase guildWordsDb = App.getMongoClient().getDatabase("TRASHBOT_GUILD_WORDS");
  private static final MongoDatabase guildPunishmentsDb = App.getMongoClient()
      .getDatabase("TRASHBOT_GUILD_PUNISHMENTS");

  private long guildId;
  private GuildSettings guildSettings;
  private long firstJoined;

  private ArrayList<GuildUserRecord> userList;
  private ArrayList<GuildWordRecord> wordList;
  private ArrayList<GuildPunishmentRecord> punishmentList;
  private MongoCollection<Document> guildUsers;
  private MongoCollection<Document> guildWords;
  private MongoCollection<Document> guildPunishments;

  public GuildRecord(long id) {
    Document entry = guilds.find(eq("_id", id)).first();
    if (entry == null) {
      entry = new Document();
    }
    this.guildId = GetOrDefault.Long(entry, "_id", id);
    this.guildSettings = GetOrDefault.guildSettings(entry, "settings");
    this.firstJoined = GetOrDefault.Long(entry, "firstJoined", System.currentTimeMillis());

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

  public void setSettings(GuildSettings s) {
    this.guildSettings = s;
  }

  public GuildSettings getSettings() {
    return guildSettings;
  }

  public long getFirstJoined() {
    return firstJoined;
  }

  public GuildUserRecord getUser(long id) {
    Document user = guildUsers.find(eq("_id", id)).first();
    GuildUserRecord gu = new GuildUserRecord(user, id);
    this.userList.add(gu);
    return gu;
  }

  public GuildWordRecord getWord(String w) {
    Document word = guildWords.find(eq("word", w)).first();
    GuildWordRecord gw = new GuildWordRecord(word, w);
    this.wordList.add(gw);
    return gw;
  }

  public List<GuildWordRecord> getWords(int amount, int page, String order, boolean reverseOrder) {
    int sortDirection = reverseOrder ? 1 : -1;
    int skip = page * amount;

    try (MongoCursor<Document> cursor = guildWords.find()
        .sort(new Document(order, sortDirection))
        .skip(skip)
        .limit(amount)
        .iterator()) {

      List<GuildWordRecord> results = new ArrayList<>();
      while (cursor.hasNext()) {
        Document doc = cursor.next();
        long firstUse = GetOrDefault.Long(doc, "firstUse", 0L);
        results.add(
            new GuildWordRecord(doc.getObjectId("_id"), doc.getString("word"), doc.getLong("count"),
                firstUse));
      }
      return results;
    }
  }

  public GuildPunishmentRecord getPunishment(String punishmentId) {
    Document punishment = guildPunishments.find(eq("_id", punishmentId)).first();
    if (punishment == null) {
      return null;
    }
    GuildPunishmentRecord gp = new GuildPunishmentRecord(punishment);
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
      for (GuildUserRecord gu : userList) {
        guildUsers.updateOne(eq("_id", gu.getId()), gu.toDocument(), opts);
      }
      for (GuildWordRecord gw : wordList) {
        guildWords.updateOne(eq("_id", gw.getId()), gw.toDocument(), opts);
      }
    } catch (Exception e) {
      System.out.println("[ERROR] MongoDB Write Error - " + e);
    }
  }
}
