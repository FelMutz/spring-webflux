package application.services;

import application.exceptions.ExceptionCustom;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ValidPasswordTest {


    @Rule
    public ExpectedException trown = ExpectedException.none();

    @Test
    public void validPasswordTrue(){
        ValidPassword.validPassword("123","123");
    }

    @Test
    public void validPasswordFalse(){
        trown.expect(ExceptionCustom.class);
        trown.expectMessage("Dado não confere com o banco!!");
        ValidPassword.validPassword("123","456");
    }

}