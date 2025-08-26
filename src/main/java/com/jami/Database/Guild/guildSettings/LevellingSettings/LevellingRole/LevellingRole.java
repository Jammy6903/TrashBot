package com.jami.Database.Guild.guildSettings.LevellingSettings.LevellingRole;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class LevellingRole {

  @BsonProperty("roleId")
  private long roleId;

  @BsonProperty("levelRequirement")
  private int levelRequirement;

  @BsonProperty("customLevelUpMessage")
  private String customLevelUpMessage;

  public LevellingRole() {
  }

  // RoleId

  public void setRoleId(long id) {
    this.roleId = id;
  }

  public long getRoleId() {
    return roleId;
  }

  // LevelRequirement

  public void setLevelRequirement(int requirement) {
    this.levelRequirement = requirement;
  }

  public int getLevelRequirement() {
    return levelRequirement;
  }

  // CustomLevelUpMessage

  public void setCustomLevelUpMessage(String message) {
    this.customLevelUpMessage = message;
  }

  public String getCustomLevelUpMessage() {
    return customLevelUpMessage;
  }
}
