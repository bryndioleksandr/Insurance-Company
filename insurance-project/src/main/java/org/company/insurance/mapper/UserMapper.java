package org.company.insurance.mapper;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import org.company.insurance.dto.UserCreationDto;
import org.company.insurance.dto.UserDto;
import org.company.insurance.entity.InsurancePolicy;
import org.company.insurance.entity.User;
import org.company.insurance.enums.Role;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "password", source = "password")
    @Mapping(target = "role", source = "role")
    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);

    User toEntity(UserCreationDto userCreationDto);

}