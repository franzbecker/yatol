package com.github.yatol.backend.services;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

import com.github.yatol.backend.demo.AbstractIntegrationTest;
import com.github.yatol.backend.services.responses.LoginResponse;

public class UserLoginIntegrationTest extends AbstractIntegrationTest {

  private static final String HUGO_USERNAME = "Hugo";

  @Test
  public void testLoginUser() throws Exception {
    // given: user is registered
    registerUser(HUGO_USERNAME);

    // when: user wants to login
    LoginResponse response = callBackend("users/login", LoginResponse.class, "Hugo");

    // then: call is success
    assertThat(response.isSuccess()).isTrue();
    assertThat(response.getToken()).isNotNull();
  }

  @Test
  public void testLoginNotExistingUser() throws Exception {
    // when: user wants to login but not exists
    LoginResponse response = callBackend("users/login", LoginResponse.class, "Hugo");

    // then: call is discarded
    assertThat(response.isSuccess()).isFalse();
    assertThat(response.getToken()).isNull();
  }
}
