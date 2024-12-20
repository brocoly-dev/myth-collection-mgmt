package com.mesofi.myth.collection.mgmt.repository;

import com.mesofi.myth.collection.mgmt.model.Figurine;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Primary // we make it primary because the Integration Tests uses a custom implementation.
@Repository
public interface MythCollectionRepository extends MongoRepository<Figurine, String> {}
