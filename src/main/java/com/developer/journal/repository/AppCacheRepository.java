package com.developer.journal.repository;

import com.developer.journal.entity.AppCache;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppCacheRepository extends MongoRepository<AppCache, ObjectId> {
}
