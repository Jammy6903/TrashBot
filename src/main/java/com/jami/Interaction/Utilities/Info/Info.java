package com.jami.Interaction.Utilities.Info;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.jami.App;
import com.jami.Database.Guild.GuildRecord;
import com.jami.JDA.JDATools;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Info {

    private SlashCommandInteractionEvent event;

    public Info(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    public void bot() {
        Date firstJoined = new Date(new GuildRecord(event.getGuild().getIdLong()).getFirstJoined());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("**TRASH BOT**")
                .setDescription(
                        "Trash bot is a multi-purpose Discord bot designed for the twenty one pilots Manchester Discord server\n\n/featurerequest to request new features!\n/help for help!\n\n[Support Discord](https://discord.gg/ZUcg6ArVzp)")
                .addField("Member since", df.format(firstJoined), true)
                .addField("Shard Number", String.valueOf(event.getJDA().getShardInfo().getShardId()), true)
                .addField("Total Guilds", String.valueOf(JDATools.totalGuildCount()), true)
                .setThumbnail(
                        "https://cdn.discordapp.com/avatars/1390058332689137847/878371b41485c8556b90511d612334d9.png?size=1024")
                .setFooter("Made with ❤️ by JamiTheFox",
                        App.getShardManager().getUserById(190855924368277508L).getAvatarUrl());

        event.replyEmbeds(embed.build()).queue();
    }

    public void guild() {
        Guild g = event.getGuild();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("**" + g.getName() + " Guild Info**")
                .setDescription(g.getDescription())
                .setThumbnail(g.getIconUrl())
                .addField("Member Count", String.valueOf(g.getMemberCount()), false)
                .addField("Role Count", String.valueOf(g.getRoles().size()), false)
                .addField("Total Channels", String.valueOf(g.getChannels().size()), true)
                .addField("Channels (exc. Categories)",
                        String.valueOf(g.getChannels().size() - g.getCategories().size()), true)
                .addField("", "", true)
                .addField("Text Channels", String.valueOf(g.getTextChannels().size()), true)
                .addField("Voice Channels", String.valueOf(g.getVoiceChannels().size()), true)
                .addField("Categories", String.valueOf(g.getCategories().size()), true)
                .setFooter("Guild ID: " + g.getIdLong());

        event.replyEmbeds(embed.build()).queue();
    }

    public void member(Member m) {

        List<Role> roles = m.getRoles();
        String roleString = "__Roles:__ *(" + roles.size() + ")*\n";
        for (Role r : roles) {
            roleString += r.getAsMention() + "\n";
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

        String title = "";

        String userName = m.getUser().getGlobalName();

        if (m.getNickname() == null) {
            title = "**" + userName + " Info**";
        } else {
            title = String.format("**%s (%s) Info**", userName, m.getNickname());
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(title)
                .setDescription(roleString)
                .setThumbnail(m.getUser().getAvatarUrl())

                // to-do make these times a nice format and add (x days ago)
                .addField("User Since", m.getTimeCreated().format(dtf), true)
                .addField("Member Since", m.getTimeJoined().format(dtf), true)
                .setFooter("User ID: " + String.valueOf(m.getIdLong()));

        event.replyEmbeds(embed.build()).queue();
    }

    public void user(User u) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("**" + u.getName() + " Info**")
                .setDescription(" ")
                .setThumbnail(u.getAvatarUrl())
                .addField("User Since", u.getTimeCreated().toString(), false);

        event.replyEmbeds(embed.build()).queue();
    }

    public void role(Role r) {

    }

    public void channel(Channel c) {

    }
}
