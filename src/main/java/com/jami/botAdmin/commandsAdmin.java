package com.jami.botAdmin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jami.App;
import com.jami.database.config.config;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class commandsAdmin {
  public static void adminCommands(MessageReceivedEvent event) {
    String message = event.getMessage().getContentRaw();
    message = message.replaceFirst("a!", "");
    List<String> args = new ArrayList<>(Arrays.asList(message.split(" ")));
    switch (args.get(0)) {
      case "set-config":
        if (args.size() == 1) {
          event.getMessage().reply("**Command Usage:** a!set-config <configName>").queue();
          return;
        }
        App.getProps().setProperty("CURRENT_CONFIG", args.get(1));
        App.saveProps();
        App.CONFIG = new config(args.get(1));
        event.getMessage().reply("Config set to " + args.get(1)).queue();
        ;
        break;
      case "load-config":
        if (args.size() == 1) {
          event.getMessage().reply("**Command Usage:** a!load-config <configName>").queue();
          return;
        }
        App.CONFIG = new config(args.get(1));
        event.getMessage().reply("Config " + args.get(1) + " loaded").queue();
        break;
      case "save-config":
        App.CONFIG.saveConfig();
        event.getMessage().reply("Config " + App.CONFIG.getConfigName() + " saved").queue();
        break;
      case "show-config":
        String disabledFeatures = "";
        for (String feature : App.CONFIG.getDisabledFeatures()) {
          disabledFeatures += "- " + feature + "\n";
        }
        String disabledCommands = "";
        for (String command : App.CONFIG.getDisabledCommands()) {
          disabledCommands += "- " + command + "\n";
        }
        String adminIds = "";
        for (long id : App.CONFIG.getAdminIds()) {
          adminIds += "- " + String.valueOf(id) + "\n";
        }
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle(App.CONFIG.getConfigName())
            .setDescription("Status: " + App.CONFIG.getBotStatus() + "\n" +
                "Color: " + App.CONFIG.getBotColor() + "\n" +
                "Exp Increment: " + App.CONFIG.getExpIncrement() + "\n" +
                "Exp Variation: " + App.CONFIG.getExpVariation() + "\n" +
                "Exp Cooldown: " + App.CONFIG.getExpCooldown() + "\n" +
                "Level Base: " + App.CONFIG.getLevelBase() + "\n" +
                "Level Growth: " + App.CONFIG.getLevelGrowth() + "\n" +
                "Disabled Features: \n" + disabledFeatures + "\n" +
                "Disabled Commands: \n" + disabledCommands + "\n" +
                "Admin IDs: \n" + adminIds);
        event.getChannel().sendMessageEmbeds(embed.build()).queue();
        break;
      case "set-config-name":
        if (args.size() == 1) {
          event.getMessage().reply("**Command Usage:** a!set-config-name <configName>").queue();
          return;
        }
        App.CONFIG.setConfigName(args.get(1));
        event.getMessage().reply("Config name set to " + args.get(1)).queue();
        break;
      case "set-bot-status":
        if (args.size() == 1) {
          event.getMessage().reply("**Command Usage:** a!set-bot-status <status>").queue();
          return;
        }
        String status = "";
        for (int i = 1; i < args.size(); i++) {
          status += args.get(i) + " ";
        }
        App.CONFIG.setBotStatus(status);
        event.getJDA().getPresence().setActivity(Activity.customStatus(status));
        event.getMessage().reply("Status set to " + status).queue();
        break;
      case "set-bot-color":
        if (args.size() == 1) {
          event.getMessage().reply("**Command Usage:** a!set-bot-color <#color>").queue();
          return;
        }
        String color = args.get(1).replaceFirst("#", "");
        App.CONFIG.setBotColor(color);
        event.getMessage().reply("Color set to" + color).queue();
        break;
      case "set-exp-increment":
        if (args.size() == 1) {
          event.getMessage().reply("**Command Usage:** a!set-exp-increment <exp>").queue();
          return;
        }
        try {
          App.CONFIG.setExpIncrement(Integer.valueOf(args.get(1)));
          event.getMessage().reply("Exp Increment set to " + args.get(1)).queue();
        } catch (Exception e) {
          event.getMessage().reply("**Command Usage:** a!set-exp-increment <exp>").queue();
          return;
        }
        break;
      case "set-exp-variation":
        if (args.size() == 1) {
          event.getMessage().reply("**Command Usage:** a!set-exp-variation <variation>").queue();
          return;
        }
        try {
          App.CONFIG.setExpVariation(Integer.valueOf(args.get(1)));
          event.getMessage().reply("Exp Variation set to " + args.get(1)).queue();
        } catch (Exception e) {
          event.getMessage().reply("**Command Usage:** a!set-exp-variation <variation>").queue();
          return;
        }
        break;
      case "set-exp-cooldown":
        if (args.size() == 1) {
          event.getMessage().reply("**Command Usage:** a!set-exp-cooldown <cooldown>").queue();
          return;
        }
        try {
          App.CONFIG.setExpCooldown(Long.valueOf(args.get(1)));
          event.getMessage().reply("Exp Cooldown set to " + args.get(1)).queue();
        } catch (Exception e) {
          event.getMessage().reply("**Command Usage:** a!set-exp-cooldown <cooldown>").queue();
          return;
        }
        break;
      case "set-level-base":
        if (args.size() == 1) {
          event.getMessage().reply("**Command Usage:** a!set-level-base <exp>").queue();
          return;
        }
        try {
          App.CONFIG.setLevelBase(Long.valueOf(args.get(1)));
          event.getMessage().reply("Level Base set to " + args.get(1)).queue();
        } catch (Exception e) {
          event.getMessage().reply("**Command Usage:** a!set-level-base <exp>").queue();
          return;
        }
        break;
      case "set-level-growth":
        if (args.size() == 1) {
          event.getMessage().reply("**Command Usage:** a!set-level-growth <growth>").queue();
          return;
        }
        try {
          App.CONFIG.setLevelGrowth(Double.valueOf(args.get(1)));
          event.getMessage().reply("Level Growth set to " + args.get(1)).queue();
        } catch (Exception e) {
          event.getMessage().reply("**Command Usage:** a!set-level-growth <growth>").queue();
          return;
        }
        break;
      case "add-disabled-feature":
        if (args.size() == 1) {
          event.getMessage().reply("**Command Usage:** a!add-disabled-feature <feature> [features...]").queue();
          return;
        }
        String addedFeatures = "Following features added to Disabled Features: ";
        for (int i = 1; i < args.size(); i++) {
          App.CONFIG.addDisabledFeature(args.get(i));
          addedFeatures += args.get(i) + ", ";
        }
        event.getMessage().reply(addedFeatures).queue();
        break;
      case "remove-disabled-feature":
        if (args.size() == 1) {
          event.getMessage().reply("**Command Usage:** a!remove-disabled-feature <feature> [features...]").queue();
          return;
        }
        String removedFeatures = "Following features removed from Disabled Features: ";
        for (int i = 1; i < args.size(); i++) {
          App.CONFIG.removeDisabledFeature(args.get(i));
          removedFeatures += args.get(i) + ", ";
        }
        event.getMessage().reply(removedFeatures).queue();
        break;
      case "add-disabled-command":
        if (args.size() == 1) {
          event.getMessage().reply("**Command Usage:** a!add-disabled-command <command> [commands...]").queue();
          return;
        }
        String addedCommands = "Following commands added from Disabled Commands: ";
        for (int i = 1; i < args.size(); i++) {
          App.CONFIG.addDisabledCommand(args.get(i));
          addedCommands += args.get(i) + ", ";
        }
        event.getMessage().reply(addedCommands).queue();
        break;
      case "remove-disabled-command":
        if (args.size() == 1) {
          event.getMessage().reply("**Command Usage:** a!remove-disabled-command <command> [commands...]").queue();
          return;
        }
        String removedCommands = "Following commands removed from Disabled Commands: ";
        for (int i = 1; i < args.size(); i++) {
          App.CONFIG.removeDisabledCommand(args.get(i));
          removedCommands += args.get(i) + ", ";
        }
        event.getMessage().reply(removedCommands).queue();
        break;
      case "add-admin":
        if (args.size() == 1) {
          event.getMessage().reply("**Command Usage:** a!add-admin <userId> [userIds...]").queue();
          return;
        }
        boolean addError = false;
        String addErrorString = "Error adding following IDs - maybe they aren't IDs: ";
        String addedIds = "Following IDs added to Admin IDs: ";
        for (int i = 1; i < args.size(); i++) {
          try {
            App.CONFIG.addAdminId(Long.valueOf(args.get(i)));
            addedIds += args.get(i) + ", ";
          } catch (Exception e) {
            addError = true;
            addErrorString += args.get(i) + ", ";
          }
        }
        event.getMessage().reply(addedIds).queue();
        if (addError) {
          event.getMessage().reply(addErrorString).queue();
        }
        break;
      case "remove-admin":
        if (args.size() == 1) {
          event.getMessage().reply("**Command Usage: a!remove-admin <userId> [userIds...]").queue();
          return;
        }
        boolean error = false;
        String errorString = "Error removing following IDs = maybe they aren't IDs: ";
        String removedIds = "Following IDs removed from Admin IDs: ";
        for (int i = 1; i < args.size(); i++) {
          try {
            App.CONFIG.removeAdminId(Long.valueOf(args.get(i)));
            removedIds += args.get(i) + ", ";
          } catch (Exception e) {
            error = true;
            errorString += args.get(i) + ", ";
          }
        }
        event.getMessage().reply(removedIds).queue();
        if (error) {
          event.getMessage().reply(errorString).queue();
        }
        break;
      default:
        event.getMessage().reply(
            "**Unknown Command:** available options - set-config, load-config, save-config, " +
                "show-config, set-config-name, set-bot-status, set-exp-increment, set-exp-variation, " +
                "set-exp-cooldown, set-level-base, set-level-growth, add-disabled-feature, " +
                "remove-disabled-feature, add-disabled-command, remove-disabled-command, add-admin, remove-admin")
            .queue();
        break;
    }
  }
}
