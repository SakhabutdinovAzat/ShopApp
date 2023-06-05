package com.store.services;

import com.store.models.Item;
import com.store.models.Mood;
import com.store.models.Person;
import com.store.repositories.PeopleRepositories;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepositories peopleRepositories;

    @Autowired
    public PeopleService(PeopleRepositories peopleRepositories) {
        this.peopleRepositories = peopleRepositories;
    }

    public List<Person> findAll(){
        return peopleRepositories.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepositories.findById(id);

        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        person.setCreatedAt(new Date());
        person.setMood(Mood.CALM);
        peopleRepositories.save(person);
    }

    @Transactional
    public void update(int id, Person updatePerson) {
        updatePerson.setId(id);
        peopleRepositories.save(updatePerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepositories.deleteById(id);
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepositories.findByFullName(fullName);
    }

    public Optional<Person> findByEmail(String email) {
        return peopleRepositories.findByEmail(email);
    }

    public List<Item> getItemsById(int id) {
        Optional<Person> person = peopleRepositories.findById(id);

        if (person.isPresent()){
            Hibernate.initialize(person.get().getItems());

            // Проверка просрочености оплаты
            person.get().getItems().forEach(item -> {
                long diffInMillies = Math.abs(item.getTakenAt().getTime() - new Date().getTime());
                // 2592000000 миллисекунд = 3 суток
                if (diffInMillies >= 259200000) {
                    item.setExpired(true); // оплата просрочена
                }
            });
            return person.get().getItems();
        } else {
            return Collections.emptyList();
        }
    }

    public void test(){
        System.out.println("Testing here with debug");
    }
}
