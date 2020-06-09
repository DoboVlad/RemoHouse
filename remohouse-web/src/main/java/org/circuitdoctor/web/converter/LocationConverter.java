package org.circuitdoctor.web.converter;

import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.web.dto.LocationDto;

public class LocationConverter extends BaseConverter<Location, LocationDto> {

    @Override
    public Location convertDtoToModel(LocationDto dto) {
        return null;
    }

    @Override
    public LocationDto convertModelToDto(Location location) {
        return null;
    }
}
