package com.jami.database.guild.guildPunishment;

import org.bson.Document;
import org.bson.types.ObjectId;

public class guildPunishment {
  private ObjectId punishmentId;
  private long userId;
  private String punishmentType;
  private String punishmentReason;
  private long punishmentLength;
  private long punishmentTime;
  private boolean active;

  public guildPunishment(long userId, String punishmentType, String punishmentReason, long punishmentLength) {
    this.punishmentId = new ObjectId();
    this.userId = userId;
    this.punishmentType = punishmentType;
    this.punishmentReason = punishmentReason;
    this.punishmentLength = punishmentLength;
    this.punishmentTime = System.currentTimeMillis();
    this.active = true;
  }

  public guildPunishment(Document p) {
    this.punishmentId = p.getObjectId("_id");
    this.userId = p.getLong("userId");
    this.punishmentType = p.getString("punishmentType");
    this.punishmentReason = p.getString("punishmentReason");
    this.punishmentLength = p.getLong("punishmentLength");
    this.punishmentTime = p.getLong("punishmentTime");
    this.active = p.getBoolean("active");
  }

  public String getId() {
    return punishmentId.toString();
  }

  public void setUserId(long uId) {
    this.userId = uId;
  }

  public long getUserId() {
    return userId;
  }

  public void setType(String type) {
    this.punishmentType = type;
  }

  public String getType() {
    return punishmentType;
  }

  public void setReason(String reason) {
    this.punishmentReason = reason;
  }

  public String getReason() {
    return punishmentReason;
  }

  public void setLength(long length) {
    this.punishmentLength = length;
  }

  public long getLength() {
    return punishmentLength;
  }

  public void setTime(long time) {
    this.punishmentTime = time;
  }

  public long getTime() {
    return punishmentTime;
  }

  public void setActive(boolean bool) {
    this.active = bool;
  }

  public boolean getActive() {
    return active;
  }

  public Document toDocument() {
    return new Document()
        .append("_id", punishmentId)
        .append("userId", punishmentId)
        .append("punishmentType", punishmentType)
        .append("punishmentReason", punishmentReason)
        .append("punishmentLength", punishmentLength)
        .append("punishmentTime", punishmentTime)
        .append("active", active);
  }
}
