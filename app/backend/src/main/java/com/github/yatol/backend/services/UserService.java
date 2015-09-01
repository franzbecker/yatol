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

import com.github.yatol.backend.entities.User;
import com.github.yatol.backend.security.LoginToken;

@Path("/users")
@Stateless
public class UserService {

  @PersistenceContext(unitName = "yatol")
  EntityManager em;

  @Inject
  LoginToken loginToken;

  @POST
  @Path("/register")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public UserServiceResponse registerUser(String username) {

    System.err.println("Paramenter name: " + username);

    if (getUser(username) == null) {
      User user = new User();
      user.setUsername(username);
      em.persist(user);
      return new UserServiceResponse(true, user);

    } else {
      return new UserServiceResponse(false, null);
    }
  }

  @POST
  @Path("/login")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LoginServiceResponse login(String username) {

    System.err.println("Paramenter name: " + username);
    User user = getUser(username);

    if (user != null) {
      String token = loginToken.addToken(user);
      return new LoginServiceResponse(true, token);

    } else {
      return new LoginServiceResponse(false, null);
    }
  }

  public User getUser(String name) {
    String qlString = "from User u where u.username = ?";
    TypedQuery<User> createQuery = em.createQuery(qlString, User.class);
    System.err.println("Paramenter name: " + name);
    createQuery.setParameter(1, name);

    List<User> resultList = createQuery.getResultList();

    if (resultList.size() > 0) {
      return resultList.get(0);

    } else {
      return null;
    }
  }
}
