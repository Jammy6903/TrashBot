package com.jami.Database.Guild;

import com.jami.Database.Guild.Bracket.Bracket;
import com.jami.Database.Guild.guildSettings.*;

import java.util.List;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class GuildRecord {

  @BsonId
  private long guildId;

  @BsonProperty("guildSettings")
  private GuildSettings guildSettings = new GuildSettings();

  @BsonProperty("brackets")
  private List<Bracket> brackets;

  @BsonProperty("dateCreated")
  private long dateCreated;

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

  // FirstJoined

  public long getDateCreated() {
    return dateCreated;
  }

}
