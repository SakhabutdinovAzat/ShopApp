package com.store.DAO;

import com.store.models.Person;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Access;
import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PersonDAO {

    @Autowired
    private final EntityManager entityManager;

    public PersonDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public void testNPlusOne(){
        Session session = entityManager.unwrap(Session.class);

        Set<Person> people = new HashSet<Person> (session.createQuery("select p from Person p left join fetch p.items", Person.class).
                getResultList());

        for (Person person : people) {
            System.out.println("Person " + person.getFullName() + " has items: " + person.getItems());
        }
    }
}
