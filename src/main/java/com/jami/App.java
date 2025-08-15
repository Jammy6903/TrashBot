package com.jami;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jami.botAdmin.commandsAdmin;
import com.jami.database.config.config;
import com.jami.utilities.guildLogging.guildChange;
import com.jami.utilities.guildLogging.memberChange;
import com.jami.utilities.guildLogging.messages;
import com.jami.utilities.guildLogging.voiceEvents;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import de.tudarmstadt.ukp.jwktl.JWKTL;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Scanner;

public class App {

        private static ShardManager shardManager;
        private static final EventWaiter eventWaiter = new EventWaiter();

        private static final File propFile = new File("config.properties");
        private static Properties props = new Properties();

        public static MongoClient mongoClient;

        private static String currentConfig;
        public static config CONFIG;

        private static File dumpFile = new File("assets/dictionary/wiktionary.xml");
        private static File outputDirectory = new File("assets/dictionary/output");

        public static void main(String[] args) {
                System.setProperty("jdk.xml.maxGeneralEntitySizeLimit", "0");
                System.setProperty("jdk.xml.totalEntitySizeLimit", "0");

                try {
                        props.load(new FileInputStream(propFile));

                        currentConfig = props.getProperty("CURRENT_CONFIG");

                        mongoClient = MongoClients.create(props.getProperty("DATABASE_URI"));

                        if (args.length == 1) {
                                new App().start(args[0]);
                        } else {
                                new App().start(props.getProperty("TOKEN"));
                        }
                } catch (Exception e) {
                        System.out.println("Bot startup failed: " + e);
                }

                new config("defaultConfig").saveConfig(); // Makes sure theres always a default config available

                CONFIG = new config(currentConfig);
                String status = CONFIG.getBotStatus();
                try {
                        shardManager.getShards()
                                        .forEach(jda -> jda.getPresence().setActivity(Activity.customStatus(status)));
                } catch (Exception e) {
                        System.out.println(e);
                }

                Scanner s = new Scanner(System.in);

                while (s.hasNext()) {
                        String command = s.nextLine();
                        commandsAdmin.adminCommands(null, command);
                }

                s.close();
        }

        private void start(String Token) throws Exception {
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
                                .addEventListeners(eventWaiter, new eventListeners(), new guildChange(),
                                                new memberChange(),
                                                new messages(), new voiceEvents())
                                .setMemberCachePolicy(MemberCachePolicy.ALL)
                                .setShardsTotal(-1)
                                .build();

                getCommands();
        }

        public static void parseWiktionary() {
                new Thread(() -> JWKTL.parseWiktionaryDump(dumpFile, outputDirectory, true)).start();
        }

        public static File getWiktionary() {
                return outputDirectory;
        }

        public static EventWaiter getEventWaiter() {
                return eventWaiter;
        }

        public static Properties getProps() {
                return props;
        }

        public static void saveProps() {
                try {
                        props.store(new FileOutputStream(propFile), null);
                } catch (Exception e) {
                        System.out.println("[ERROR] Unable to save to properties file - " + e);
                }
        }

        public static ShardManager getShardManager() {
                return shardManager;
        }

        public static int totalGuildCount() {
                return shardManager.getShards().stream()
                                .mapToInt(jda -> jda.getGuilds().size())
                                .sum();
        }

