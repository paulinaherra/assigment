package com.paulinaherra.assigment.api.v1;

import com.paulinaherra.assigment.api.v1.request.DiffRequest;
import com.paulinaherra.assigment.service.DiffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static com.paulinaherra.assigment.api.v1.DiffApi.URI;

@WebFluxTest(DiffApi.class)
public class DiffApiTest {

  @Autowired
  private WebTestClient client;

  @MockBean
  private DiffService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void postRight() throws Exception {
    client
      .post()
      .uri(URI + "/1/right")
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .body(BodyInserters.fromValue(new DiffRequest().setData("ewogICAgZGF0YTogInRoaXMgaXMgYSBqc29uIgp9")))
      .exchange()
      .expectStatus().isOk();
  }

  @Test
  public void postLeft() throws Exception {
    client
      .post()
      .uri(URI + "/1/left")
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .body(BodyInserters.fromValue(new DiffRequest().setData("ewogICAgZGF0YTogInRoaXMgaXMgYSBqc29uIgp9")))
      .exchange()
      .expectStatus().isOk();
  }

  @Test
  public void get() throws Exception {
    client
      .get()
      .uri(URI + "/1")
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON);
  }

}