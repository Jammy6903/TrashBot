package com.jami.Database.Guild;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.jami.Database.GetorDefault;
import com.jami.Database.Enumerators.EnumToString;
import com.jami.Database.Enumerators.PunishmentType;

public class GuildPunishmentRecord {
  private ObjectId punishmentId;
  private long guildId;
  private long userId;
  private PunishmentType punishmentType;
  private String punishmentReason;
  private long punishmentLength;
  private long punishmentTime;
  private boolean active;

  public GuildPunishmentRecord(Document doc) {
    this.punishmentId = doc.getObjectId("_id");
    this.guildId = doc.getLong("guildId");
    this.userId = doc.getLong("userId");
    this.punishmentType = GetorDefault.Enum(doc, "punishmentType", PunishmentType.class, null);
    this.punishmentReason = doc.getString("punishmentReason");
    this.punishmentLength = doc.getLong("punishmentLength");
    this.punishmentTime = doc.getLong("punishmentTime");
    this.active = doc.getBoolean("active");
  }

  public GuildPunishmentRecord(long guildId, long userId, PunishmentType punishmentType, String punishmentReason,
      long punishmentLength) {
    this.punishmentId = new ObjectId();
    this.guildId = guildId;
    this.userId = userId;
    this.punishmentType = punishmentType;
    this.punishmentReason = punishmentReason;
    this.punishmentLength = punishmentLength;
    this.punishmentTime = System.currentTimeMillis();
    this.active = true;
  }

  public void setPunishmentId(String id) {
    this.punishmentId = new ObjectId(id);
  }

  public String getPunishmentId() {
    return punishmentId.toString();
  }

  public void setGuildId(long guildId) {
    this.guildId = guildId;
  }

  public long getGuildId() {
    return guildId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public long getUserId() {
    return userId;
  }

  public void setType(PunishmentType type) {
    this.punishmentType = type;
  }

  public PunishmentType getType() {
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
        .append("guildId", guildId)
        .append("userId", punishmentId)
        .append("punishmentType", EnumToString.get(punishmentType))
        .append("punishmentReason", punishmentReason)
        .append("punishmentLength", punishmentLength)
        .append("punishmentTime", punishmentTime)
        .append("active", active);
  }
}
