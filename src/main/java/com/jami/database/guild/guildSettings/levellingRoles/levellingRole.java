package com.jami.database.guild.guildSettings.levellingRoles;

import org.bson.Document;
import org.bson.types.ObjectId;

public class levellingRole {
  private ObjectId id;
  private long roleId;
  private int levelRequirement;

  public levellingRole(long roleId, int levelRequirement) {
    this.id = new ObjectId();
    this.roleId = roleId;
    this.levelRequirement = levelRequirement;
  }

  public levellingRole(Document doc) {
    this.id = doc.getObjectId("_id");
    this.roleId = doc.getLong("roleId");
    this.levelRequirement = doc.getInteger("levelRequirement");
  }

  public String getId() {
    return id.toString();
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

  public Document toDocument() {
    return new Document()
        .append("_id", id)
        .append("roleId", roleId)
        .append("levelRequirement", levelRequirement);
  }
}
