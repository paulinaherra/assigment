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
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static com.paulinaherra.assigment.api.v1.response.Result.EQUAL;
import static com.paulinaherra.assigment.api.v1.response.Result.EQUAL_SIZE;
import static com.paulinaherra.assigment.api.v1.response.Result.NOT_EQUAL_SIZE;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@Service
@RequiredArgsConstructor
public class DiffServiceImpl implements DiffService {

  private final ReactiveMongoTemplate mongoTemplate;

  @Override
  public Mono<DiffResponse> getDiff(String id) {
    return mongoTemplate.findById(id, Diff.class)
      .flatMap(diff -> {
        DiffResponse diffResponse = new DiffResponse();

        if (StringUtils.isEmpty(diff.getRight()) || StringUtils.isEmpty(diff.getLeft()) || diff.getRight().length() != diff.getLeft().length()) {
          diffResponse.setResult(NOT_EQUAL_SIZE);
          return Mono.just(diffResponse);
        }
        if (diff.getRight().equals(diff.getLeft())) {
          diffResponse.setResult(EQUAL);
          return Mono.just(diffResponse);
        }

        return Mono.just(diffResponse
          .setLength(diff.getRight().length())
          .setResult(EQUAL_SIZE)
          .setOffset(getIndexOfDifference(diff.getLeft(), diff.getRight())));
      })
      .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }

  @Override
  public void save(String id, String data, String side) {
    mongoTemplate.findAndModify(query(Criteria.where("id").is(id)), update(side, data).setOnInsert("id", id),
      new FindAndModifyOptions().upsert(true).returnNew(true), Diff.class)
      .log()
      .subscribe(System.out::println);
  }

  private int getIndexOfDifference(String left, String right) {
    int i = 0;
    for (i = 0; i < left.length() && i < left.length(); ++i) {
      if (left.charAt(i) != right.charAt(i)) {
        break;
      }
    }
    return i;
  }

}
