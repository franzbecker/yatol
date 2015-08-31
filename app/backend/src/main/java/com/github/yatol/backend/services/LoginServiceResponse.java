package com.github.yatol.backend.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class LoginServiceResponse {

  private boolean success;
  private String token;
}
