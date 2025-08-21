package com.jami.Interaction.Fun.Levelling;

import java.awt.Color;
import java.util.Random;

import com.jami.Database.Guild.GuildRecord;
import com.jami.Database.Guild.GuildUserRecord;
import com.jami.Database.Guild.guildSettings.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GuildLevelling {

  private GuildUserRecord gu;

  private long userExp;
  private int userLevel;

  private int expInc;
  private int expVar;
  private long expCooldown;
  private long levelBase;
  private double levelGrowth;

  private long userLastMessage;

  private MessageReceivedEvent event;

  public GuildLevelling(MessageReceivedEvent event, GuildRecord g) {
    long userId = event.getAuthor().getIdLong();

    GuildSettings gs = g.getSettings();
    this.gu = g.getUser(userId);

    this.userExp = gu.getExp();
    this.userLevel = gu.getLevel();
    this.userLastMessage = gu.getUserLastMessage();

    this.expInc = gs.getExpIncrement();
    this.expVar = gs.getExpVariation();
    this.expCooldown = gs.getExpCooldown();
    this.levelBase = gs.getLevelBase();
    this.levelGrowth = gs.getLevelGrowth();

    this.event = event;
  }

  public boolean incrementExp() {

    if (System.currentTimeMillis() - userLastMessage <= expCooldown * 1000) {
      return false;
    }

    userExp = userExp + new Random().nextLong((expInc + expVar) - (expInc - expVar) + 1) + (expInc - expVar);

    gu.setExp(userExp);

    gu.setUserLastMessage(System.currentTimeMillis());

    if (userExp >= gu.getRequiredExp(levelBase, levelGrowth)) {
      userLevel++;
      gu.setLevel(userLevel);
      return true;
    }
    return false;
  }

  public void announceLevelUp() {
    String message = String.format("You've reached Level %d!\nYou need %d more exp to reach level %d.",
        userLevel, gu.getRequiredExp(levelBase, levelGrowth) - userExp, userLevel + 1);
    EmbedBuilder embed = new EmbedBuilder()
        .setTitle("**LEVEL UP!**")
        .setDescription(message)
        .setColor(Color.CYAN);
    event.getMessage().replyEmbeds(embed.build());
  }
}
