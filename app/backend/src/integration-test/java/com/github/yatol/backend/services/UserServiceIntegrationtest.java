package com.github.yatol.backend.services;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Ignore;

public class UserServiceIntegrationtest {

  // TODO we need running AS in combination with Eclipse to execute integration
  // tests.
  @Ignore
  public void testRegisterUser() throws Exception {

    // given
    UserService userServiceImpl = new UserService();

    // when
    userServiceImpl.registerUser("Hugo");

    // then
    assertThat(userServiceImpl.getUser("Hugo")).isNotNull();
  }
}
