package com.paulinaherra.assigment.api.v1.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.codec.binary.Base64;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiffRequest {

  @NotBlank
  private String data;

  @AssertTrue
  public boolean is64Encoded() {
    return Base64.isBase64(data);
  }
}
