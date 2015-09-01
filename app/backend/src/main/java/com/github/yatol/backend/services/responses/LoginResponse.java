package com.github.yatol.backend.services.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class LoginResponse {

  private boolean success;
  private String token;
}
