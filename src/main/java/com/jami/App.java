package com.jami;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jami.utilities.guildLogging.guildChange;
import com.jami.utilities.guildLogging.memberChange;
import com.jami.utilities.guildLogging.messages;
import com.jami.utilities.guildLogging.voiceEvents;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

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

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class App {

    private static JDA jda;
    private static final EventWaiter eventWaiter = new EventWaiter();
    private static final File config = new File("config.properties");
    private static Properties Config = new Properties();
    public static MongoClient mongoClient;

    public static void main(String[] args) {
        try {
            Config.load(new FileInputStream(config));
            mongoClient = MongoClients.create(Config.getProperty("DATABASE_URI"));
            if (args.length == 1) {
                new App().start(args[0]);
            } else {
                new App().start(Config.getProperty("TOKEN"));
            }
        } catch (Exception e) {
            System.out.println("Bot startup failed: " + e);
        }
    }

    private void start(String Token) throws Exception {
        jda = JDABuilder.createDefault(Token)
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
                .setEventManager(new AnnotatedEventManager())
                .addEventListeners(eventWaiter, new eventListeners(), new guildChange(), new memberChange(),
                        new messages(), new voiceEvents())
                .build();

        if (Config.getProperty("STATUS") != null) {
            jda.getPresence().setActivity(Activity.customStatus(Config.getProperty("STATUS")));
        }

        getCommands(jda);
    }

    public static EventWaiter getEventWaiter() {
        return eventWaiter;
    }

    public static Properties getConfig() {
        return Config;
    }

    private static void getCommands(JDA jda) {

        /*
         * / guild-settings sub commnads
         */

        // Levelling Roles

        SubcommandData addLevellingRole = new SubcommandData("add-levelling-role",
                "add a levelling role as a reward for members reaching certain levelling milestones")
                .addOption(OptionType.INTEGER, "level", "level for member to reach to receive the role", true)
                .addOption(OptionType.ROLE, "role", "role to give to the member", true);

        SubcommandData removeLevellingRole = new SubcommandData("remove-levelling-role", "delete a levelling role.")
                .addOption(OptionType.STRING, "id",
                        "ID of the entry you want to remove, find it with /guild-settings list-levelling-roles", true);

        SubcommandData listLevellingRoles = new SubcommandData("list-levelling-roles", "List all levelling roles");

        // Levelling

        SubcommandData setExpSettings = new SubcommandData("set-exp-settings", "set values for gaining exp")
                .addOption(OptionType.INTEGER, "exp-increment", "how much exp to give per valid message")
                .addOption(OptionType.INTEGER, "exp-variation", "how much to vary exp per valid message")
                .addOption(OptionType.INTEGER, "exp-cooldown", "how long between messages to be able to gain exp");

        SubcommandData setLevelSettings = new SubcommandData("set-level-settings", "set values for level progression")
                .addOption(OptionType.INTEGER, "base-exp",
                        "where levels start, e.g. if set to 200, level 1 is gained at 200 exp")
                .addOption(OptionType.INTEGER, "growth",
                        "how much to increase exp requirements per level, e.g. if set to 1, exp requirement would be linear");

        /*
         * / level sub commands
         */

        SubcommandData card = new SubcommandData("card", "See your or another users levelling card")
                .addOption(OptionType.USER, "user", "who's level do you want to see?");

        jda.updateCommands().addCommands(
                Commands.slash("featurerequest",
                        "Submit a feature request for something you think this bot is missing."),
                Commands.slash("guild-settings", "change settings for your guild")
                        .addSubcommands(addLevellingRole, removeLevellingRole, listLevellingRoles, setExpSettings,
                                setLevelSettings)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                        .setContexts(InteractionContextType.GUILD),
                Commands.slash("level", "check out your level progression")
                        .addSubcommands(card)
                        .setContexts(InteractionContextType.GUILD))
                .queue();
    }
}
