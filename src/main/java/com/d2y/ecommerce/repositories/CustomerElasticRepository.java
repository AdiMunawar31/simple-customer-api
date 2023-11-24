package com.d2y.ecommerce.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.d2y.ecommerce.models.CustomerElastic;

public interface CustomerElasticRepository extends ElasticsearchRepository<CustomerElastic, Long> {

}
