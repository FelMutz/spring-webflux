package application.services;

import application.exceptions.ExceptionCustom;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;



@RunWith(SpringRunner.class)
public class ValidPasswordTest {


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void validPasswordTrue(){
        ValidPassword.validPassword("123","123");
    }

    @Test
    public void validPasswordFalse(){
        thrown.expect(ExceptionCustom.class);
        thrown.expectMessage("Dado não confere com o banco!!");
        thrown.expect(hasProperty("httpStatus", is(HttpStatus.NOT_ACCEPTABLE)));

        ValidPassword.validPassword("123","456");
    }

}