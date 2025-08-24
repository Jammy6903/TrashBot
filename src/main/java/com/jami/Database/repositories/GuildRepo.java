package com.jami.Database.repositories;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.jami.App;
import com.jami.Database.Guild.GuildPunishmentRecord;
import com.jami.Database.Guild.GuildRecord;
import com.jami.Database.Guild.GuildUserRecord;
import com.jami.Database.Guild.GuildWordRecord;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

public class GuildRepo {
  private static final MongoDatabase database = App.getMongoClient().getDatabase("TRASHBOT");
  private static final MongoCollection<Document> guilds = database.getCollection("GUILDS");
  private static final MongoCollection<Document> guildUsers = database.getCollection("GUILD_USERS");
  private static final MongoCollection<Document> guildWords = database.getCollection("GUILD_WORDS");
  private static final MongoCollection<Document> guildPunishments = database.getCollection("GUILD_PUNISHMENTS");

  private static final UpdateOptions upsert = new UpdateOptions().upsert(true);

  /*
   * GUILD
   */

  public static GuildRecord getById(long id) {
    Document doc = guilds.find(eq("_id", id)).first();
    if (doc == null) {
      return new GuildRecord(id);
    }
    return new GuildRecord(doc);
  }

  public static void save(GuildRecord record) {
    long id = record.getGuildId();
    guilds.updateOne(eq("_id", id), new Document("$set", record.toDocument()), upsert);
  }

  /*
   * GUILD USER
   */

  public static GuildUserRecord getUserById(long guildId, long userId) {
    Document doc = guildUsers.find(and(eq("guildId", guildId), eq("userId", userId))).first();
    if (doc == null) {
      return new GuildUserRecord(guildId, userId);
    }
    return new GuildUserRecord(doc);
  }

  public static void incrementExp(long guildId, long userId, int exp) {
    guildUsers.updateOne(and(eq("guildId", guildId), eq("userId", userId)), Updates.inc("userExp", exp));
  }

  public static void incrementLevel(long guildId, long userId) {
    guildUsers.updateOne(and(eq("guildId", guildId), eq("userId", userId)), Updates.inc("userLevel", 1));
  }

  public static void saveUser(GuildUserRecord record) {
    long guildId = record.getGuildId();
    long userId = record.getUserId();
    guildUsers.updateOne(and(eq("guildId", guildId), eq("userId", userId)), new Document("$set", record.toDocument()),
        upsert);
  }

  /*
   * GUILD WORDS
   */

  public static GuildWordRecord getGuildWordById(String id) {
    return new GuildWordRecord(guildWords.find(eq("_id", id)).first());
  }

  public static GuildWordRecord getGuildWordByWord(long guildId, String word) {
    Document doc = guildWords.find(and(eq("guildId", guildId), eq("word", word))).first();
    if (doc == null) {
      return new GuildWordRecord(guildId, word);
    }
    return new GuildWordRecord(doc);
  }

  public static List<GuildWordRecord> getWords(long guildId, int amount, int page, String order, boolean reverseOrder) {
    int sortDirection = reverseOrder ? 1 : -1;
    int skip = page * amount;

    try (MongoCursor<Document> cursor = guildWords.find(eq("guildId", guildId))
        .sort(new Document(order, sortDirection))
        .skip(skip)
        .limit(amount)
        .iterator()) {

      List<GuildWordRecord> results = new ArrayList<>();
      while (cursor.hasNext()) {
        Document doc = cursor.next();
        results.add(new GuildWordRecord(doc));
      }
      return results;
    }
  }

  public static void incrementWord(String word) {
    guildWords.updateOne(eq("word", word), Updates.inc("count", 1));
  }

  public static void saveWord(GuildWordRecord record) {
    guildWords.updateOne(eq("_id", record.getRecordId()), new Document("$set", record.toDocument()), upsert);
  }

  /*
   * GUILD PUNISHMENTS
   */

  public static GuildPunishmentRecord getPunishmentById(String id) {
    return new GuildPunishmentRecord(guildPunishments.find(eq("_id", id)).first());
  }

  public static List<GuildPunishmentRecord> getPunishmentsByUser(long guildId, long userId) {
    return null;
  }

  public static List<GuildPunishmentRecord> getPunishmentsByGuild(long guildId) {
    return null;
  }

  public static void savePunishment(GuildPunishmentRecord record) {
    guildPunishments.updateOne(eq("_id", record.getPunishmentId()), new Document("$set", record.toDocument()),
        new UpdateOptions().upsert(true));
  }

}
