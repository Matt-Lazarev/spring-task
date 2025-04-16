package com.selivanov.service;

import com.selivanov.dto.PassportDto;
import com.selivanov.dto.PersonDto;
import com.selivanov.entity.Passport;
import com.selivanov.entity.Person;
import com.selivanov.exception.NoSuchEntityException;
import com.selivanov.mapper.PersonPassportMapper;
import com.selivanov.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository repository;
    private final PersonPassportMapper mapper;
    private final PassportService passportService;

    @Transactional(readOnly = true)
    public List<PersonDto> getAllPersons() {
        List<Person> persons = repository.findAllPersons();
        return mapper.toPersonsDto(persons);
    }

    @Transactional(readOnly = true)
    public PersonDto getPersonById(Integer id) {
        Person person = repository.findPersonById(id)
                .orElseThrow(() -> new NoSuchEntityException("Entity not found"));
        return mapper.toPersonDto(person);
    }

    @Transactional(readOnly = true)
    public PassportDto getPassportByPerson(Integer personId) {
        Person person = repository.findPersonById(personId)
                .orElseThrow(() -> new NoSuchEntityException("Entity not found"));

        Passport passport = person.getPassport();
        return mapper.toPassportDto(passport);
    }

    @Transactional
    public void savePerson(PersonDto personDto) {
        Person person = mapper.toPerson(personDto);
        repository.save(person);
    }

    @Transactional
    public void addPassportToPerson(Integer id, PassportDto passportDto) {
        Person person = repository.findPersonById(id)
                .orElseThrow(() -> new NoSuchEntityException("Entity not found"));

        if (person.getPassport() != null) {
            throw new IllegalArgumentException("Passport is already exist");
        }

        Passport passport = passportService.createOrGetPassport(passportDto);
        person.setPassport(passport);

        repository.save(person);
    }

    @Transactional
    public void updatePersonById(Integer id, PersonDto personDto) {
        Person updatablePerson = repository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("Entity not found"));

        mapper.updatePerson(updatablePerson, personDto);
        repository.save(updatablePerson);
    }

    // update Person p set p.passport = null;
    @Transactional
    public void detachPassportFromPerson(Integer id) {
        Person person = repository.findPersonById(id)
                .orElseThrow();

        person.setPassport(null);
        repository.save(person); //ignores if passport == null
    }

    @Transactional
    public void deletePersonById(Integer id) {
        Person person = repository.findById(id)
                .orElseThrow();
        repository.delete(person);
    }
}