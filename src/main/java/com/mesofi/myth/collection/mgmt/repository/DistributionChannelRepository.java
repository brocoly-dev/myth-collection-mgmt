package com.mesofi.myth.collection.mgmt.repository;

import com.mesofi.myth.collection.mgmt.model.DistributionChannel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributionChannelRepository
    extends MongoRepository<DistributionChannel, String> {}
