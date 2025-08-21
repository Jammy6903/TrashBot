package com.jami.Database.Utilities;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.jami.App;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

public class FeatureRequestRecord {
  private static MongoDatabase db = App.getMongoClient().getDatabase("TRASHBOT");
  private static MongoCollection<Document> requests = db.getCollection("featureRequests");

  private ObjectId requestId;
  private String requestTitle;
  private String requestDescription;
  private String userName;
  private String status;
  private long userId;

  public FeatureRequestRecord(String title, String description, String userName, long userId) {
    this.requestId = new ObjectId();
    this.requestTitle = title;
    this.requestDescription = description;
    this.userName = userName;
    this.userId = userId;
    this.status = "Requested";
  }

  public FeatureRequestRecord(ObjectId id) {
    Document req = requests.find(eq("_id", id)).first();
    this.requestId = id;
    this.requestTitle = req.getString("requestTitle");
    this.requestDescription = req.getString("requestDescription");
    this.userName = req.getString("userName");
    this.userId = req.getLong("userId");
    this.status = req.getString("status");
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

  public void setStatus(String st) {
    this.status = st;
  }

  public String getStatus() {
    return status;
  }

  public void commit() {
    Document r = new Document("$set", new Document()
        .append("_id", requestId)
        .append("requestTitle", requestTitle)
        .append("requestDescription", requestDescription)
        .append("userName", userName)
        .append("userId", userId)
        .append("status", status));
    try {
      UpdateOptions opts = new UpdateOptions().upsert(true);
      requests.updateOne(eq("_id", requestId), r, opts);
    } catch (Exception e) {
      System.out.println("[ERROR] MongoDB Write Error - " + e);
    }
  }
}
