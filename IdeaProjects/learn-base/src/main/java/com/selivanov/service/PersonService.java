package com.selivanov.service;

import com.selivanov.dto.PassportDto;
import com.selivanov.dto.PersonDto;
import com.selivanov.entity.Passport;
import com.selivanov.entity.Person;
import com.selivanov.exception.NoSuchPersonException;
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
    public PersonDto getPerson(Integer id) {
        Person person = repository.findPersonById(id)
                .orElseThrow(() -> new NoSuchPersonException(
                        "Person with id = '%d' not found".formatted(id)
                ));
        return mapper.toPersonDto(person);
    }

    @Transactional(readOnly = true)
    public List<PersonDto> getPersons() {
        List<Person> persons = repository.findPersons();
        return mapper.toPersonsDto(persons);
    }

    @Transactional(readOnly = true)
    public PassportDto getPersonPassport(Integer personId) {
        Person person = repository.findPersonById(personId).orElseThrow(
                () -> new NoSuchPersonException(
                        "Person with id = '%d' not found".formatted(personId)
                ));

        Passport passport = person.getPassport();
        return mapper.toPassportDto(passport);
    }

    //todo
    @Transactional
    public void savePerson(PersonDto personDto) {
        Person person = mapper.toPerson(personDto);
        repository.save(person);
    }

    //todo
    @Transactional
    public void addPassportToPerson(Integer personId, PassportDto passportDto) {
        Person person = repository.findPersonById(personId).orElseThrow(
                () -> new NoSuchPersonException(
                        "Person with id = '%d' not found".formatted(personId)
                )
        );

        if (person.getPassport() != null) {
            throw new IllegalArgumentException("Passport is already exist");
        }

        Passport passport = passportService.createOrGetPassport(passportDto);
        person.setPassport(passport);
        repository.save(person);
    }

    @Transactional
    public void updatePersonById(Integer id, PersonDto personDto) {
        Person updatablePerson = repository.findPersonById(id)
                .orElseThrow(
                        () -> new NoSuchPersonException(
                                "Person with id = '%d' not found".formatted(id)
                        )
                );
        mapper.updatePerson(updatablePerson, personDto);
        repository.save(updatablePerson);
    }

    //update Person p set p.passport = null;
    @Transactional
    public void deletePassportFromPerson(Integer personId) {
        Person person = repository.findPersonById(personId).orElseThrow(
                () -> new NoSuchPersonException(
                        "Person with id = '%d' not found".formatted(personId)
                )
        );
        person.setPassport(null);
        repository.save(person); //ignores if passport == null
    }

    @Transactional
    public void deletePersonById(Integer id) {
        Person person = repository.findPersonById(id).orElseThrow(
                () -> new NoSuchPersonException(
                        "Person with id = '%d' not found".formatted(id)
                )
        );
        repository.delete(person);
    }
}