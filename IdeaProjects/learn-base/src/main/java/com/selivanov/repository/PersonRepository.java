package com.selivanov.repository;

import com.selivanov.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("select p from Person p left join fetch p.passport")
    List<Person> findAllPersons();

    @Query("select p from Person p left join fetch p.passport where p.id = :id")
    Optional<Person> findPersonById(Integer id);
}
