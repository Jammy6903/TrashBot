package com.jami.database.guild.bracket;

import org.bson.Document;

public class bracket {
  private String bracketId;
  private String bracketName;
  private String bracketDescription;
  private long roleId;
  private long interval;

  public bracket(Document b, long id) {

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

}
