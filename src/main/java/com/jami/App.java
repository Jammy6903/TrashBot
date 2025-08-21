package com.jami;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jami.BotAdmin.CommandsAdmin;
import com.jami.Database.Config.ConfigRecord;
import com.jami.Interaction.Utilities.GuildLogging.Logging;
import com.jami.JDA.CommandsSetup;
import com.jami.JDA.EventListeners;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import de.tudarmstadt.ukp.jwktl.JWKTL;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
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

public class App {

        // JDA
        private static ShardManager shardManager;
        private static final EventWaiter EVENT_WAITER = new EventWaiter();

        // .properties
        private static final File PROP_FILE = new File("config.properties");
        private static Properties props = new Properties();

        // MongoDB
        private static MongoClient mongoClient;

        // Global Config
        private static ConfigRecord globalConfig;

        // Wiktionary
        private static File wiktionaryDumpFile = new File("assets/dictionary/wiktionary.xml");
        private static File wiktionaryOutputDirectory = new File("assets/dictionary/output");

        public static void main(String[] args) {
                // Increase arbitrary XML limits to allow for Wiktionary parsing
                System.setProperty("jdk.xml.maxGeneralEntitySizeLimit", "0");
                System.setProperty("jdk.xml.totalEntitySizeLimit", "0");

                // Load Properties File
                System.out.println("[INFO] Loading properties file");
                try {
                        props.load(new FileInputStream(PROP_FILE));
                } catch (IOException e) {
                        System.out.println("[ERROR] Property file not found - " + e);
                        System.exit(1);
                }
                System.out.println("[INFO] Properties loaded");

                wiktionaryDumpFile = new File(props.getProperty("WIKTIONARY_XML"));
                wiktionaryOutputDirectory = new File(props.getProperty("WIKTIONARY_DIRECTORY"));

                // Connect to MongoDB - Keep trying until connected
                System.out.println("[INFO] Attempting to connect to MongoDB");
                while (mongoClient == null) {
                        mongoClient = MongoClients.create(props.getProperty("DATABASE_URI"));
                        if (mongoClient == null) {
                                System.out.println("[ERROR] Failed to connect to MongoDB, reattempting in 10 seconds");
                                wait(10000);
                        }
                }
                System.out.println("[INFO] Connected to MongoDB");

                // Start shard manager
                System.out.println("[INFO] Attempting to connect to Discord");
                while (shardManager == null) {
                        new App().startShardManager(props.getProperty("TOKEN"));
                        if (shardManager == null) {
                                System.out.println("[ERROR] Failed to connect to Discord, reattempting in 10 seconds");
                                wait(1000);
                        }
                }
                System.out.println("[INFO] Connected to Discord");

                new ConfigRecord("defaultConfig").saveConfig(); // Makes sure theres always a default config available
                globalConfig = new ConfigRecord(props.getProperty("CURRENT_CONFIG")); // Loads current config from DB

                // After-startup bot configuration
                String status = globalConfig.getBotStatus();
                if (!status.isEmpty()) {
                        shardManager.getShards()
                                        .forEach(jda -> jda.getPresence().setActivity(Activity.customStatus(status)));
                }
                CommandsSetup.getCommands(shardManager);

                // Start scanner for console commands
                Scanner s = new Scanner(System.in);
                while (s.hasNext()) {
                        String command = s.nextLine();
                        if (command.equals("shutdown")) {
                                s.close();
                                shardManager.shutdown();
                                System.exit(0);
                        }
                        CommandsAdmin.adminCommands(null, command);
                }
        }

        private void startShardManager(String Token) {
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
                        System.out.println("[ERROR] Couldn't start shard manager, reattempting in 10 seconds - " + e);
                }
        }

        public static EventWaiter getEventWaiter() {
                return EVENT_WAITER;
        }

        public static ShardManager getShardManager() {
                return shardManager;
        }

        public static int totalGuildCount() {
                return shardManager.getShards().stream()
                                .mapToInt(jda -> jda.getGuilds().size())
                                .sum();
        }

        public static void parseWiktionary() {
                new Thread(() -> JWKTL.parseWiktionaryDump(wiktionaryDumpFile, wiktionaryOutputDirectory, true))
                                .start();
        }

        public static File getWiktionary() {
                return wiktionaryOutputDirectory;
        }

        public static Properties getProps() {
                return props;
        }

        public static void saveProps() {
                try {
                        props.store(new FileOutputStream(PROP_FILE), null);
                } catch (Exception e) {
                        System.out.println("[ERROR] Unable to save to properties file - " + e);
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

        private static void wait(int ms) {
                try {
                        Thread.sleep(ms);
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        ;
                }
        }
}
