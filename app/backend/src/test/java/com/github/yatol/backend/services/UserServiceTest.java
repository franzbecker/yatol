package com.github.yatol.backend.services;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.github.yatol.backend.entities.User;
import com.github.yatol.backend.security.LoginToken;

public class UserServiceTest {

  private UserService userServiceImpl;

  @Mock
  private EntityManager mockedEntityManager;
  
  @Mock
  private LoginToken mockedLoginToken;

  @Mock
  private TypedQuery<User> mockedTypedQuery;

  private ArrayList<User> userList;

  @Before()
  @SuppressWarnings("unchecked")
  public void before() {
    userServiceImpl = new UserService();

    MockitoAnnotations.initMocks(this);
    userServiceImpl.em = mockedEntityManager;
    userServiceImpl.loginToken = mockedLoginToken;

    Mockito.when(mockedEntityManager.createQuery(Mockito.any(String.class), Mockito.any(Class.class)))
        .thenReturn(mockedTypedQuery);

    Mockito.when(mockedLoginToken.addToken(Mockito.any(User.class))).thenReturn(UUID.randomUUID().toString());
    
    userList = new ArrayList<User>();
    Mockito.when(mockedTypedQuery.getResultList()).thenReturn(userList);
  }

  @Test
  public void testRegisterNonExistingUser() throws Exception {
    UserServiceResponse userServiceResponse = userServiceImpl.registerUser(null);

    assertThat(userServiceResponse.isSuccess()).isTrue();
    assertThat(userServiceResponse.getUser()).isNotNull();
  }

  @Test
  public void testRegisterExistingUser() throws Exception {
    addUserToList();

    UserServiceResponse userServiceResponse = userServiceImpl.registerUser(null);
    assertThat(userServiceResponse.isSuccess()).isFalse();
  }

  @Test
  public void testGetExistingUser() throws Exception {
    User user = addUserToList();

    assertThat(userServiceImpl.getUser(null)).isSameAs(user);
  }

  @Test
  public void testGetNotExistingUser() throws Exception {
    assertThat(userServiceImpl.getUser(null)).isNull();
  }

  @Test
  public void testLoginExistingUser() throws Exception {
    addUserToList();
    LoginServiceResponse loginServiceResponse = userServiceImpl.login(null);
    assertThat(loginServiceResponse.getToken()).isNotNull();
  }

  private User addUserToList() {
    User user = new User();
    userList.add(user);
    return user;
  }
}
