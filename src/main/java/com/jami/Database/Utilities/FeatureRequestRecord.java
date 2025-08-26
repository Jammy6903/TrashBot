package com.jami.Database.Utilities;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import com.jami.Database.Enumerators.FeatureRequestStatus;

public class FeatureRequestRecord {

  @BsonId
  private ObjectId requestId;

  @BsonProperty("requestTitle")
  private String requestTitle;

  @BsonProperty("requestDescription")
  private String requestDescription;

  @BsonProperty("userName")
  private String userName;

  @BsonProperty("userId")
  private long userId;

  @BsonProperty("upvotes")
  private long upvotes;

  @BsonProperty("status")
  private FeatureRequestStatus status;

  @BsonProperty("dateCreated")
  private long dateCreated;

  public FeatureRequestRecord() {
  }

  public FeatureRequestRecord(String title, String description, String userName, long userId) {
    this.requestId = new ObjectId();
    this.requestTitle = title;
    this.requestDescription = description;
    this.userName = userName;
    this.userId = userId;
    this.upvotes = 0;
    this.status = FeatureRequestStatus.REQUESTED;
    this.dateCreated = System.currentTimeMillis();
  }

  // requestId

  public ObjectId getId() {
    return requestId;
  }

  // Title

  public void setTitle(String title) {
    this.requestTitle = title;
  }

  public String getTitle() {
    return requestTitle;
  }

  // Description

  public void setDescription(String description) {
    this.requestDescription = description;
  }

  public String getDescription() {
    return requestDescription;
  }

  // UserName

  public void setUserName(String name) {
    this.userName = name;
  }

  public String getUserName() {
    return userName;
  }

  // UserId

  public long getUserId() {
    return userId;
  }

  // Upvotes

  public void setUpvotes(long v) {
    this.upvotes = v;
  }

  public long getUpvotes() {
    return upvotes;
  }

  // Status

  public void setStatus(FeatureRequestStatus status) {
    this.status = status;
  }

  public FeatureRequestStatus getStatus() {
    return status;
  }

  // DateCreated
  public long getDateCreated() {
    return dateCreated;
  }

}
