package com.github.yatol.backend.demo;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class HelloWorldIntegrationTest {

	public static final String BACKEND_URL = "BACKEND_URL";
	public static final String BACKEND_URL_DEFAULT = "http://localhost:8081/backend/";

	@Test
	public void helloWorldJsonTest() throws Exception {
		// Given
		String backendUrl = System.getProperty(BACKEND_URL, BACKEND_URL_DEFAULT);

		// When
		WebResource resource = Client.create().resource(backendUrl + "json");
		String result = resource.get(String.class);

		// Then
		assertThat(result).isEqualTo("{\"result\":\"Hello World\"}");
	}

}
