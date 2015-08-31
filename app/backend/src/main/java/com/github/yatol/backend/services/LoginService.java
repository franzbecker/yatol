package com.github.yatol.backend.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Stateless
@Path("/users")
public class LoginService {

  @PersistenceContext(unitName = "yatol")
  EntityManager em;


}
