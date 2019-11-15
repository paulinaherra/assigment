package com.paulinaherra.assigment.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
public class DiffTest {

  @Autowired
  private ReactiveMongoTemplate mongoTemplate;

  @Test
  public void persist() throws Exception {
    Diff diff = new Diff()
      .setId("1")
      .setRight("ewogICAgZGF0YTogInRoaXMgaXMgYSBqc29uIgp9")
      .setLeft("ewogICAgZGF0YTogInRoaXMgaXMgYSBqc29uIgp9");

    Mono<Diff> save = mongoTemplate.save(diff);

    StepVerifier
      .create(save)
      .expectNextMatches(savedDiff -> savedDiff.getId().equals(diff.getId()) && savedDiff.getLeft().equals(diff.getLeft()) && savedDiff.getRight()
        .equals(diff.getRight()))
      .verifyComplete();
  }

}
