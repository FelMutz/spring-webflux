package application.dto.validator;

import application.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
@Component
public class CheckPasswordValidator implements ConstraintValidator<CheckPassword, String> {

    AccountService accountService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if ( value == null ) {
            return false;
        }

        if(value.length() < 6){
            return false;
        }

        return true;
    }
}
