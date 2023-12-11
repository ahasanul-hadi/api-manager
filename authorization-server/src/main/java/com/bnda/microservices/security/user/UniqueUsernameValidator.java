package com.bnda.microservices.security.user;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUsernameValidator.class)

@interface UniqueUsername {

    String message() default "Hi! This username already exists! in custom";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}

@Component
@RequiredArgsConstructor
@Data
public class UniqueUsernameValidator implements  ConstraintValidator<UniqueUsername, String>{

    /**TODO
     * Add below line in properties file to inject bean
        spring.jpa.properties.javax.persistence.validation.mode=none
     */

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        System.out.println("inside custom validate");
        return !userRepository.findByUsername(value).isPresent();
    }

   /* @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);;
    }*/

   /* @Override
    public void validate(Object target, Errors errors) {
        errors.rejectValue("username","Username already exists");
    }*/
}
