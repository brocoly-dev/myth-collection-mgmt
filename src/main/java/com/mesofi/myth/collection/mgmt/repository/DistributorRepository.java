package com.mesofi.myth.collection.mgmt.repository;

import com.mesofi.myth.collection.mgmt.model.Distributor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributorRepository extends MongoRepository<Distributor, String> {}
