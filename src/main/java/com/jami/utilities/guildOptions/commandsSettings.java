package com.jami.utilities.guildOptions;

import java.util.concurrent.TimeUnit;

import org.bson.Document;

import com.jami.App;
import com.jami.database.guild.guild;
import com.jami.database.guild.guildSettings.guildSettings;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class commandsSettings {

  private static String[] features = { "levelling", "words", "logging" };
  private static String[] logs = { "message", "member", "voice", "moderation", "guild" };

  guild g;
  guildSettings gs;
  SlashCommandInteractionEvent event;
  ButtonInteractionEvent currentButtonEvent;

  public commandsSettings(SlashCommandInteractionEvent event) {
    this.g = new guild(event.getGuild().getIdLong());
    this.gs = g.getSettings();
    this.event = event;
  }

  public void start() {
    event.deferReply().queue();
    generateSettingsPage();
    generateButtons();
  }

  public void generateSettingsPage() {
    String loggingChannels = "**__Logging Channels__**\n";
    for (String log : logs) {
      for (Document doc : gs.getLoggingChannels()) {
        if (doc.getString("logType") == log) {
          loggingChannels += String.format("%s: %s\n", log,
              event.getJDA().getChannelById(TextChannel.class, doc.getLong("chanelId")).getAsMention());
          break;
        }
      }
      loggingChannels += String.format("%s: %s\n", log, "N/A");
    }

    String levellingRoles = "**__Levelling Roles__**\n";
    for (Document doc : gs.getLevellingRoles()) {
      levellingRoles += String.format("Level %d: %s\n", doc.getInteger("levelRequirement"),
          event.getJDA().getRoleById(doc.getLong("roleId")).getAsMention());
    }

    String disabledFeatures = "**__Disabled Features__**\n";
    for (String feature : features) {
      if (gs.getDisabledFeatures().contains(feature)) {
        disabledFeatures += String.format("%s, ", feature);
      } else {
        disabledFeatures += String.format("**%s**, ", feature);
      }
    }

    String wordsDisabledChannels = "**__Words Disabled Channels__**\n";
    for (long channel : gs.getWordsDisabledChannels()) {
      wordsDisabledChannels += String.format("%s, ", event.getJDA().getChannelById(TextChannel.class, channel));
    }

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

  public void generateButtons() {
    Button expIncBut = Button.secondary("exp-inc", "Exp Increment");
    Button expVarBut = Button.secondary("exp-var", "Exp Variation");
    Button expCoolBut = Button.secondary("exp-cool", "Exp Cooldown");

    ActionRow expRow = ActionRow.of(expIncBut, expVarBut, expCoolBut);

    Button levelBaseBut = Button.secondary("level-base", "Level Base");
    Button levelGrowthBut = Button.secondary("level-growth", "Level Growth");

    ActionRow levelRow = ActionRow.of(levelBaseBut, levelGrowthBut);

    Button loggingChannelBut = Button.secondary("logging-channels", "Logging Channels");
    Button levellingRoleBut = Button.secondary("levelling-roles", "Levelling Roles");

    Button disabledFeaturesBut = Button.secondary("disabled-features", "Disabled Features");
    Button wordsDisabledChannelsBut = Button.secondary("words-disabled-channels", "Words Disabled Channels");

    ActionRow otherRowOne = ActionRow.of(loggingChannelBut, levellingRoleBut);
    ActionRow otherRowTwo = ActionRow.of(disabledFeaturesBut, wordsDisabledChannelsBut);

    event.getHook().editOriginalComponents(expRow, levelRow, otherRowOne, otherRowTwo)
        .queue(message -> App.getEventWaiter().waitForEvent(ButtonInteractionEvent.class,
            e -> e.getUser().equals(event.getUser()),
            e -> {
              e.deferReply();
              this.currentButtonEvent = e;
              switch (e.getComponentId()) {
                case "exp-inc":
                  setExpInc();
                  break;
                case "exp-var":
                  setExpVar();
                  break;
                case "exp-cool":
                  setExpCool();
                  break;
                case "level-base":
                  setLevelBase();
                  break;
                case "level-growth":
                  setLevelGrowth();
                  break;
                case "logging-channels":
                  loggingChannels();
                  break;
                case "levelling-roles":
                  levellingRoles();
                  break;
                case "disabled-features":
                  disabledFeatures();
                  break;
                case "words-disabled-channels":
                  wordsDisabledChannels();
                  break;
              }
              generateButtons();
            },
            5, TimeUnit.MINUTES,
            () -> disableButtons()));

  }

  public void disableButtons() {
    event.getHook().editOriginalComponents().queue();
  }

  public void setExpInc() {
    TextInput exp = TextInput.create("exp-inc", "Exp Increment", TextInputStyle.SHORT)
        .setPlaceholder("Numbers only")
        .setRequired(true)
        .build();
    Modal m = Modal.create("exp-inc", "Set Exp Increment")
        .addComponents(ActionRow.of(exp))
        .build();
    currentButtonEvent.replyModal(m)
        .queue(message -> App.getEventWaiter().waitForEvent(ModalInteractionEvent.class,
            e -> e.getUser().equals(event.getUser()),
            e -> {
              try {
                int value = Integer.parseInt(e.getValue("exp-inc").getAsString());
                gs.setExpIncrement(value);
                g.commit();
                e.reply("Exp Increment set to " + value).queue();
                generateSettingsPage();
              } catch (Exception ex) {
                e.reply("Exp Increment must be a number").setEphemeral(true).queue();
              }
            },
            5, TimeUnit.MINUTES,
            () -> {
            }));
  }

  public void setExpVar() {

  }

  public void setExpCool() {

  }

  public void setLevelBase() {

  }

  public void setLevelGrowth() {

  }

  public void loggingChannels() {

  }

  public void addLoggingChannel() {

  }

  public void removeLoggingChanel() {

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
