package com.paulinaherra.assigment.service;

import com.paulinaherra.assigment.api.v1.response.DiffResponse;
import reactor.core.publisher.Mono;

public interface DiffService {

  Mono<DiffResponse> getDiff(String id);

  void save(String id, String data, String side);
}
