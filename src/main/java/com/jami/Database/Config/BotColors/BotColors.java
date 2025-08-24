package com.jami.Database.Config.BotColors;

import org.bson.Document;

import com.jami.Database.GetorDefault;

public class BotColors {

  private String botPrimaryColor;
  private String botSecondaryColor;
  private String botWarnColor;
  private String botErrorColor;

  private static String defaultColor = "000000";

  public BotColors(Document doc) {
    this.botPrimaryColor = GetorDefault.String(doc, "botPrimaryColor", defaultColor);
    this.botSecondaryColor = GetorDefault.String(doc, "botSecondaryColor", defaultColor);
    this.botWarnColor = GetorDefault.String(doc, "botWarnColor", defaultColor);
    this.botErrorColor = GetorDefault.String(doc, "botErrorColor", defaultColor);
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

  public Document toDocument() {
    return new Document()
        .append("botPrimaryColor", botPrimaryColor)
        .append("botSecondaryColor", botSecondaryColor)
        .append("botWarnColor", botWarnColor)
        .append("botErrorColor", botErrorColor);
  }
}
