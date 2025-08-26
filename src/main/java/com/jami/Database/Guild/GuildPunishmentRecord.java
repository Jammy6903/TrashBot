package com.jami.Database.Guild;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import com.jami.Database.Enumerators.PunishmentType;

public class GuildPunishmentRecord {

  @BsonId
  private ObjectId punishmentId;

  @BsonProperty
  private long guildId;

  @BsonProperty("userId")
  private long userId;

  @BsonProperty("punishmentType")
  private PunishmentType punishmentType;

  @BsonProperty("punishmentReason")
  private String punishmentReason;

  @BsonProperty("punishmentLength")
  private long punishmentLength;

  @BsonProperty("punishmentTime")
  private long punishmentTime;

  @BsonProperty("active")
  private boolean active;

  public GuildPunishmentRecord(Document doc) {
  }

  // PunishmentId

  public ObjectId getPunishmentId() {
    return punishmentId;
  }

  // GuildId

  public long getGuildId() {
    return guildId;
  }

  // UserId

  public long getUserId() {
    return userId;
  }

  // PunishmentType

  public void setPunishmentType(PunishmentType type) {
    this.punishmentType = type;
  }

  public PunishmentType getPunishmentType() {
    return punishmentType;
  }

  // PunishmentReason

  public void setPunishmentReason(String reason) {
    this.punishmentReason = reason;
  }

  public String getPunishmentReason() {
    return punishmentReason;
  }

  // PunishmentLength

  public void setPunishmentLength(long length) {
    this.punishmentLength = length;
  }

  public long getPunishmentLength() {
    return punishmentLength;
  }

  // PunishmentTime

  public long getPunishmentTime() {
    return punishmentTime;
  }

  // Active

  public void setActive(boolean bool) {
    this.active = bool;
  }

  public boolean getActive() {
    return active;
  }
}
