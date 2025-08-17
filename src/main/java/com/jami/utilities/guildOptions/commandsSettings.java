package com.jami.utilities.guildOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bson.Document;

import com.jami.App;
import com.jami.database.Feature;
import com.jami.database.LogType;
import com.jami.database.guild.guild;
import com.jami.database.guild.guildSettings.guildSettings;
import com.jami.database.guild.guildSettings.loggingChannels.loggingChannel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu.SelectTarget;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class commandsSettings {

  guild g;
  guildSettings gs;
  SlashCommandInteractionEvent event;
  StringSelectInteractionEvent currentSelectionEvent;

  @Deprecated
  ButtonInteractionEvent currentButtonEvent;

  public commandsSettings(SlashCommandInteractionEvent event) {
    this.g = new guild(event.getGuild().getIdLong());
    this.gs = g.getSettings();
    this.event = event;
  }

  public void start() {
    event.deferReply().queue();
    generateSettingsPage();
    generateSelectMenu();
  }

  public void generateSettingsPage() {
    String loggingChannels = "**__Logging Channels__**\n";
    for (loggingChannel lc : gs.getLoggingChannels()) {
      loggingChannels += String.format("- %s\n",
          event.getJDA().getChannelById(TextChannel.class, lc.getLogChannel()).getAsMention());
      for (LogType type : lc.getAssociatedLogs()) {
        loggingChannels += String.format("  - %s\n", type.toString());
      }
    }

    String levellingRoles = "**__Levelling Roles__**\n";
    for (Document doc : gs.getLevellingRoles()) {
      levellingRoles += String.format("- Level %d: %s\n", doc.getInteger("levelRequirement"),
          event.getJDA().getRoleById(doc.getLong("roleId")).getAsMention());
    }

    String disabledFeatures = "**__Disabled Features__**\n";
    for (Feature feature : Feature.values()) {
      if (gs.getDisabledFeatures().contains(feature)) {
        disabledFeatures += String.format("%s, ", String.valueOf(feature));
      }
      disabledFeatures = disabledFeatures.substring(0, disabledFeatures.length() - 2);
    }

    String wordsDisabledChannels = "**__Words Disabled Channels__**\n";
    for (long channel : gs.getWordsDisabledChannels()) {
      wordsDisabledChannels += String.format("%s, ", event.getJDA().getChannelById(TextChannel.class, channel));
    }
    wordsDisabledChannels = disabledFeatures.substring(0, disabledFeatures.length() - 2);

    String settings = String.format("Exp Increment: %d\n" +
        "Exp Variation: %d\n" +
        "Exp Cooldown: %ds\n\n" +
        "Level Base: %d\n" +
        "Level Growth: %f\n\n" +
        "%s\n%s\n%s\n%s", gs.getExpIncrement(), gs.getExpVariation(), gs.getExpCooldown(), gs.getLevelBase(),
        gs.getLevelGrowth(), loggingChannels, levellingRoles, disabledFeatures, wordsDisabledChannels);

    EmbedBuilder embed = new EmbedBuilder()
        .setTitle(String.format("**%s's Guild Settings**", event.getGuild().getName()))
        .setDescription(settings)
        .setFooter("Pick a setting to change below:");

    event.getHook().editOriginalEmbeds(embed.build()).queue();
  }

  public void generateSelectMenu() {
    StringSelectMenu.Builder menu = StringSelectMenu.create("trash_guild_options")
        .addOption("Exp Increment", OptionType.EXP_INCREMENT.toString(), "How much exp to reward per message")
        .addOption("Exp Variation", OptionType.EXP_VARIATION.toString(), "How much to vary exp rewards by")
        .addOption("Exp Cooldown", OptionType.EXP_COOLDOWN.toString(),
            "How long to wait between messages to reward exp")
        .addOption("Level Base", OptionType.LEVEL_BASE.toString(), "How much exp to start levelling members")
        .addOption("Level Growth", OptionType.LEVEL_GROWTH.toString(), "How much to increase exp requirement per level")
        .addOption("Levelling Roles", OptionType.LEVELLING_ROLE.toString(),
            "Roles to reward members with upon reaching certain levels")
        .addOption("Logging Channels", OptionType.LOGGING_CHANNEL.toString(), "Where to send logs")
        .addOption("Disabled Features", OptionType.DISABLED_FEATURE.toString(), "Features you dont wan't users to use")
        .addOption("Words Disabled Channels", OptionType.WORDS_DISABLED_CHANNEL.toString(),
            "Channels you don't want word usage logged in");

    event.getHook().editOriginalComponents(ActionRow.of(menu.build()))
        .queue(message -> App.getEventWaiter().waitForEvent(StringSelectInteractionEvent.class,
            e -> {
              if (!e.getUser().equals(event.getUser())) {
                return false;
              }
              if (!e.getComponentId().equals("trash_guild_options")) {
                return false;
              }
              return !e.isAcknowledged();
            },
            e -> {
              OptionType type = OptionType.valueOf(e.getValues().get(0));
              this.currentSelectionEvent = e;
              switch (type) {
                case EXP_INCREMENT:
                  sendModal(OptionType.EXP_INCREMENT, "Exp Increment", "Whole Numbers Only");
                  break;
                case EXP_VARIATION:
                  sendModal(OptionType.EXP_VARIATION, "Exp Variation", "Whole Numbers Only");
                  break;
                case EXP_COOLDOWN:
                  sendModal(OptionType.EXP_COOLDOWN, "Exp Cooldown", "Whole Numbers Only");
                  break;
                case LEVEL_BASE:
                  sendModal(OptionType.LEVEL_BASE, "Level Base", "Whole Numbers Only");
                  break;
                case LEVEL_GROWTH:
                  sendModal(OptionType.LEVEL_GROWTH, "Level Growth", "Whole or Decimal Numbers Only");
                  break;
                case LEVELLING_ROLE:
                  levellingRoles();
                  break;
                case LOGGING_CHANNEL:
                  loggingChannels();
                  break;
                case DISABLED_FEATURE:
                  disabledFeatures();
                  break;
                case WORDS_DISABLED_CHANNEL:
                  wordsDisabledChannels();
                  break;
              }
              generateSelectMenu();
            },
            5, TimeUnit.MINUTES,
            () -> message.editMessageComponents(ActionRow.of(menu.setDisabled(true).build())).queue()));
  }

  public void sendModal(OptionType option, String optionString, String placeholder) {
    TextInput input = TextInput.create(option.toString(), optionString, TextInputStyle.SHORT)
        .setPlaceholder(placeholder)
        .setRequired(true)
        .build();
    Modal m = Modal.create(option.toString(), String.format("Set %s", optionString))
        .addComponents(ActionRow.of(input))
        .build();
    currentSelectionEvent.replyModal(m).queue(modal -> App.getEventWaiter().waitForEvent(ModalInteractionEvent.class,
        e -> {
          if (!e.getUser().equals(event.getUser())) {
            return false;
          }
          if (!e.getModalId().equals(option.toString())) {
            return false;
          }
          return !e.isAcknowledged();
        },
        e -> {
          try {
            Integer value = Integer.getInteger(e.getValue(option.toString()).getAsString());
            setOption(value, option);
            e.reply(String.format("%s set to %d", optionString, value)).setEphemeral(true).queue();
          } catch (Exception err) {
            e.reply("Option requires a whole number").setEphemeral(true).queue();
            System.out.println(err);
          }
        },
        2, TimeUnit.MINUTES,
        () -> {
        }));
  }

  public void setOption(Integer value, OptionType type) {
    switch (type) {
      case EXP_INCREMENT:
        gs.setExpIncrement(value);
        break;
      case EXP_VARIATION:
        gs.setExpVariation(value);
        break;
      case EXP_COOLDOWN:
        gs.setExpCooldown(value);
        break;
      case LEVEL_BASE:
        gs.setLevelBase(value);
        break;
      case LEVEL_GROWTH:
        gs.setLevelGrowth(value);
        break;
      default:
        event.getHook().sendMessage("Unknown error occured.").setEphemeral(true).queue();
        break;
    }
    g.commit();
  }

  public void loggingChannels() {
    Button remove = Button.danger("trash_log_remove", "REMOVE");
    Button add = Button.success("trash_log_add", "ADD");
    currentSelectionEvent.reply("Add or Remove a log").addComponents(ActionRow.of(remove, add))
        .queue(message -> App.getEventWaiter().waitForEvent(ButtonInteractionEvent.class,
            e -> {
              if (!e.getUser().equals(event.getUser())) {
                return false;
              }
              if (!(e.getComponentId().equals("trash_log_remove") || e.getComponentId().equals("trash_log_add"))) {
                return false;
              }
              return !e.isAcknowledged();
            },
            e -> {
              message.deleteOriginal().queue();
              if (e.getComponentId().equals("trash_log_remove")) {
                removeLoggingChannel();
              } else {
                addLoggingChannel(e);
              }
            },
            2, TimeUnit.MINUTES,
            () -> message.deleteOriginal().queue()));
  }

  public void addLoggingChannel(ButtonInteractionEvent be) {
    OptionType type = OptionType.LOGGING_CHANNEL;
    EntitySelectMenu menu = EntitySelectMenu.create(type.toString(), SelectTarget.CHANNEL).build();
    be.reply("Pick logging channel:").addComponents(ActionRow.of(menu))
        .queue(message -> App.getEventWaiter().waitForEvent(EntitySelectInteractionEvent.class,
            e -> {
              if (!e.getUser().equals(event.getUser())) {
                return false;
              }
              if (!e.getComponentId().equals(type.toString())) {
                return false;
              }
              return !e.isAcknowledged();
            },
            e -> {
              message.deleteOriginal().queue();
              if (!e.getMentions().getChannels().get(0).getType().equals(ChannelType.TEXT)) {
                e.reply("Channel must be a text channel").setEphemeral(true).queue();
              } else {
                pickLogType(e);
              }
            },
            2, TimeUnit.MINUTES,
            () -> message.deleteOriginal().queue()));
  }

  public void pickLogType(EntitySelectInteractionEvent me) {
    OptionType type = OptionType.LOGGING_CHANNEL;
    TextChannel channel = (TextChannel) me.getMentions().getChannels().get(0);
    StringSelectMenu.Builder menu = StringSelectMenu.create(type.toString()).setMaxValues(10);
    for (LogType log : LogType.values()) {
      menu.addOption(log.toString(), log.toString());
    }
    me.reply("Select logs to associate with " + channel.getAsMention()).addComponents(ActionRow.of(menu.build()))
        .queue(message -> App.getEventWaiter().waitForEvent(StringSelectInteractionEvent.class,
            e -> {
              if (!e.getUser().equals(event.getUser())) {
                return false;
              }
              if (!e.getComponentId().equals(type.toString())) {
                return false;
              }
              return !e.isAcknowledged();
            },
            e -> {
              List<LogType> logs = new ArrayList<>();
              String confirm = "";
              for (String value : e.getValues()) {
                logs.add(LogType.valueOf(value));
                confirm += value + ", ";
              }
              confirm = confirm.substring(0, confirm.length() - 2);
              gs.addLoggingChannel(new loggingChannel(channel.getIdLong(), logs));
              g.commit();
              e.reply("Added " + confirm + " log(s) to " + channel.getAsMention()).queue();
            },
            2, TimeUnit.MINUTES,
            () -> message.deleteOriginal().queue()));
  }

  public void removeLoggingChannel() {

  }

  public void levellingRoles() {

  }

  public void addLevellingRole() {

  }

  public void removeLevellingRole() {

  }

  public void disabledFeatures() {

  }

  public void addDisabledFeature() {

  }

  public void removeDisabledFeature() {

  }

  public void wordsDisabledChannels() {

  }

  public void addWordsDisabledChannel() {

  }

  public void removeWordsDisabledChannel() {

  }
}
