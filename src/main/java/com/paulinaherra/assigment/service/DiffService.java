package com.paulinaherra.assigment.service;

import com.paulinaherra.assigment.api.v1.response.DiffResponse;
import reactor.core.publisher.Mono;

public interface DiffService {

  Mono<DiffResponse> getDiff(String id);

  void saveLeft(String id, String data);

  void saveRight(String id, String data);

}
