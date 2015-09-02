package com.github.yatol.backend.services;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

import com.github.yatol.backend.demo.AbstractIntegrationTest;
import com.github.yatol.backend.services.responses.RegisterResponse;

/**
 * All tests need running Postgresql and jboss AS.
 * 
 * @author christian
 *
 */
public class UserRegisterIntegrationTest extends AbstractIntegrationTest {

  private static final String HUGO_USERNAME = "Hugo";

  @Test
  public void testRegisterNonExistingUser() throws Exception {

    // when: user calls register and name not exists
    RegisterResponse callResult = registerUser(HUGO_USERNAME);

    // then: user object with id is returned
    assertThat(callResult.isSuccess()).isTrue();
    assertThat(callResult.getUser().getId()).isNotNull();
    assertThat(callResult.getUser().getUsername()).isEqualTo(HUGO_USERNAME);
  }

  @Test
  public void testRegisterExistingUser() throws Exception {
    // given: registered user
    registerUser(HUGO_USERNAME);

    // when: user with same name wants to register
    RegisterResponse callResult = registerUser(HUGO_USERNAME);

    // then: success is false user returned is null
    assertThat(callResult.isSuccess()).isFalse();
    assertThat(callResult.getUser()).isNull();
  }

  @Test
  public void testRegisterExistingUserIgnoreSpelling() throws Exception {
    registerUser(HUGO_USERNAME);

    // when: user with different spelling of same name tries to register

    RegisterResponse response = registerUser("hugo");

    // then: the call is invalid

    assertThat(response.isSuccess()).isFalse();
    assertThat(response.getUser()).isNull();
  }

}
