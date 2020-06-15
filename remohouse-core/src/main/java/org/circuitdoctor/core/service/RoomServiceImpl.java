package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.model.Room;
import org.circuitdoctor.core.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    @Autowired
    private Repository<Room,Long> roomRepository;

    @Override
    public Room addRoom(@Valid Room room) {
        log.trace("addRoom - method entered room={}",room);
        Room roomResult = roomRepository.save(room);
        log.trace("addRoom - method finished newRoom={}",roomResult);
        return roomResult;
    }

    @Override
    public Room updateRoom(@Valid Room room) {
        log.trace("updateRoom - method entered room={}",room);

        AtomicReference<Room> newRoom = new AtomicReference<>();
        Optional<Room> roomFromDB = roomRepository.findById(room.getId());
        roomFromDB.ifPresent(roomDB->{

            roomDB.setName(room.getName());
            roomRepository.save(roomDB);
            newRoom.set(roomDB);
        });

        log.trace("\"updateRoom - method finished room={}",newRoom);
        return newRoom.get();
    }
}
