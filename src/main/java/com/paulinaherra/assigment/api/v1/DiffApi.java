package com.paulinaherra.assigment.api.v1;

import com.paulinaherra.assigment.api.v1.request.DiffRequest;
import com.paulinaherra.assigment.api.v1.response.DiffResponse;
import com.paulinaherra.assigment.model.Diff;
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

  @PostMapping(value = "/{id}/right")
  private void saveRight(@PathVariable String id, @RequestBody @Valid DiffRequest diffRequest) {
    diffService.saveRight(id, diffRequest.getData());
  }

  @PostMapping("/{id}/left")
  private void saveLeft(@PathVariable String id, @RequestBody @Valid DiffRequest diffRequest) {
    diffService.saveLeft(id, diffRequest.getData());
  }

}
