package com.selivanov.controller;

import com.selivanov.dto.PassportDto;
import com.selivanov.dto.PersonDto;
import com.selivanov.dto.PersonPassportRequest;
import com.selivanov.service.PersonService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<PersonDto>> getPersons() {
        List<PersonDto> persons = service.getPersons();
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPersonById(@PathVariable Integer id) {
        PersonDto person = service.getPerson(id);
        return ResponseEntity.ok(person);
    }

    @GetMapping("/{id}/passport")
    public ResponseEntity<PassportDto> getPersonPassport(@PathVariable("id") Integer personId) {
        PassportDto passportByPerson = service.getPersonPassport(personId);
        return ResponseEntity.ok(passportByPerson);
    }

    @PostMapping
    public ResponseEntity<?> savePerson(@Valid @RequestBody PersonDto personDto) {
        service.savePerson(personDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/passport")
    public ResponseEntity<?> addPassportToPerson(@PathVariable("id") Integer personId,
                                                 @Valid @RequestBody PersonPassportRequest request) {
        service.addPassportToPerson(personId, request.passport());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePersonById(@PathVariable Integer id,
                                              @Valid @RequestBody PersonDto personDto) {
        service.updatePersonById(id, personDto);
        return ResponseEntity.ok().build();
    }

    //Detaches passport from person
    @DeleteMapping("/{id}/passport")
    public ResponseEntity<?> detachPassportFromPerson(@PathVariable("id") Integer personId) {
        service.deletePassportFromPerson(personId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePersonById(@PathVariable Integer id) {
        service.deletePersonById(id);
        return ResponseEntity.ok().build();
    }
}