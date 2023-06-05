package com.store.util;


import com.store.models.Passport;
import com.store.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/*@Component
public class PassportValidator implements Validator {

    public final ItemService itemService;

    @Autowired
    public PassportValidator(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Passport.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Passport passport = new Passport();

        //
        if (PassportService. (passport.getNumber()).isPresent()){
            errors.rejectValue("name", "", "This number is already exist");
        }
    }
}*/
