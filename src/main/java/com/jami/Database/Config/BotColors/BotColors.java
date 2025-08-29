package com.jami.Database.Config.BotColors;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class BotColors {

  @BsonProperty("botPrimaryColor")
  private String botPrimaryColor = "#61eaff";

  @BsonProperty("botSecondaryColor")
  private String botSecondaryColor = "#9061ff";

  @BsonProperty("botWarnColor")
  private String botWarnColor = "#ffe761";

  @BsonProperty("botErrorColor")
  private String botErrorColor = "#ff6171";

  @BsonExtraElements
  private Document legacyValues;

  public BotColors() {
  }

  public void setPrimary(String color) {
    this.botPrimaryColor = color;
  }

  public String getPrimary() {
    return botPrimaryColor;
  }

  public void setSecondary(String color) {
    this.botSecondaryColor = color;
  }

  public String getSecondary() {
    return botSecondaryColor;
  }

  public void setWarn(String color) {
    this.botWarnColor = color;
  }

  public String getWarn() {
    return botWarnColor;
  }

  public void setError(String color) {
    this.botErrorColor = color;
  }

  public String getError() {
    return botErrorColor;
  }
}
