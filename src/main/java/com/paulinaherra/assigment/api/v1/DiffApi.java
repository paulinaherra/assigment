package com.paulinaherra.assigment.api.v1;

import com.paulinaherra.assigment.api.v1.request.DiffRequest;
import com.paulinaherra.assigment.api.v1.response.DiffResponse;
import com.paulinaherra.assigment.service.DiffService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import reactor.core.publisher.Mono;

import static com.paulinaherra.assigment.api.v1.DiffApi.URI;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController("DiffApiV1")
@RequestMapping(value = URI, consumes = MediaType.APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class DiffApi {

  static final String URI = "/api/v1/diff";

  private final DiffService diffService;

  public DiffApi(DiffService diffService) {
    this.diffService = diffService;
  }

  @GetMapping("/{id}")
  private Mono<DiffResponse> getDiff(@PathVariable String id) {
    return diffService.getDiff(id);
  }

  @PostMapping("/{id}/{side}")
  private void saveOrUpdate(@PathVariable String id, @PathVariable String side, @RequestBody @Valid DiffRequest diffRequest) {
    diffService.save(id, diffRequest.getData(), side);
  }

}
