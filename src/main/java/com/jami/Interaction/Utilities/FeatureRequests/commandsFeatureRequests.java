package com.jami.Interaction.Utilities.FeatureRequests;

import java.util.concurrent.TimeUnit;

import com.jami.App;
import com.jami.Database.infrastructure.mongo.MongoFeatureRequestRepo;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class commandsFeatureRequests {
        public static void newFeatureRequest(SlashCommandInteractionEvent event) {
                TextInput title = TextInput.create("title", "Title", TextInputStyle.SHORT)
                                .setPlaceholder("My feature request")
                                .setRequired(true)
                                .build();

                TextInput description = TextInput.create("description", "Description", TextInputStyle.PARAGRAPH)
                                .setRequired(true)
                                .build();

                Modal modal = Modal.create("featureRequestModal", "Feature Request")
                                .addComponents(ActionRow.of(title), ActionRow.of(description))
                                .build();

                event.replyModal(modal).queue(message -> App.getEventWaiter().waitForEvent(ModalInteractionEvent.class,
                                e -> {
                                        if (e.getUser() != event.getUser()) {
                                                return false;
                                        }
                                        return !e.isAcknowledged();
                                },
                                e -> {
                                        String t = e.getValue("title").getAsString();
                                        String d = e.getValue("description").getAsString();
                                        String un = e.getUser().getName();
                                        long ui = e.getUser().getIdLong();
                                        String reqId = MongoFeatureRequestRepo.createRequest(
                                                        t, d, un, ui);
                                        EmbedBuilder embed = new EmbedBuilder()
                                                        .setTitle(t)
                                                        .setDescription(d)
                                                        .addField("Status", "REQUESTED", false)
                                                        .setFooter(String.format("Requested by: %s (%d) | ID: %s", un,
                                                                        ui, reqId));
                                        e.reply("Feature request created:").addEmbeds(embed.build()).queue();
                                },
                                10, TimeUnit.MINUTES,
                                () -> {
                                }));
        }
}
