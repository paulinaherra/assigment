package com.paulinaherra.assigment.data;

import com.paulinaherra.assigment.model.Diff;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface DiffRepository extends ReactiveCrudRepository<Diff, String> {

}
