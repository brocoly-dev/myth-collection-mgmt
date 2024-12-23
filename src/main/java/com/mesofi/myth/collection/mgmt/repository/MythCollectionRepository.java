package com.mesofi.myth.collection.mgmt.repository;

import com.mesofi.myth.collection.mgmt.model.Figurine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MythCollectionRepository extends MongoRepository<Figurine, String> {}
