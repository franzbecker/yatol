package com.github.yatol.backend.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.core.MediaType;

import org.junit.AfterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class AbstractIntegrationTest {

  public static final String BACKEND_URL = "BACKEND_URL";
  public static final String BACKEND_URL_DEFAULT = "http://localhost:8081/backend/";

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private static final String url = "jdbc:postgresql://localhost:5432/postgres";
  private static final String user = "postgres";
  private static final String pwd = "postgres";

  @AfterClass
  public static void afterTests() {
    try {
      Connection connection = DriverManager.getConnection(url, user, pwd);
      Statement statement = connection.createStatement();
      statement.execute("DELETE FROM PUBLIC.USER;");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private String getBackendUrl() {
    String backendUrl = System.getProperty(BACKEND_URL, BACKEND_URL_DEFAULT);
    if (backendUrl.endsWith("/")) {
      return backendUrl;
    } else {
      return backendUrl + "/";
    }
  }

  protected <T> T callBackend(String urlpart, Class<T> clazz, String jsonInput) {
    Client client = Client.create();
    WebResource resource = client.resource(getBackendUrl() + urlpart);

    String input = jsonInput;
    ClientResponse response = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .post(ClientResponse.class, input);

    int httpStatus = response.getStatus();
    if (httpStatus != 200) {
      throw new RuntimeException("Failed, HTTP-Code: " + httpStatus);
    }

    String jsonString = response.getEntity(String.class);
    T fromJson = new Gson().fromJson(jsonString, clazz);
    return fromJson;
  }

  protected <T> T callBackend(String urlpart, Class<T> clazz) {
    return callBackend(urlpart, clazz, "");
  }
}
