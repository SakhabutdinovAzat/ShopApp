package com.store.util;

import com.store.models.Person;
import com.store.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        // посмотреть, если человек с таким ФИО в БД
        if(peopleService.getPersonByFullName(person.getFullName()).isPresent())
            errors.rejectValue("fullName", "", "This fullName id already taken");

        // посмотреть, если человек с таким email в БД
        if (peopleService.findByEmail(person.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "This email is already taken");
        }

/*        // Неверный формат даты
        if(person.getDateOfBirth())  {
            errors.rejectValue("dateOfBirth", "", "Date of birth invalid");
        }*/
    }
}
