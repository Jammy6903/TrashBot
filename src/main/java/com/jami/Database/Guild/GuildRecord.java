package com.jami.Database.Guild;

import com.jami.Database.Guild.fun.Counting;
import com.jami.Database.Guild.guildSettings.*;
import com.jami.Database.Guild.utilities.brackets.Bracket;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class GuildRecord {

  @BsonId
  private long guildId;

  @BsonProperty("guildSettings")
  private GuildSettings guildSettings = new GuildSettings();

  @BsonProperty("counting")
  private Counting counting = new Counting();

  @BsonProperty("brackets")
  private List<Bracket> brackets = new ArrayList<>();

  @BsonProperty("dateCreated")
  private long dateCreated;

  @BsonExtraElements
  private Document legacyValues;

  public GuildRecord() {
  }

  // GuildId

  public long getGuildId() {
    return guildId;
  }

  // GuildSettings

  public GuildSettings getSettings() {
    return guildSettings;
  }

  public void setSettings(GuildSettings settings) {
    this.guildSettings = settings;
  }

  // Counting

  public Counting getCounting() {
    return counting;
  }

  public void setCounting(Counting counting) {
    this.counting = counting;
  }

  // Brackets

  public void addBracket(Bracket bracket) {
    this.brackets.add(bracket);
  }

  public void removeBracket(Bracket bracket) {
    this.brackets.remove(bracket);
  }

  public List<Bracket> getBrackets() {
    return brackets;
  }

  public void setBrackets(List<Bracket> brackets) {
    this.brackets = brackets;
  }

  // FirstJoined

  public long getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(long dateCreated) {
    this.dateCreated = dateCreated;
  }

}
