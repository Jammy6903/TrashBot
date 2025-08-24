package com.jami.Database.Utilities;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.jami.Database.GetorDefault;
import com.jami.Database.Enumerators.EnumToString;
import com.jami.Database.Enumerators.FeatureRequestStatus;

public class FeatureRequestRecord {

  private ObjectId requestId;
  private String requestTitle;
  private String requestDescription;
  private String userName;
  private long userId;
  private FeatureRequestStatus status;

  public FeatureRequestRecord(Document doc) {
    this.requestId = doc.getObjectId("_id");
    this.requestTitle = doc.getString("requestTitle");
    this.requestDescription = doc.getString("requestDescription");
    this.userName = doc.getString("userName");
    this.userId = doc.getLong("userId");
    this.status = GetorDefault.Enum(doc, "status", FeatureRequestStatus.class, FeatureRequestStatus.REQUESTED);
  }

  public FeatureRequestRecord(String title, String description, String userName, long userId) {
    this.requestId = new ObjectId();
    this.requestTitle = title;
    this.requestDescription = description;
    this.userName = userName;
    this.userId = userId;
    this.status = FeatureRequestStatus.REQUESTED;
  }

  public String getId() {
    return requestId.toString();
  }

  public void setTitle(String title) {
    this.requestTitle = title;
  }

  public String getTitle() {
    return requestTitle;
  }

  public void setDescription(String description) {
    this.requestDescription = description;
  }

  public String getDescription() {
    return requestDescription;
  }

  public void setUserName(String name) {
    this.userName = name;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserId(long id) {
    this.userId = id;
  }

  public long getUserId() {
    return userId;
  }

  public void setStatus(FeatureRequestStatus status) {
    this.status = status;
  }

  public FeatureRequestStatus getStatus() {
    return status;
  }

  public Document toDocument() {
    return new Document()
        .append("_id", requestId)
        .append("requestTitle", requestTitle)
        .append("requestDescription", requestDescription)
        .append("userName", userName)
        .append("userId", userId)
        .append("status", EnumToString.get(status));
  }
}
