package com.paulinaherra.assigment.integration;

import com.paulinaherra.assigment.Application;
import com.paulinaherra.assigment.api.v1.response.DiffResponse;
import com.paulinaherra.assigment.api.v1.response.Result;
import com.paulinaherra.assigment.model.Diff;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(Application.class)
public class IntegrationTest {

  private final WebClient webClient = WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

  @Autowired
  private ReactiveMongoTemplate template;
  @LocalServerPort
  private int port;

  @Test
  public void getDiff() throws Exception {
    Diff diff = new Diff()
      .setId("200")
      .setRight("ewogICAgZGF0YTogInRoaXMgaXPgYSAqc29uIgp9")
      .setLeft("ewogICAgZGF0YTogInRoaXMgaXMgYSBqc29uIgp9");

    template.insert(diff)
      .log()
      .subscribe(System.out::println);

    DiffResponse response = webClient
      .get()
      .uri("http://localhost:" + port + "/api/v1/diff/200")
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .bodyToMono(DiffResponse.class)
      .log()
      .block();

    assertThat(response.getLength(), is(40));
    assertThat(response.getOffset(), is(26));
    assertThat(response.getResult(), is(Result.EQUAL_SIZE));

    template.remove(diff);
  }

}
