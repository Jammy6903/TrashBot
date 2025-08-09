package com.jami.utilities.featureRequests;

import java.util.concurrent.TimeUnit;

import com.jami.App;
import com.jami.database.utilities.featureRequest;

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
                                        featureRequest req = new featureRequest(e.getValue("title").getAsString(),
                                                        e.getValue("description").getAsString(), e.getUser().getName(),
                                                        e.getUser().getIdLong());
                                        EmbedBuilder embed = new EmbedBuilder()
                                                        .setTitle(req.getTitle())
                                                        .setDescription(req.getDescription())
                                                        .addField("Status", req.getStatus(), false)
                                                        .setFooter("Requested by " + req.getUserName() + " | "
                                                                        + req.getId());
                                        e.reply("Feature request submitted:").queue();
                                        e.getHook().sendMessageEmbeds(embed.build()).queue();
                                        req.commit();
                                },
                                10, TimeUnit.MINUTES,
                                () -> {
                                }));
        }
}
