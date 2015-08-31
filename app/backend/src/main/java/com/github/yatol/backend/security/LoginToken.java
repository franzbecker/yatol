package com.github.yatol.backend.security;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Singleton;

import com.github.yatol.backend.entities.User;

@Singleton
//@Dependent
//@Stateless
public class LoginToken {

  private static final Map<String, User> tokenForUser = new ConcurrentHashMap<>();

  public String addToken(User user) {
    String uuidToken = UUID.randomUUID().toString();
    tokenForUser.put(uuidToken, user);
    return uuidToken;
  }

  public User verifyToken(String token) {
    return tokenForUser.get(token);
  }

}
