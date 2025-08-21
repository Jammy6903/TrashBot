package com.jami.JDA;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.sharding.ShardManager;

public class CommandsSetup {
  public static void getCommands(ShardManager sm) {

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

    sm.getShards().forEach(jda -> jda.updateCommands().addCommands(
        Commands.slash("featurerequest",
            "submit a feature request for something you think this bot is missing."),
        Commands.slash("guild-settings", "change settings for your guild")
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
