package com.jami.Database.Guild;

import com.jami.Database.Guild.Bracket.Bracket;
import com.jami.Database.Guild.guildSettings.*;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class GuildRecord {

  private long guildId;
  private long firstJoined;
  private GuildSettings guildSettings;
  private List<Bracket> brackets;

  public GuildRecord(Document doc) {
    this.guildId = doc.getLong("_id");
    this.firstJoined = doc.getLong("firstJoined");
    this.guildSettings = new GuildSettings(doc.get("guildSettings", Document.class));
    for (Document b : doc.getList("brackets", Document.class)) {
      this.brackets.add(new Bracket(b));
    }
  }

  public GuildRecord(long guildId) {
    this.guildId = guildId;
    this.firstJoined = System.currentTimeMillis();
    this.guildSettings = new GuildSettings(new Document());
    this.brackets = new ArrayList<>();
  }

  public long getGuildId() {
    return guildId;
  }

  public long getFirstJoined() {
    return firstJoined;
  }

  public GuildSettings getSettings() {
    return guildSettings;
  }

  public void addBracket(Bracket bracket) {
    this.brackets.add(bracket);
  }

  public void removeBracket(Bracket bracket) {
    this.brackets.remove(bracket);
  }

  public List<Bracket> getBrackets() {
    return brackets;
  }

  public Document toDocument() {
    List<Document> bs = new ArrayList<>();
    for (Bracket b : brackets) {
      bs.add(b.toDocument());
    }
    return new Document()
        .append("_id", guildId)
        .append("firstJoined", firstJoined)
        .append("guildSettings", guildSettings.toDocument())
        .append("brackets", bs);
  }

}
