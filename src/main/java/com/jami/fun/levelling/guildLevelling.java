package com.jami.fun.levelling;

import java.awt.Color;
import java.util.Random;

import com.jami.database.guild.guild;
import com.jami.database.guild.guildUser;
import com.jami.database.guild.guildSettings.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class guildLevelling {

  private guildUser gu;

  private long userExp;
  private int userLevel;

  private int expInc;
  private int expVar;
  private long expCooldown;
  private long levelBase;
  private double levelGrowth;

  private long userLastMessage;

  private MessageReceivedEvent event;

  public guildLevelling(MessageReceivedEvent event, guild g) {
    long userId = event.getAuthor().getIdLong();

    guildSettings gs = g.getSettings();
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
