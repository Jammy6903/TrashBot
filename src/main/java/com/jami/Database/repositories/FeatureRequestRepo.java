package com.jami.Database.repositories;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.jami.App;
import com.jami.Database.Utilities.FeatureRequestRecord;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

public class FeatureRequestRepo {
  private static final MongoDatabase database = App.getMongoClient().getDatabase("TRASHBOT");
  private static final MongoCollection<Document> featureRequests = database.getCollection("FEATURE_REQUESTS");

  public static FeatureRequestRecord getById(String id) {
    return new FeatureRequestRecord(featureRequests.find(eq("_id", new ObjectId(id))).first());
  }

  public static FeatureRequestRecord createRequest(String title, String description, String userName, long userId) {
    return new FeatureRequestRecord(title, description, userName, userId);
  }

  public static void save(FeatureRequestRecord record) {
    featureRequests.updateOne(eq("_id", record.getId()), record.toDocument(), new UpdateOptions().upsert(true));
  }

}
