package com.jami.fun.levelling;

import java.util.Random;

import com.jami.database.guild.guild;
import com.jami.database.guild.guildUser;
import com.jami.database.guild.guildSettings.*;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class guildLevelling {

  private guild g;
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

  public guildLevelling(MessageReceivedEvent event) {
    long guildId = event.getGuild().getIdLong();
    long userId = event.getAuthor().getIdLong();

    this.g = new guild(guildId);
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

    // Check if cooldown time has elapsed since users last message
    if (System.currentTimeMillis() - userLastMessage <= expCooldown * 1000) {
      return false;
    }

    // Increment exp by guild values, check for level up and increment level

    userExp = userExp + new Random().nextLong((expInc + expVar) - (expInc - expVar) + 1) + (expInc - expVar);

    gu.setExp(userExp);

    // Set last message to current millis
    gu.setUserLastMessage(System.currentTimeMillis());

    if (userExp >= levelRequirement()) {
      userLevel++;
      gu.setLevel(userLevel);
      return true;
    }
    return false;
  }

  private double levelRequirement() {
    return Math.floor(levelBase * Math.pow(userLevel + 1, levelGrowth));
  }

  public void announceLevelUp() {
    String message = String.format("**LEVEL UP!**\nYou've reached Level %d, you need %d more exp to reach level %d",
        userLevel, (long) levelRequirement() - userExp, userLevel + 1);
    event.getChannel().sendMessage(message).queue();
  }

  public void commit() {
    g.commit();
  }
}
