package com.jami;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import com.jami.BotAdmin.CommandsAdmin;
import com.jami.Database.Config.ConfigRecord;
import com.jami.Database.repositories.ConfigRepo;
import com.jami.Interaction.Utilities.GuildLogging.Logging;
import com.jami.JDA.CommandsSetup;
import com.jami.JDA.EventListeners;
import com.jami.JDA.JDATools;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import static org.bson.codecs.configuration.CodecRegistries.*;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

        // Logger
        private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

        // JDA
        private static ShardManager shardManager;
        private static final EventWaiter EVENT_WAITER = new EventWaiter();

        // .properties
        private static final File PROP_FILE = new File("config.properties");
        private static Properties props = new Properties();

        // MongoDB
        private static MongoClient mongoClient;
        private static CodecRegistry pojoCodecRegistry;

        // Global Config
        private static ConfigRecord globalConfig;

        // Wiktionary
        public static void main(String[] args) {
                // Increase arbitrary XML limits to allow for Wiktionary parsing
                System.setProperty("jdk.xml.maxGeneralEntitySizeLimit", "0");
                System.setProperty("jdk.xml.totalEntitySizeLimit", "0");

                // Load Properties File
                loadProperties();

                // Connect to MongoDb
                connectMongoDb();

                // Start shard manager
                new App().startShardManager(props.getProperty("TOKEN"));

                // Load Config from DB
                loadConfig();

                // After-startup bot configuration
                if (globalConfig != null) {
                        JDATools.SetBotStatus(globalConfig.getBotStatus());
                }
                CommandsSetup.getCommands(shardManager);

                // Start scanner for console commands
                Scanner s = new Scanner(System.in);
                while (s.hasNext()) {
                        String command = s.nextLine();
                        if (command.equals("shutdown")) {
                                s.close();
                                shutdown();
                        }
                        CommandsAdmin.adminCommands(null, command);
                }
        }

        private static void loadProperties() {
                LOGGER.info("[INFO] Loading properties file");
                try {
                        props.load(new FileInputStream(PROP_FILE));
                } catch (IOException e) {
                        LOGGER.error("[ERROR] Property file not found at {}", PROP_FILE, e);
                        System.exit(1);
                }
                LOGGER.info("[INFO] Properties loaded");
        }

        private static void connectMongoDb() {
                LOGGER.info("[INFO] Attempting to connect to MongoDB");
                while (mongoClient == null) {
                        pojoCodecRegistry = fromRegistries(
                                        MongoClientSettings.getDefaultCodecRegistry(),
                                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));
                        MongoClientSettings settings = MongoClientSettings.builder()
                                        .applyConnectionString(new ConnectionString(props.getProperty("DATABASE_URI")))
                                        .codecRegistry(pojoCodecRegistry)
                                        .retryWrites(true)
                                        .build();
                        mongoClient = MongoClients.create(settings);
                        if (mongoClient == null) {
                                LOGGER.error("[ERROR] Failed to connect to MongoDB, reattempting in 10 seconds");
                                wait(10000);
                        }
                }
                LOGGER.info("[INFO] Connected to MongoDB");
        }

        private static void loadConfig() {
                globalConfig = ConfigRepo.getByName(props.getProperty("CURRENT_CONFIG")); // Loads current config from
                                                                                          // DB

        }

        private void startShardManager(String Token) {
                LOGGER.info("[INFO] Attempting to connect to Discord...");
                while (shardManager == null) {
                        try {
                                shardManager = DefaultShardManagerBuilder.createDefault(Token)
                                                .setStatus(OnlineStatus.ONLINE)
                                                .enableIntents(
                                                                GatewayIntent.GUILD_MEMBERS,
                                                                GatewayIntent.GUILD_MODERATION,
                                                                GatewayIntent.GUILD_WEBHOOKS,
                                                                GatewayIntent.GUILD_INVITES,
                                                                GatewayIntent.GUILD_VOICE_STATES,
                                                                GatewayIntent.GUILD_MESSAGES,
                                                                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                                                                GatewayIntent.GUILD_MESSAGE_TYPING,
                                                                GatewayIntent.DIRECT_MESSAGES,
                                                                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                                                                GatewayIntent.DIRECT_MESSAGE_TYPING,
                                                                GatewayIntent.MESSAGE_CONTENT,
                                                                GatewayIntent.AUTO_MODERATION_EXECUTION,
                                                                GatewayIntent.GUILD_MESSAGE_POLLS)
                                                .setEventManagerProvider(id -> new AnnotatedEventManager())
                                                .addEventListeners(EVENT_WAITER, new EventListeners(), new Logging())
                                                .setMemberCachePolicy(MemberCachePolicy.ALL)
                                                .setBulkDeleteSplittingEnabled(true)
                                                .setShardsTotal(-1)
                                                .build();
                        } catch (Exception e) {
                                LOGGER.error("[ERROR] Couldn't start shard manager, reattempting in 10 seconds", e);
                                wait(10000);
                        }
                }
                LOGGER.info("[INFO] Connected to Discord");
        }

        private static void shutdown() {
                shardManager.shutdown();
                mongoClient.close();
                System.exit(0);
        }

        public static EventWaiter getEventWaiter() {
                return EVENT_WAITER;
        }

        public static ShardManager getShardManager() {
                return shardManager;
        }

        public static Properties getProps() {
                return props;
        }

        public static void saveProps() {
                try {
                        props.store(new FileOutputStream(PROP_FILE), null);
                } catch (Exception e) {
                        LOGGER.error("[ERROR] Unable to save to properties file - ", e);
                }
        }

        public static MongoClient getMongoClient() {
                return mongoClient;
        }

        public static ConfigRecord getGlobalConfig() {
                return globalConfig;
        }

        public static void setGlobalConfig(ConfigRecord config) {
                globalConfig = config;
        }

        public static Logger getLogger() {
                return LOGGER;
        }

        private static void wait(int ms) {
                try {
                        Thread.sleep(ms);
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        ;
                }
        }
}
