package com.jami.Database.Guild.Bracket;

import java.util.List;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class Bracket {

  @BsonProperty("bracketName")
  private String bracketName;

  @BsonProperty("bracketDescription")
  private String bracketDescription;

  @BsonProperty("roleId")
  private long roleId;

  @BsonProperty("interval")
  private long interval;

  @BsonProperty("teams")
  private List<Team> teams;

  public Bracket() {

  }

  // Name

  public void setBracketName(String name) {
    this.bracketName = name;
  }

  public String getBracketName() {
    return bracketName;
  }

  // Description

  public void setBracketDescription(String description) {
    this.bracketDescription = description;
  }

  public String getBracketDescription() {
    return bracketDescription;
  }

  // PingRoleId

  public void setRoleId(long id) {
    this.roleId = id;
  }

  public long getRoleId() {
    return roleId;
  }

  // Interval

  public void setInterval(long time) {
    this.interval = time;
  }

  public long getInterval() {
    return interval;
  }

  // Teams

  public void addTeam(Team team) {
    this.teams.add(team);
  }

  public void removeTeam(Team team) {
    this.teams.remove(team);
  }

  public List<Team> getTeams() {
    return teams;
  }
}
