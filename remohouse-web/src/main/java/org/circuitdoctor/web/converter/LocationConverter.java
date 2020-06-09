package org.circuitdoctor.web.converter;
import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.web.dto.LocationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocationConverter extends BaseConverter<Location, LocationDto> {
    @Autowired
    private UserConverter userConverter;
    @Override
    public Location convertDtoToModel(LocationDto dto) {

        Location location = Location.builder()
                .image(dto.getImage())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .name(dto.getName())
                .user(userConverter.convertDtoToModel(dto.getUserDto()))
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
                .userDto(userConverter.convertModelToDto(location.getUser()))
                .build();
        return locationDto;
    }
}
