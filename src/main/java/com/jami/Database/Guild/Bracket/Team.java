package com.jami.Database.Guild.Bracket;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class Team {

  @BsonProperty("teamName")
  private String teamName;

  @BsonProperty("teamImage")
  private String teamImage;

  public Team() {
  }

  public void setName(String name) {
    this.teamName = name;
  }

  public String getName() {
    return teamName;
  }

  public void setImage(String image) {
    this.teamImage = image;
  }

  public String getImage() {
    return teamImage;
  }
}
