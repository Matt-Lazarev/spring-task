package com.selivanov.mapper;

import com.selivanov.dto.PassportDto;
import com.selivanov.dto.PersonDto;
import com.selivanov.entity.Passport;
import com.selivanov.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonPassportMapper {
    @Mapping(target = "passport.id", source = "passportId")
    Person toPerson(PersonDto personDto);

    @Mapping(target = "passportId", source = "passport.id")
    PersonDto toPersonDto(Person person);

    List<PersonDto> toPersonsDto(List<Person> persons);

    Passport toPassport(PassportDto passportDto);

    PassportDto toPassportDto(Passport passport);

    void updatePerson(@MappingTarget Person updatablePerson, PersonDto personDto);
}