/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.yatol.backend.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.yatol.backend.entities.User;

@Path("/")
@Stateless
public class HelloWorldService {

  private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldService.class);

  @PersistenceContext(unitName = "yatol")
  private EntityManager em;

  @GET
  @POST
  @Path("/json")
  @Produces({ "application/json" })
  public String getHelloWorldJSON() {
    LOGGER.debug("Got json");

    User user = new User();
    user.setUsername("Hugo");

    em.persist(user);

    LOGGER.debug("Persisted user" + user);

    return "{\"result\":\"" + "Hello World" + "\"}";
  }

  @GET
  @POST
  @Path("/xml")
  @Produces({ "application/xml" })
  public String getHelloWorldXML() {
    LOGGER.debug("Got xml");
    return "<xml><result>" + "Hello World" + "</result></xml>";
  }
}
