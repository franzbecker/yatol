package com.github.yatol.backend.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractIntegrationTest {

    public static final String BACKEND_URL = "BACKEND_URL";
    public static final String BACKEND_URL_DEFAULT = "http://localhost:8081/backend/";

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected String getBackendUrl() {
        String backendUrl = System.getProperty(BACKEND_URL, BACKEND_URL_DEFAULT);
        if (backendUrl.endsWith("/")) {
            return backendUrl;
        } else {
            return backendUrl + "/";
        }
    }


}
