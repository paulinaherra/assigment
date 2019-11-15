package com.paulinaherra.assigment.service;

import com.paulinaherra.assigment.api.v1.response.DiffResponse;
import com.paulinaherra.assigment.model.Diff;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static com.paulinaherra.assigment.api.v1.response.Status.EQUAL;
import static com.paulinaherra.assigment.api.v1.response.Status.EQUAL_SIZE;
import static com.paulinaherra.assigment.api.v1.response.Status.NOT_EQUAL_SIZE;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@Service
public class DiffServiceImpl implements DiffService {

  private final ReactiveMongoTemplate mongoTemplate;

  public DiffServiceImpl(ReactiveMongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Mono<DiffResponse> getDiff(String id) {
    return mongoTemplate.findById(id, Diff.class)
      .flatMap(diff -> {
        DiffResponse diffResponse = new DiffResponse();

        if (StringUtils.isEmpty(diff.getRight()) || StringUtils.isEmpty(diff.getLeft()) || diff.getRight().length() != diff.getLeft().length()) {
          diffResponse.setStatus(NOT_EQUAL_SIZE);
          return Mono.just(diffResponse);
        }

        if (diff.getRight().equals(diff.getLeft())) {
          diffResponse.setStatus(EQUAL);
          return Mono.just(diffResponse);
        }

        int strOffset = diff.getRight().length() - diff.getLeft().length();

        return Mono.just(diffResponse.setLength(diff.getRight().length()).setStatus(EQUAL_SIZE).setOffset(strOffset));
      })
      .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }

  @Override
  public void saveLeft(String id, String data) {
    mongoTemplate.findAndModify(query(Criteria.where("id").is(id)), update("right", data).setOnInsert("id", id),
      new FindAndModifyOptions().upsert(true).returnNew(true), Diff.class)
      .log()
      .subscribe(System.out::println);
  }

  @Override
  public void saveRight(String id, String data) {
    mongoTemplate.findAndModify(query(Criteria.where("id").is(id)), update("right", data).setOnInsert("id", id),
      new FindAndModifyOptions().upsert(true).returnNew(true), Diff.class)
      .log()
      .subscribe(System.out::println);
  }

}
