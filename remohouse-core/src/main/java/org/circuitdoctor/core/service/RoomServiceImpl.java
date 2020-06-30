package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.model.Room;
import org.circuitdoctor.core.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private GSMControllerService gsmControllerService;
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

    @Override
    public List<Room> getRooms(Location location) {
        log.trace("getRooms - method entered l={}",location);
        List<Room> result =  roomRepository.findAllByLocation(location);
        log.trace("getRooms - method finished r={}",result);
        return result;
    }

    @Override
    public boolean deleteRoom(Long room) {
        log.trace("deleteRoom - method entered r={} ",room);
        AtomicReference<Boolean> roomFound = new AtomicReference<>(false);
        Optional<Room> roomFromDB = roomRepository.findById(room);
        roomFromDB.ifPresent(roomDB->{
            gsmControllerService.deleteGSMsWithRoom(roomDB);
            roomRepository.delete(roomDB);
            roomFound.set(true);
        });
        log.trace("deleteRoom - method finished");
        return roomFound.get();
    }

    @Override
    public void deleteRoomsWithLocation(Location location) {
        log.trace("deleteRoomsWithLocation - method entered l={}",location);
        List<Room> roomsToBeDeleted = roomRepository.findAllByLocation(location);
        roomsToBeDeleted.forEach(room->{
            roomRepository.delete(room);
            log.trace("deleteRoomWithLocation - delete room r={}",room.getId());
        });
        log.trace("deleteRoomWithLocation - method finished");
    }

    @Override
    public boolean checkAccessRoom(Long id, Long roomID) {
        log.trace("checkAccessRoom - method entered u={} r={}",id,roomID);
        AtomicBoolean r = new AtomicBoolean(false);
        roomRepository.findById(roomID).ifPresent(room->{
            r.set(room.getLocation().getUser().getId().equals(id));
        });
        log.trace("deleteRoomsWithLocation - method finished r={}",r.get());
        return r.get();
    }
}
