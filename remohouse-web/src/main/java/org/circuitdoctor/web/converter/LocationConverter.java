package org.circuitdoctor.web.converter;
import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.repository.UserRepository;
import org.circuitdoctor.web.dto.LocationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocationConverter extends BaseConverter<Location, LocationDto> {
    @Autowired
    private UserRepository userRepository;
    @Override
    public Location convertDtoToModel(LocationDto dto) {

        Location location = Location.builder()
                .image(dto.getImage())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .name(dto.getName())
                .user(userRepository.findById(dto.getUserID()).get())
                .city(dto.getCity())
                .build();
        location.setId(dto.getId());
        return location;

    }

    @Override
    public LocationDto convertModelToDto(Location location) {
        LocationDto locationDto = LocationDto.builder()
                .id(location.getId())
                .image(location.getImage())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .name(location.getName())
                .userID(location.getUser().getId())
                .city(location.getCity())
                .build();
        return locationDto;
    }
}
