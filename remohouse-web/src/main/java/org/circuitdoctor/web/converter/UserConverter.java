package org.circuitdoctor.web.converter;

import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.repository.UserRepository;
import org.circuitdoctor.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class UserConverter extends BaseConverter<User, UserDto> {
    @Autowired
    private LocationConverter locationConverter;
    @Autowired
    private UserRepository userRepository;

    @Override
    public User convertDtoToModel(UserDto dto) {
        Optional<User> userOptional = userRepository.findById(dto.getId());
        AtomicReference<User> userResult = null;
        userOptional.ifPresent(user->{
            userResult.set(User.builder()
                    .email(dto.getEmail())
                    .locations(locationConverter.convertIDstoModel(dto.getLocations()))
                    .name(user.getName())
                    .password(dto.getPassword())
                    .phoneNumber(dto.getPhoneNumber())
                    .surname(user.getSurname())
                    .build());
        });
        return userResult.get();
    }

    @Override
    public UserDto convertModelToDto(User user) {
        UserDto userDto= UserDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .locations(locationConverter.convertModelsToIDs(user.getLocations()))
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .build();
        return userDto;
    }
}
