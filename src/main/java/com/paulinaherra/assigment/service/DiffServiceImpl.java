package com.paulinaherra.assigment.service;

import com.paulinaherra.assigment.api.v1.response.DiffResponse;
import com.paulinaherra.assigment.data.DiffRepository;
import com.paulinaherra.assigment.model.Diff;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static com.paulinaherra.assigment.api.v1.response.Status.EQUAL;
import static com.paulinaherra.assigment.api.v1.response.Status.EQUAL_SIZE;
import static com.paulinaherra.assigment.api.v1.response.Status.NOT_EQUAL_SIZE;

@Service
public class DiffServiceImpl implements DiffService {

  private final DiffRepository repository;

  public DiffServiceImpl(DiffRepository repository) {
    this.repository = repository;
  }

  @Override
  public Mono<DiffResponse> getDiff(String id) {
    return repository.findById(id)
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
    repository.findById(id)
      .flatMap(diff -> {
        return repository.save(diff.setLeft(data).setId(id));
        })
      .switchIfEmpty(repository.save(new Diff().setLeft(data).setId(id)));

  }

  @Override
  public void saveRight(String id, String data) {
    repository.findById(id)
      .flatMap(diff -> {
        return repository.save(diff.setRight(data).setId(id));
      })
      .switchIfEmpty(repository.save(new Diff().setRight(data).setId(id)));
  }

}
