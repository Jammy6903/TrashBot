package com.jami;

import com.jami.Database.infrastructure.mongo.Mongo;
import com.jami.bot.Bot;
import com.jami.bot.Console;
import com.jami.bot.Props;

import java.util.Properties;


public class App {


        public static void main(String[] args) {
                // Increase arbitrary XML limits to allow for Wiktionary parsing
                System.setProperty("jdk.xml.maxGeneralEntitySizeLimit", "0");
                System.setProperty("jdk.xml.totalEntitySizeLimit", "0");

                // Load Properties File
                Props.loadProperties();
                Properties props = Props.getProps();

                // Connect to MongoDb
                Mongo.connectMongoDb(props.getProperty("DATABASE_URI"), props.getProperty("DATABASE_NAME"));
                Mongo.getConfigRepo().getByName(props.getProperty("CURRENT_CONFIG"));

                // Start shard manager
                new Bot(props.getProperty("TOKEN"));

                // Start scanner for console commands
                Console.startScanning();
        }

        public static void shutdown() {
                Bot.getShardManager().shutdown();
                Mongo.closeMongoClient();
                Console.shutdown();
                System.exit(0);
        }

        public static void sleepQuietly(int ms) {
                try {
                        Thread.sleep(ms);
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                }
        }
}
