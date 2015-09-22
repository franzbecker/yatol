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
import com.github.yatol.backend.services.responses.LoginResponse;
import com.github.yatol.backend.services.responses.RegisterResponse;

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
  public RegisterResponse registerUser(String username) {

    System.err.println("Paramenter name: " + username);
if(username!=null){
    if(Character.isDigit(username.charAt(0))){
    	return new RegisterResponse(false, null);
    }
}
    if (getUser(username) == null) {
      User user = new User();
      user.setUsername(username);
      em.persist(user);
      return new RegisterResponse(true, user);

    } else {
      return new RegisterResponse(false, null);
    }
  }

  @POST
  @Path("/login")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public LoginResponse login(String username) {

    System.err.println("Paramenter name: " + username);
    User user = getUser(username);

    if (user != null) {
      String token = loginToken.addToken(user);
      return new LoginResponse(true, token);

    } else {
      return new LoginResponse(false, null);
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
