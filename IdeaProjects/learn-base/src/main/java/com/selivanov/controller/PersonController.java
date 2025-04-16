package com.selivanov.controller;

import com.selivanov.dto.PassportDto;
import com.selivanov.dto.PersonDto;
import com.selivanov.dto.PersonPassportRequest;
import com.selivanov.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService service;

    @GetMapping
    public ResponseEntity<List<PersonDto>> getAllPersons() {
        List<PersonDto> persons = service.getAllPersons();
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPersonById(@PathVariable Integer id) {
        PersonDto person = service.getPersonById(id);

        return ResponseEntity.ok(person);
    }

    @GetMapping("/{id}/passport")
    public ResponseEntity<PassportDto> getPersonPassport(@PathVariable("id") Integer personId) {
        PassportDto passportByPerson = service.getPassportByPerson(personId);

        return ResponseEntity.ok(passportByPerson);
    }

    @PostMapping
    public ResponseEntity<?> savePerson(@RequestBody PersonDto personDto) {
        service.savePerson(personDto);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/passport")
    public ResponseEntity<?> addPassportToPerson(@PathVariable Integer id,
                                                 @RequestBody PersonPassportRequest request) {
        service.addPassportToPerson(id, request.passportDto());

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePersonById(@PathVariable Integer id,
                                              @RequestBody PersonDto personDto) {
        service.updatePersonById(id, personDto);
        return ResponseEntity.ok().build();
    }

    // Detaches passport from person
    @DeleteMapping("/{id}/passport")
    public ResponseEntity<?> detachPassportFromPerson(@PathVariable Integer id) {
        service.detachPassportFromPerson(id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{/id}")
    ResponseEntity<?> deletePersonById(@PathVariable Integer id) {
        service.deletePersonById(id);
        return ResponseEntity.ok().build();
    }
}