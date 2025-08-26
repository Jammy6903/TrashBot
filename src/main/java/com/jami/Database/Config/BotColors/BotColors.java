package com.jami.Database.Config.BotColors;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class BotColors {

  @BsonProperty("botPrimaryColor")
  private String botPrimaryColor;

  @BsonProperty("botSecondaryColor")
  private String botSecondaryColor;

  @BsonProperty("botWarnColor")
  private String botWarnColor;

  @BsonProperty("botErrorColor")
  private String botErrorColor;

  public BotColors() {
  }

  public BotColors(boolean d) {
    this.botPrimaryColor = "#61eaff";
    this.botSecondaryColor = "#9061ff";
    this.botWarnColor = "#ffe761";
    this.botErrorColor = "#ff6171";
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
