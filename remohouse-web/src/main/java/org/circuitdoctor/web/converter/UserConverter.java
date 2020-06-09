package org.circuitdoctor.web.converter;

import org.circuitdoctor.core.model.BaseEntity;
import org.circuitdoctor.core.model.User;
import org.circuitdoctor.web.dto.BaseDto;
import org.circuitdoctor.web.dto.UserDto;

public class UserConverter extends BaseConverter<User, UserDto> {

    @Override
    public User convertDtoToModel(UserDto dto) {
        return null;
    }

    @Override
    public UserDto convertModelToDto(User user) {
        UserDto userDto=new UserDto();
        return userDto;

    }
}
