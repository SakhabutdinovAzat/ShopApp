package com.store.repositories;

import com.store.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepositories extends JpaRepository<Person, Integer> {
    Optional<Person> findByFullName(String fullName);

    Optional<Person> findByEmail(String email);

}
