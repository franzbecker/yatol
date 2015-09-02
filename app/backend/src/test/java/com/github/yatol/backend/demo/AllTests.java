package com.github.yatol.backend.demo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.github.yatol.backend.services.UserServiceTest;

@RunWith(Suite.class)
@SuiteClasses({ UserServiceTest.class })
public class AllTests {

}
