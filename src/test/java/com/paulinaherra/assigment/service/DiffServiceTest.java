package com.paulinaherra.assigment.service;

import com.paulinaherra.assigment.api.v1.response.Result;
import com.paulinaherra.assigment.model.Diff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class DiffServiceTest {

  private ReactiveMongoTemplate template;
  private DiffService diffService;

  @BeforeEach
  void setUp() {
    template = Mockito.mock(ReactiveMongoTemplate.class);
    diffService = new DiffServiceImpl(template);

    when(template.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(Diff.class)))
      .thenReturn(Mono.empty());
  }

  @Test
  void getDiffNotFound() {
    when(template.findById("id", Diff.class)).thenReturn(Mono.empty());

    StepVerifier
      .create(diffService.getDiff("id"))
      .expectError()
      .verify();
  }

  @Test
  void getDiffEqual() {
    Diff diff = new Diff();
    diff.setLeft("ewogICAgZGF0YTogInRoaXMgaXMgYSBqc29uIgp9");
    diff.setRight("ewogICAgZGF0YTogInRoaXMgaXMgYSBqc29uIgp9");

    when(template.findById("id", Diff.class)).thenReturn(Mono.just(diff));

    StepVerifier
      .create(diffService.getDiff("id"))
      .expectNextMatches(result -> {
        assertThat(result.getLength(), is(nullValue()));
        assertThat(result.getOffset(), is(nullValue()));
        assertThat(result.getResult(), is(Result.EQUAL));
        return true;
      })
      .verifyComplete();
  }

  @Test
  void getDiffEqualSize() {
    Diff diff = new Diff();
    diff.setLeft("eWogICAgZGF0YTogInRoaXMgaXMgYSBqc29uIgp9");
    diff.setRight("ewogICAgZGF0YTogInRoaXMgaXMgYSBqc29uIgp9");

    when(template.findById("id", Diff.class)).thenReturn(Mono.just(diff));

    StepVerifier
      .create(diffService.getDiff("id"))
      .expectNextMatches(result -> {
        assertThat(result.getLength(), is(diff.getLeft().length()));
        assertThat(result.getOffset(), is(1));
        assertThat(result.getResult(), is(Result.EQUAL_SIZE));
        return true;
      })
      .verifyComplete();
  }

  @Test
  void getDiffNotEqual() {
    Diff diff = new Diff();
    diff.setLeft("BwogICAgZGF0YTogInRoaXMgaXMgYSBqc29uIgp9");
    diff.setRight("ewogICAgZGF0YTog");

    when(template.findById("id", Diff.class)).thenReturn(Mono.just(diff));

    StepVerifier
      .create(diffService.getDiff("id"))
      .expectNextMatches(result -> {
        assertThat(result.getLength(), is(nullValue()));
        assertThat(result.getOffset(), is(nullValue()));
        assertThat(result.getResult(), is(Result.NOT_EQUAL_SIZE));
        return true;
      })
      .verifyComplete();
  }

  @Test
  void saveRight() {
    diffService.save("id", "data", "right");
    Mockito.verify(template)
      .findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(Diff.class));
  }

  @Test
  void saveLeft() {
    diffService.save("id", "data", "left");
    Mockito.verify(template)
      .findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(Diff.class));
  }
}