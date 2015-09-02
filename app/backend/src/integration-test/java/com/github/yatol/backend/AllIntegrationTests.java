package com.github.yatol.backend;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.github.yatol.backend.services.UserLoginIntegrationTest;
import com.github.yatol.backend.services.UserRegisterIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({ UserLoginIntegrationTest.class, UserRegisterIntegrationTest.class })
public class AllIntegrationTests {

}
