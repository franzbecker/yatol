package com.github.yatol.backend.demo;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class HelloWorldIntegrationTest extends AbstractIntegrationTest {

	@Test
	public void helloWorldJsonTest() throws Exception {
		// Given
		String backendUrl = getBackendUrl();
		logger.info("Using backendUrl='{}'", backendUrl);

		// When
		WebResource resource = Client.create().resource(backendUrl + "json");
		String result = resource.get(String.class);

		// Then
		assertThat(result).isEqualTo("{\"result\":\"Hello World\"}");
	}

}
