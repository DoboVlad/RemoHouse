package org.circuitdoctor.web.converter;

import org.circuitdoctor.core.model.User;
import org.circuitdoctor.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter extends BaseConverter<User, UserDto> {
    @Autowired
    private LocationConverter locationConverter;

    @Override
    public User convertDtoToModel(UserDto dto) {
        User userResult = User.builder()
                .email(dto.getEmail())
                .locations(locationConverter.convertIDstoModel(dto.getLocations()))
                .name(dto.getName())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .surname(dto.getSurname())
                .build();
        return userResult;
    }

    @Override
    public UserDto convertModelToDto(User user) {
        UserDto userDto= UserDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .locations(locationConverter.convertModelsToIDs(user.getLocations()))
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .name(user.getName())
                .surname(user.getSurname())
                .build();
        return userDto;
    }
}
