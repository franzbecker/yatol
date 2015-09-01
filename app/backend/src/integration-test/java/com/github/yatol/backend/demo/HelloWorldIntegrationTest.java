package com.github.yatol.backend.demo;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Ignore;

public class HelloWorldIntegrationTest extends AbstractIntegrationTest {

  @Ignore
  public void helloWorldJsonTest() throws Exception {

    // When
    String result = callBackend("json", String.class);

    // Then
    assertThat(result).isEqualTo("{\"result\":\"Hello World\"}");
  }

}
