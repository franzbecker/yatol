package com.github.yatol.backend.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.yatol.backend.entities.User;
import com.github.yatol.backend.security.LoginToken;
import com.github.yatol.backend.services.responses.LoginResponse;
import com.github.yatol.backend.services.responses.RegisterResponse;

@Path("/users")
@Stateless
public class UserService {

  @PersistenceContext(unitName = "yatol")
  EntityManager em;

  @Inject
  LoginToken loginToken;

  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

  @POST
  @Path("/register")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public RegisterResponse registerUser(String username) {

    LOGGER.info("Call register user with username: " + username);

    if (getUser(username) == null) {
      User user = new User();
      user.setUsername(username);
      em.persist(user);

      LOGGER.info("Persisted user with username: " + username);

      return new RegisterResponse(true, user);

    } else {
      LOGGER.info("User already exists with username: " + username);
      return new RegisterResponse(false, null);
    }
  }

  @POST
  @Path("/login")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LoginResponse login(String username) {

    LOGGER.info("Call login user with username: " + username);

    User user = getUser(username);

    if (user != null) {
      String token = loginToken.addToken(user);

      LOGGER.info("User logged in with username: " + username);

      return new LoginResponse(true, token);

    } else {
      LOGGER.info("Login failed for user with username: " + username);
      return new LoginResponse(false, null);
    }
  }

  public User getUser(String username) {
    String qlString = "from User u where upper(u.username) = ?";
    TypedQuery<User> createQuery = em.createQuery(qlString, User.class);

    LOGGER.debug("Search for user with username" + username);

    String upperCaseUsername = username.toUpperCase();
    createQuery.setParameter(1, upperCaseUsername);

    LOGGER.info("Query: " + qlString + " Params, username: " + upperCaseUsername);

    List<User> resultList = createQuery.getResultList();

    LOGGER.info("Found " + resultList.size() + " results");

    if (resultList.size() > 0) {
      return resultList.get(0);

    } else {
      return null;
    }
  }
}
