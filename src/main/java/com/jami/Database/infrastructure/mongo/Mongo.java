package com.jami.Database.infrastructure.mongo;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;

import com.jami.App;
import com.jami.Database.Config.ConfigRecord;
import com.jami.Database.repository.ConfigRepo;
import com.jami.Database.repository.FeatureRequestRepo;
import com.jami.Database.repository.GuildRepo;
import com.jami.Database.repository.UserRepo;
import com.jami.Database.repository.WordRepo;
import com.jami.bot.Log;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;

import static org.bson.codecs.configuration.CodecRegistries.*;

public class Mongo {

  private static final IndexOptions UNIQUE = new IndexOptions().unique(true);

  // MongoDB
  private static MongoClient mongoClient;
  private static CodecRegistry pojoCodecRegistry;
  private static MongoDatabase database;

  private static ConfigRepo configRepo;
  private static FeatureRequestRepo featureRequestRepo;
  private static GuildRepo guildRepo;
  private static UserRepo userRepo;
  private static WordRepo wordRepo;

  private static ConfigRecord globalConfig;

  private static Logger LOGGER = Log.getLogger();

  public static void connectMongoDb(String databaseUri, String databaseName) {
    LOGGER.info("[INFO] Attempting to connect to MongoDB");

    while (mongoClient == null) {

      pojoCodecRegistry = fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

      MongoClientSettings settings = MongoClientSettings.builder()
      .applyConnectionString(new ConnectionString(databaseUri))
      .codecRegistry(pojoCodecRegistry)
      .retryWrites(true)
      .build();

      mongoClient = MongoClients.create(settings);

      if (mongoClient == null) {
        LOGGER.error("[ERROR] Failed to connect to MongoDB, reattempting in 10 seconds");
        App.sleepQuietly(10000);
      }
    }
    database = mongoClient.getDatabase(databaseName);
    LOGGER.info("[INFO] Connected to MongoDB");
  }

  // Indexes

  public static void ensureIndexes() {

  }

  // DB

  public static MongoDatabase getDatabase() {
    return database;
  }

  // REPOS

  public static ConfigRepo getConfigRepo() {
    if (configRepo == null) {
      configRepo = new MongoConfigRepo();
    }
    return configRepo;
  }

  public static FeatureRequestRepo getFeatureRequestRepo() {
    if (featureRequestRepo == null) {
      featureRequestRepo = new MongoFeatureRequestRepo();
    }
    return featureRequestRepo;
  }

  public static GuildRepo getGuildRepo() {
    if (guildRepo == null) {
      guildRepo = new MongoGuildRepo();
    }
    return guildRepo;
  }

  public static UserRepo getUserRepo() {
    if (userRepo == null) {
      userRepo = new MongoUserRepo();
    }
    return userRepo;
  }

  public static WordRepo getWordRepo() {
    if (wordRepo == null) {
      wordRepo = new MongoWordRepo();
    }
    return wordRepo;
  }

  // GLOBAL CONFIG
  
  public static ConfigRecord getGlobalConfig() {
    return globalConfig;
  }

  public static void setGlobalConfig(ConfigRecord config) {
    globalConfig = config;
  }

  // MongoClient
  
  public static void closeMongoClient() {
    mongoClient.close();
  }
}
