package com.jami.Database.Guild.guildSettings.LevellingRoles;

import org.bson.Document;

public class LevellingRole {
  private long id;
  private int levelRequirement;

  public LevellingRole(long roleId, int levelRequirement) {
    this.id = roleId;
    this.levelRequirement = levelRequirement;
  }

  public LevellingRole(Document doc) {
    this.id = doc.getLong("_id");
    this.levelRequirement = doc.getInteger("levelRequirement");
  }

  public long getId() {
    return id;
  }

  public void setLevelRequirement(int requirement) {
    this.levelRequirement = requirement;
  }

  public int getLevelRequirement() {
    return levelRequirement;
  }

  public Document toDocument() {
    return new Document()
        .append("_id", id)
        .append("levelRequirement", levelRequirement);
  }
}
