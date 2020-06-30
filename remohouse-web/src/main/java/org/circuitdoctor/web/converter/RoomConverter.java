package org.circuitdoctor.web.converter;

import org.circuitdoctor.core.model.Room;
import org.circuitdoctor.core.repository.LocationRepository;
import org.circuitdoctor.web.dto.RoomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomConverter extends BaseConverter<Room, RoomDto> {
    @Autowired
    private LocationRepository locationRepository;
    @Override
    public Room convertDtoToModel(RoomDto dto) {
        Room room = Room.builder()
                .location(locationRepository.findById(dto.getLocationID()).get())
                .name(dto.getName())
                .build();
        room.setId(dto.getId());
        return room;
    }

    @Override
    public RoomDto convertModelToDto(Room room) {
        RoomDto roomDto = RoomDto.builder()
                .locationID(room.getLocation().getId())
                .name(room.getName())
                .id(room.getId())
                .build();
        return roomDto;
    }
}
