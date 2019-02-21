package application;

import application.controllers.AccountControllerTest;
import application.controllers.BankSharesControllerTest;
import application.controllers.PersonControllerTest;
import application.services.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ValidPasswordTest.class,
        PersonServiceTest.class,
        AccountServiceTest.class,
        BankSharesServiceTest.class,
        RoudTest.class,
        BankSharesControllerTest.class,
        AccountControllerTest.class,
        PersonControllerTest.class
})
public class RunAllTests {
}
