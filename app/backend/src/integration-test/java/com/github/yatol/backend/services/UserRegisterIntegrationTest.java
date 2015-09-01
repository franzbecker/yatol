package com.github.yatol.backend.services;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.*;

import org.junit.Before;
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

  @Before
  public void before() {

  }

  @Test
  public void testRegisterNonExistingUser() throws Exception {

    // when: user calls register and name not exists
    RegisterResponse callResult = callBackend("users/register", RegisterResponse.class, "Hugo");

    // then: user object with id is returned
    assertThat(callResult.isSuccess()).isTrue();
    assertThat(callResult.getUser().getId()).isNotNull();
    assertThat(callResult.getUser().getUsername()).isEqualTo("Hugo");
  }
}
