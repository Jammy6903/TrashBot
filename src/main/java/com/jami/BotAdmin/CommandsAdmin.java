package com.jami.BotAdmin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jami.App;
import com.jami.Database.Feature;
import com.jami.Database.Config.ConfigRecord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandsAdmin {
  public static void adminCommands(MessageReceivedEvent event, String command) {
    List<String> args;
    if (event != null) {
      String message = event.getMessage().getContentRaw();
      message = message.replaceFirst("a!", "");
      args = new ArrayList<>(Arrays.asList(message.split(" ")));
    } else {
      args = new ArrayList<>(Arrays.asList(command.split(" ")));
    }

    String response = "";

    switch (args.get(0)) {
      case "set-config":
        response = setConfig(args);
        break;
      case "load-config":
        response = loadConfig(args);
        break;
      case "save-config":
        response = saveConfig();
        break;
      case "show-config":
        if (event == null) {
          response = showConfigConsole();
          break;
        }
        event.getMessage().replyEmbeds(showConfig().build()).queue();
        break;
      case "set-config-name":
        response = setConfigName(args);
        break;
      case "set-bot-status":
        response = setBotStatus(args);
        break;
      case "set-bot-color":
        response = setBotColor(args);
        break;
      case "set-exp-increment":
        response = setExpIncrement(args);
        break;
      case "set-exp-variation":
        response = setExpVariation(args);
        break;
      case "set-exp-cooldown":
        response = setExpCooldown(args);
        break;
      case "set-level-base":
        response = setLevelBase(args);
        break;
      case "set-level-growth":
        response = setLevelGrowth(args);
        break;
      case "add-disabled-feature":
        response = addDisabledFeature(args);
        break;
      case "remove-disabled-feature":
        response = removeDisabledFeature(args);
        break;
      case "add-disabled-command":
        response = addDisabledCommand(args);
        break;
      case "remove-disabled-command":
        response = removeDisabledCommand(args);
        break;
      case "add-admin":
        response = addAdmin(args);
        break;
      case "remove-admin":
        response = removeAdmin(args);
        break;
      case "parse-wiktionary":
        response = "Parsing wiktionary...";
        App.parseWiktionary();
        break;
      default:
        response = "**Unknown Command:** available options - set-config, load-config, save-config, " +
            "show-config, set-config-name, set-bot-status, set-bot-color, set-exp-increment, set-exp-variation, " +
            "set-exp-cooldown, set-level-base, set-level-growth, add-disabled-feature, " +
            "remove-disabled-feature, add-disabled-command, remove-disabled-command, add-admin, remove-admin";
        break;

    }
    if (event == null) {
      System.out.println("[TRASH] " + response);
      return;
    }

    event.getMessage().reply(response).queue();
  }

  private static String setConfig(List<String> args) {
    if (args.size() == 1) {
      return "**Command Usage:** a!set-config <configName>";
    }
    App.getProps().setProperty("CURRENT_getGlobalConfig()", args.get(1));
    App.saveProps();
    App.setGlobalConfig(new ConfigRecord(args.get(1)));
    return "Config set to " + args.get(1);
  }

  private static String loadConfig(List<String> args) {
    if (args.size() == 1) {
      return "**Command Usage:** a!load-config <configName>";
    }
    App.setGlobalConfig(new ConfigRecord(args.get(1)));
    return "Config " + args.get(1) + " loaded";
  }

  private static String saveConfig() {
    App.getGlobalConfig().saveConfig();
    return "Config " + App.getGlobalConfig().getConfigName() + " saved";
  }

  private static EmbedBuilder showConfig() {
    String disabledFeatures = "";
    for (Feature feature : App.getGlobalConfig().getDisabledFeatures()) {
      disabledFeatures += "- " + String.valueOf(feature) + "\n";
    }
    String disabledCommands = "";
    for (String command : App.getGlobalConfig().getDisabledCommands()) {
      disabledCommands += "- " + command + "\n";
    }
    String adminIds = "";
    for (long id : App.getGlobalConfig().getAdminIds()) {
      adminIds += "- " + String.valueOf(id) + "\n";
    }
    return new EmbedBuilder()
        .setTitle(App.getGlobalConfig().getConfigName())
        .setDescription("Status: " + App.getGlobalConfig().getBotStatus() + "\n" +
            "Color: " + App.getGlobalConfig().getBotColor() + "\n" +
            "Exp Increment: " + App.getGlobalConfig().getExpIncrement() + "\n" +
            "Exp Variation: " + App.getGlobalConfig().getExpVariation() + "\n" +
            "Exp Cooldown: " + App.getGlobalConfig().getExpCooldown() + "\n" +
            "Level Base: " + App.getGlobalConfig().getLevelBase() + "\n" +
            "Level Growth: " + App.getGlobalConfig().getLevelGrowth() + "\n" +
            "Disabled Features: \n" + disabledFeatures + "\n" +
            "Disabled Commands: \n" + disabledCommands + "\n" +
            "Admin IDs: \n" + adminIds);
  }

  private static String showConfigConsole() {
    String disabledFeatures = "";
    for (Feature feature : App.getGlobalConfig().getDisabledFeatures()) {
      disabledFeatures += "  - " + String.valueOf(feature) + "\n";
    }
    String disabledCommands = "";
    for (String command : App.getGlobalConfig().getDisabledCommands()) {
      disabledCommands += "  - " + command + "\n";
    }
    String adminIds = "";
    for (long id : App.getGlobalConfig().getAdminIds()) {
      adminIds += "  - " + String.valueOf(id) + "\n";
    }
    return App.getGlobalConfig().getConfigName() + ":\n" +
        "Status: " + App.getGlobalConfig().getBotStatus() + "\n" +
        "Color: " + App.getGlobalConfig().getBotColor() + "\n" +
        "Exp Increment: " + App.getGlobalConfig().getExpIncrement() + "\n" +
        "Exp Variation: " + App.getGlobalConfig().getExpVariation() + "\n" +
        "Exp Cooldown: " + App.getGlobalConfig().getExpCooldown() + "\n" +
        "Level Base: " + App.getGlobalConfig().getLevelBase() + "\n" +
        "Level Growth: " + App.getGlobalConfig().getLevelGrowth() + "\n" +
        "Disabled Features: \n" + disabledFeatures + "\n" +
        "Disabled Commands: \n" + disabledCommands + "\n" +
        "Admin IDs: \n" + adminIds;
  }

  private static String setConfigName(List<String> args) {
    if (args.size() == 1) {
      return "**Command Usage:** a!set-config-name <configName>";
    }
    App.getGlobalConfig().setConfigName(args.get(1));
    return "Config name set to " + args.get(1);
  }

  private static String setBotStatus(List<String> args) {
    if (args.size() == 1) {
      return "**Command Usage:** a!set-bot-status <status>";
    }
    String status = "";
    for (int i = 1; i < args.size(); i++) {
      status += args.get(i) + " ";
    }
    final String finalStatus = status;
    App.getGlobalConfig().setBotStatus(status);
    try {
      App.getShardManager().getShards()
          .forEach(jda -> jda.getPresence().setActivity(Activity.customStatus(finalStatus)));
    } catch (Exception e) {
      System.out.println(e);
    }
    return "Status set to " + status;
  }

  private static String setBotColor(List<String> args) {
    if (args.size() == 1) {
      return "**Command Usage:** a!set-bot-color <#color>";
    }
    String color = args.get(1).replaceFirst("#", "");
    App.getGlobalConfig().setBotColor(color);
    return "Color set to" + color;
  }

  private static String setExpIncrement(List<String> args) {
    if (args.size() == 1) {
      return "**Command Usage:** a!set-exp-increment <exp>";
    }
    try {
      App.getGlobalConfig().setExpIncrement(Integer.valueOf(args.get(1)));
      return "Exp Increment set to " + args.get(1);
    } catch (Exception e) {
      return "**Command Usage:** a!set-exp-increment <exp>";
    }
  }

  private static String setExpVariation(List<String> args) {
    if (args.size() == 1) {
      return "**Command Usage:** a!set-exp-variation <variation>";
    }
    try {
      App.getGlobalConfig().setExpVariation(Integer.valueOf(args.get(1)));
      return "Exp Variation set to ";
    } catch (Exception e) {
      return "**Command Usage:** a!set-exp-variation <variation>";
    }
  }

  private static String setExpCooldown(List<String> args) {
    if (args.size() == 1) {
      return "**Command Usage:** a!set-exp-cooldown <cooldown>";
    }
    try {
      App.getGlobalConfig().setExpCooldown(Long.valueOf(args.get(1)));
      return "Exp Cooldown set to " + args.get(1);
    } catch (Exception e) {
      return "**Command Usage:** a!set-exp-cooldown <cooldown>";
    }
  }

  private static String setLevelBase(List<String> args) {
    if (args.size() == 1) {
      return "**Command Usage:** a!set-level-base <exp>";
    }
    try {
      App.getGlobalConfig().setLevelBase(Long.valueOf(args.get(1)));
      return "Level Base set to " + args.get(1);
    } catch (Exception e) {
      return "**Command Usage:** a!set-level-base <exp>";
    }
  }

  private static String setLevelGrowth(List<String> args) {
    if (args.size() == 1) {
      return "**Command Usage:** a!set-level-growth <growth>";
    }
    try {
      App.getGlobalConfig().setLevelGrowth(Double.valueOf(args.get(1)));
      return "Level Growth set to " + args.get(1);
    } catch (Exception e) {
      return "**Command Usage:** a!set-level-growth <growth>";
    }
  }

  private static String addDisabledFeature(List<String> args) {
    if (args.size() == 1) {
      return "**Command Usage:** a!add-disabled-feature <feature> [features...]";
    }
    String addedFeatures = "Following features added to Disabled Features: ";
    for (int i = 1; i < args.size(); i++) {
      App.getGlobalConfig().addDisabledFeature(Feature.valueOf(args.get(i)));
      addedFeatures += args.get(i) + ", ";
    }
    return addedFeatures;
  }

  private static String removeDisabledFeature(List<String> args) {
    if (args.size() == 1) {
      return "**Command Usage:** a!remove-disabled-feature <feature> [features...]";
    }
    String removedFeatures = "Following features removed from Disabled Features: ";
    for (int i = 1; i < args.size(); i++) {
      App.getGlobalConfig().removeDisabledFeature(Feature.valueOf(args.get(i)));
      removedFeatures += args.get(i) + ", ";
    }
    return removedFeatures;
  }

  private static String addDisabledCommand(List<String> args) {
    if (args.size() == 1) {
      return "**Command Usage:** a!add-disabled-command <command> [commands...]";
    }
    String addedCommands = "Following commands added from Disabled Commands: ";
    for (int i = 1; i < args.size(); i++) {
      App.getGlobalConfig().addDisabledCommand(args.get(i));
      addedCommands += args.get(i) + ", ";
    }
    return addedCommands;
  }

  private static String removeDisabledCommand(List<String> args) {
    if (args.size() == 1) {
      return "**Command Usage:** a!remove-disabled-command <command> [commands...]";
    }
    String removedCommands = "Following commands removed from Disabled Commands: ";
    for (int i = 1; i < args.size(); i++) {
      App.getGlobalConfig().removeDisabledCommand(args.get(i));
      removedCommands += args.get(i) + ", ";
    }
    return removedCommands;
  }

  private static String addAdmin(List<String> args) {
    if (args.size() == 1) {
      return "**Command Usage:** a!add-admin <userId> [userIds...]";
    }
    boolean addError = false;
    String addErrorString = "Error adding following IDs - maybe they aren't IDs: ";
    String addedIds = "Following IDs added to Admin IDs: ";
    for (int i = 1; i < args.size(); i++) {
      try {
        App.getGlobalConfig().addAdminId(Long.valueOf(args.get(i)));
        addedIds += args.get(i) + ", ";
      } catch (Exception e) {
        addError = true;
        addErrorString += args.get(i) + ", ";
      }
    }
    if (addError) {
      return addErrorString;
    }
    return addedIds;
  }

  private static String removeAdmin(List<String> args) {
    if (args.size() == 1) {
      return "Command Usage: a!remove-admin <userId> [userIds...]";
    }
    boolean error = false;
    String errorString = "Error removing following IDs - maybe they aren't IDs: ";
    String removedIds = "Following IDs removed from Admin IDs: ";
    for (int i = 1; i < args.size(); i++) {
      try {
        App.getGlobalConfig().removeAdminId(Long.valueOf(args.get(i)));
        removedIds += args.get(i) + ", ";
      } catch (Exception e) {
        error = true;
        errorString += args.get(i) + ", ";
      }
    }
    if (error) {
      return errorString;
    }
    return removedIds;
  }
}
