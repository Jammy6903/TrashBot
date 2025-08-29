package com.jami.Database.repositories;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Updates.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.jami.App;
import com.jami.Database.Guild.GuildPunishmentRecord;
import com.jami.Database.Guild.GuildRecord;
import com.jami.Database.Guild.guildSettings.*;
import com.jami.Database.Guild.GuildUserRecord;
import com.jami.Database.Guild.GuildWordRecord;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Indexes;

public class GuildRepo {
  private static final MongoDatabase database = App.getDatabase();
  private static final MongoCollection<GuildRecord> guilds = database.getCollection("GUILDS", GuildRecord.class);
  private static final MongoCollection<GuildUserRecord> guildUsers = database.getCollection("GUILD_USERS",
      GuildUserRecord.class);
  private static final MongoCollection<GuildWordRecord> guildWords = database.getCollection("GUILD_WORDS",
      GuildWordRecord.class);
  private static final MongoCollection<GuildPunishmentRecord> guildPunishments = database
      .getCollection("GUILD_PUNISHMENTS", GuildPunishmentRecord.class);

  private static final UpdateOptions UPSERT = new UpdateOptions().upsert(true);
  private static final ReplaceOptions REPLACE_UPSERT = new ReplaceOptions().upsert(true);
  private static final FindOneAndUpdateOptions RETURN_UPSERT = new FindOneAndUpdateOptions().upsert(true)
      .returnDocument(ReturnDocument.AFTER);

  private static final IndexOptions UNIQUE = new IndexOptions().unique(true);

  public static void ensureIndexes() {
    guildUsers.createIndex(Indexes.compoundIndex(ascending("guildId"), ascending("userId")), UNIQUE);
    guildUsers.createIndex(Indexes.ascending("userExp"));
    guildUsers.createIndex(Indexes.descending("userExp"));
    guildWords.createIndex(Indexes.compoundIndex(ascending("guildId"), ascending("word")), UNIQUE);
    guildWords.createIndex(Indexes.ascending("word"));
    guildWords.createIndex(Indexes.descending("word"));
    guildPunishments.createIndex(Indexes.compoundIndex(ascending("guildId"), ascending("userId")));
  }

  /*
   * GUILD
   */

  public static GuildRecord getById(long id) {
    return guilds.findOneAndUpdate(eq("_id", id), combine(
        setOnInsert("firstJoined", System.currentTimeMillis()),
        setOnInsert("guildSettings", new GuildSettings()),
        setOnInsert("brackets", new ArrayList<>())),
        RETURN_UPSERT);
  }

  // COUNTING
  public static void setCount(long id, long newValue) {
    guilds.updateOne(eq("_id", id), set("counting.currentNumber", newValue));
  }

  public static void setHighestCount(long id, long newValue) {
    guilds.updateOne(eq("_id", id), set("counting.highestNumber", newValue));
  }

  public static void save(GuildRecord record) {
    guilds.replaceOne(eq("_id", record.getGuildId()), record, REPLACE_UPSERT);
  }

  /*
   * GUILD USER
   */

  public static GuildUserRecord getUserById(long guildId, long userId) {
    return guildUsers.findOneAndUpdate(and(eq("guildId", guildId), eq("userId", userId)),
        combine(
            setOnInsert("_id", new ObjectId()),
            setOnInsert("userExp", 0L),
            setOnInsert("userLevel", 0),
            setOnInsert("userLastMessage", System.currentTimeMillis()),
            setOnInsert("dateCreated", System.currentTimeMillis())),
        RETURN_UPSERT);
  }

  public static void updateLastMessage(long guildId, long userId) {
    guildUsers.updateOne(and(eq("guildId", guildId), eq("userId", userId)),
        set("userLastMessage", System.currentTimeMillis()));
  }

  public static void incrementExp(long guildId, long userId, int exp) {
    guildUsers.updateOne(and(eq("guildId", guildId), eq("userId", userId)),
        combine(
            inc("userExp", exp),
            setOnInsert("_id", new ObjectId()),
            setOnInsert("userLevel", 0),
            setOnInsert("userLastMessage", System.currentTimeMillis()),
            setOnInsert("dateCreated", System.currentTimeMillis())),
        UPSERT);
  }

