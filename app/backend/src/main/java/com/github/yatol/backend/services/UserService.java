package com.github.yatol.backend.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

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
  @Produces({ "application/json" })
  public UserServiceResponse registerUser(@FormParam("username") String username) {

    if (getUser(username) == null) {
      User user = new User();
      user.setUsername(username);
      em.persist(user);
      return new UserServiceResponse(true, user);

    } else {
      return new UserServiceResponse(false, null);
    }
  }

  public User getUser(String name) {
    String qlString = "from User u where u.username = ?";
    TypedQuery<User> createQuery = em.createQuery(qlString, User.class);
    createQuery.setParameter(1, name);

    List<User> resultList = createQuery.getResultList();

    if (resultList.size() > 0) {
      return resultList.get(0);

    } else {
      return null;
    }
  }

  @POST
  @Path("/login")
  @Produces({ "application/json" })
  public LoginServiceResponse login(@FormParam("username") String username) {
    User user = getUser(username);

    if (user != null) {
      String token = loginToken.addToken(user);
      return new LoginServiceResponse(true, token);

    } else {
      return new LoginServiceResponse(false, null);
    }
  }
}