        private static void getCommands() {

                /*
                 * guild-settings sub commnads
                 */

                // Levelling Roles

                SubcommandData addLevellingRole = new SubcommandData("add-levelling-role",
                                "add a levelling role as a reward for members reaching certain levelling milestones")
                                .addOption(OptionType.INTEGER, "level", "level for member to reach to receive the role",
                                                true)
                                .addOption(OptionType.ROLE, "role", "role to give to the member", true);

                SubcommandData removeLevellingRole = new SubcommandData("remove-levelling-role",
                                "delete a levelling role.")
                                .addOption(OptionType.STRING, "id",
                                                "ID of the entry you want to remove, find it with /guild-settings list-levelling-roles",
                                                true);

                SubcommandData listLevellingRoles = new SubcommandData("list-levelling-roles",
                                "List all levelling roles");

                // Levelling

                SubcommandData setExpSettings = new SubcommandData("set-exp-settings", "set values for gaining exp")
                                .addOption(OptionType.INTEGER, "exp-increment",
                                                "how much exp to give per valid message")
                                .addOption(OptionType.INTEGER, "exp-variation",
                                                "how much to vary exp per valid message")
                                .addOption(OptionType.INTEGER, "exp-cooldown",
                                                "how long between messages to be able to gain exp");

                SubcommandData setLevelSettings = new SubcommandData("set-level-settings",
                                "set values for level progression")
                                .addOption(OptionType.INTEGER, "base-exp",
                                                "where levels start, e.g. if set to 200, level 1 is gained at 200 exp")
                                .addOption(OptionType.INTEGER, "growth",
                                                "how much to increase exp requirements per level, e.g. if set to 1, exp requirement would be linear");

                /*
                 * level sub commands
                 */

                SubcommandData levelCard = new SubcommandData("card", "See your or another users levelling card")
                                .addOption(OptionType.USER, "user", "who's level do you want to see?")
                                .addOption(OptionType.BOOLEAN, "global", "whether to look at global levelling");

                SubcommandData levelLeaderboard = new SubcommandData("leaderboard", "See top users")
                                .addOption(OptionType.INTEGER, "page", "leaderboard page")
                                .addOption(OptionType.STRING, "order", "what to order by")
                                .addOption(OptionType.BOOLEAN, "reverse", "whether to reverse the order")
                                .addOption(OptionType.BOOLEAN, "global", "whether to look at global levelling");

                /*
                 * words sub commands
                 */

                SubcommandData wordInfo = new SubcommandData("info", "See how much a word is used")
                                .addOption(OptionType.STRING, "word", "What word to see info for", true)
                                .addOption(OptionType.BOOLEAN, "global", "whether to look at global word stats");

                SubcommandData wordLeaderboard = new SubcommandData("leaderboard", "See top word usage")
                                .addOption(OptionType.INTEGER, "page", "leaderboard page")
                                .addOption(OptionType.STRING, "order", "what to order by")
                                .addOption(OptionType.BOOLEAN, "reverse", "whether to reverse the order")
                                .addOption(OptionType.BOOLEAN, "global", "whether to look at global word stats");

                /*
                 * info sub commands
                 */

                SubcommandData bot = new SubcommandData("bot", "shows info about the bot");

                SubcommandData guild = new SubcommandData("guild", "shows info about the guild");

                SubcommandData member = new SubcommandData("member", "shows info about a member in the guild")
                                .addOption(OptionType.USER, "member", "which member?");

                SubcommandData user = new SubcommandData("user", "shows known info about a discord user")
                                .addOption(OptionType.NUMBER, "user-id", "user's discord ID");

                SubcommandData role = new SubcommandData("role", "shows info about a guild role")
                                .addOption(OptionType.ROLE, "role", "which role?");

                SubcommandData channel = new SubcommandData("channel", "shows info about a channel")
                                .addOption(OptionType.CHANNEL, "channel", "which channel?");

                shardManager.getShards().forEach(jda -> jda.updateCommands().addCommands(
                                Commands.slash("featurerequest",
                                                "submit a feature request for something you think this bot is missing."),
                                Commands.slash("guild-settings", "change settings for your guild")
                                                .addSubcommands(addLevellingRole, removeLevellingRole,
                                                                listLevellingRoles, setExpSettings,
                                                                setLevelSettings)
                                                .setDefaultPermissions(DefaultMemberPermissions
                                                                .enabledFor(Permission.ADMINISTRATOR))
                                                .setContexts(InteractionContextType.GUILD),
                                Commands.slash("level", "check out your level progression")
                                                .addSubcommands(levelCard, levelLeaderboard)
                                                .setContexts(InteractionContextType.GUILD),
                                Commands.slash("word", "check out word usage")
                                                .addSubcommands(wordInfo, wordLeaderboard)
                                                .setContexts(InteractionContextType.GUILD),
                                Commands.slash("info", "shows info about various objects")
                                                .addSubcommands(bot, guild, member, user, role, channel)
                                                .setContexts(InteractionContextType.GUILD))
                                .queue());
        }
}
