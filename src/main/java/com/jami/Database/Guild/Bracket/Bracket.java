package com.jami.Database.Guild.Bracket;

import org.bson.Document;

public class Bracket {
  private String bracketId;
  private String bracketName;
  private String bracketDescription;
  private long roleId;
  private long interval;

  public Bracket(Document b) {

  }

  public class team {
    private String teamName;
    private String teamImage;

    public team(String name, String image) {
      this.teamName = name;
      this.teamImage = image;
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

    public Document toDocument() {
      return new Document()
          .append("name", teamName)
          .append("image", teamImage);
    }
  }

  public Document toDocument() {
    return new Document();
  }

}
