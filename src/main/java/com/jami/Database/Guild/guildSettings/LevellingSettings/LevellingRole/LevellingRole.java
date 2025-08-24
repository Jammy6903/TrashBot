package com.jami.Database.Guild.guildSettings.LevellingSettings.LevellingRole;

import org.bson.Document;

public class LevellingRole {
  private long roleId;
  private int levelRequirement;
  private String customLevelUpMessage;

  public LevellingRole(Document doc) {
    this.roleId = doc.getLong("roleId");
    this.levelRequirement = doc.getInteger("levelRequirement");
    this.customLevelUpMessage = doc.getString("customLevelUpMessage");
  }

  public LevellingRole(long roleId) {
    this.roleId = roleId;
  }

  public void setRoleId(long id) {
    this.roleId = id;
  }

  public long getRoleId() {
    return roleId;
  }

  public void setLevelRequirement(int requirement) {
    this.levelRequirement = requirement;
  }

  public int getLevelRequirement() {
    return levelRequirement;
  }

  public void setCustomLevelUpMessage(String message) {
    this.customLevelUpMessage = message;
  }

  public String getCustomLevelUpMessage() {
    return customLevelUpMessage;
  }

  public Document toDocument() {
    return new Document()
        .append("roleId", roleId)
        .append("levelRequirement", levelRequirement)
        .append("customLevelUpMessage", customLevelUpMessage);
  }

}
