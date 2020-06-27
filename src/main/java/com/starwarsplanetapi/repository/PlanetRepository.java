package com.starwarsplanetapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.starwarsplanetapi.model.Planet;

@Repository
public interface PlanetRepository extends MongoRepository<Planet, String> {
}
