package com.github.yatol.backend.services.responses;

import com.github.yatol.backend.entities.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class RegisterResponse {

  private boolean success;
  private User user;
}
