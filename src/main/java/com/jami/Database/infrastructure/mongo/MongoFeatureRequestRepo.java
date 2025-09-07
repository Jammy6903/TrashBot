package com.jami.Database.infrastructure.mongo;

import static com.mongodb.client.model.Filters.eq;

import org.bson.types.ObjectId;

import com.jami.Database.Utilities.FeatureRequestRecord;
import com.jami.Database.repository.FeatureRequestRepo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;

public class MongoFeatureRequestRepo implements FeatureRequestRepo {
  private MongoCollection<FeatureRequestRecord> featureRequests;

  MongoFeatureRequestRepo() {
    this.featureRequests = Mongo.getDatabase().getCollection("FEATURE_REQUESTS", FeatureRequestRecord.class);
  }

  @Override
  public FeatureRequestRecord getById(String id) {
    return featureRequests.find(eq("_id", new ObjectId(id))).first();
  }

  @Override
  public String createRequest(String title, String description, String userName, long userId) {
    return featureRequests.insertOne(new FeatureRequestRecord(title, description, userName, userId))
        .getInsertedId().asObjectId().getValue().toString();
  }

  @Override
  public void save(FeatureRequestRecord record) {
    featureRequests.replaceOne(eq("_id", record.getId()), record, new ReplaceOptions().upsert(true));
  }

}
