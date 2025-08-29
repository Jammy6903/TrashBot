package com.jami.Database.repositories;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.*;

import org.bson.types.ObjectId;

import com.jami.App;
import com.jami.Database.Utilities.FeatureRequestRecord;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;

public class FeatureRequestRepo {
  private static final MongoCollection<FeatureRequestRecord> featureRequests = App.getDatabase()
      .getCollection("FEATURE_REQUESTS", FeatureRequestRecord.class);

  public static void ensureIndexes() {
    featureRequests.createIndex(ascending("title"));
    featureRequests.createIndex(descending("upvotes"));
  }

  public static FeatureRequestRecord getById(String id) {
    return featureRequests.find(eq("_id", new ObjectId(id))).first();
  }

  public static String createRequest(String title, String description, String userName, long userId) {
    return featureRequests.insertOne(new FeatureRequestRecord(title, description, userName, userId))
        .getInsertedId().asObjectId().getValue().toString();
  }

  public static void save(FeatureRequestRecord record) {
    featureRequests.replaceOne(eq("_id", record.getId()), record, new ReplaceOptions().upsert(true));
  }

}