  public static void incrementLevel(long guildId, long userId) {
    guildUsers.updateOne(and(eq("guildId", guildId), eq("userId", userId)), inc("userLevel", 1));
  }

  public static void saveUser(GuildUserRecord record) {
    guildUsers.replaceOne(and(eq("guildId", record.getGuildId()), eq("userId", record.getUserId())), record,
        REPLACE_UPSERT);
  }

  /*
   * GUILD WORDS
   */

  public static GuildWordRecord getGuildWordByWord(long guildId, String word) {
    return guildWords.findOneAndUpdate(and(eq("guildId", guildId), eq("word", word)),
        combine(
            setOnInsert("guildId", guildId),
            setOnInsert("word", word),
            setOnInsert("count", 0L),
            setOnInsert("dateCreated", System.currentTimeMillis())),
        RETURN_UPSERT);
  }

  public static List<GuildWordRecord> getWords(long guildId, int amount, int page, String order, boolean reverseOrder) {
    Set<String> allowed = Set.of("word", "count", "firstUse");
    String key = allowed.contains(order) ? order : "count";

    var sort = reverseOrder ? ascending(key) : descending(key);
    int skip = Math.max(0, page) * Math.max(1, amount);

    try (MongoCursor<GuildWordRecord> cursor = guildWords.find()
        .filter(eq("guildId", guildId))
        .sort(sort)
        .skip(skip)
        .limit(amount)
        .iterator()) {
      List<GuildWordRecord> results = new ArrayList<>();
      while (cursor.hasNext()) {
        results.add(cursor.next());
      }
      return results;
    }
  }

  public static void incrementWord(long guildId, String word) {
    guildWords.updateOne(and(eq("guildId", guildId), eq("word", word)),
        combine(
            inc("count", 1),
            setOnInsert("guildId", guildId),
            setOnInsert("word", word),
            setOnInsert("dateCreated", System.currentTimeMillis())),
        UPSERT);
  }

  public static void saveWord(GuildWordRecord record) {
    guildWords.replaceOne(eq("_id", record.getRecordId()), record, REPLACE_UPSERT);
  }

  /*
   * GUILD PUNISHMENTS
   */

  public static GuildPunishmentRecord getPunishmentById(String id) {
    return guildPunishments.find(eq("_id", new ObjectId(id))).first();
  }

  public static List<GuildPunishmentRecord> getPunishmentsByUser(long guildId, long userId, int page, int amount,
      String order, boolean reverseOrder) {
    Set<String> allowed = Set.of("word", "count", "firstUse");
    String key = allowed.contains(order) ? order : "count";

    var sort = reverseOrder ? ascending(key) : descending(key);
    int skip = Math.max(0, page) * Math.max(1, amount);

    try (MongoCursor<GuildPunishmentRecord> cursor = guildPunishments.find()
        .filter(and(eq("guildId", guildId), eq("userId", userId)))
        .sort(sort)
        .skip(skip)
        .limit(amount)
        .iterator()) {
      List<GuildPunishmentRecord> results = new ArrayList<>();
      while (cursor.hasNext()) {
        results.add(cursor.next());
      }
      return results;
    }
  }

  public static List<GuildPunishmentRecord> getPunishmentsByGuild(long guildId, int page, int amount, String order,
      boolean reverseOrder) {
    Set<String> allowed = Set.of("word", "count", "firstUse");
    String key = allowed.contains(order) ? order : "count";

    var sort = reverseOrder ? ascending(key) : descending(key);
    int skip = Math.max(0, page) * Math.max(1, amount);

    try (MongoCursor<GuildPunishmentRecord> cursor = guildPunishments.find()
        .filter(eq("guildId", guildId))
        .sort(sort)
        .skip(skip)
        .limit(amount)
        .iterator()) {
      List<GuildPunishmentRecord> results = new ArrayList<>();
      while (cursor.hasNext()) {
        results.add(cursor.next());
      }
      return results;
    }
  }

  public static void savePunishment(GuildPunishmentRecord record) {
    guildPunishments.replaceOne(eq("_id", record.getPunishmentId()), record, REPLACE_UPSERT);
  }

}
