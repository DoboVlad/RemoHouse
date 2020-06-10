package org.circuitdoctor.web.converter;

import org.circuitdoctor.core.model.User;
import org.circuitdoctor.web.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserConverter extends BaseConverter<User, UserDto> {

    @Override
    public User convertDtoToModel(UserDto dto) {
        User userResult = User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .surname(dto.getSurname())
                .build();
        userResult.setId(dto.getId());
        return userResult;
    }

    @Override
    public UserDto convertModelToDto(User user) {
        UserDto userDto= UserDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .name(user.getName())
                .surname(user.getSurname())
                .build();
        return userDto;
    }
}
