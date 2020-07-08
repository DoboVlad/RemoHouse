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
        /*
        DESCR: adds a new room in the database
        PARAM:room - Room
        PRE:{room} has to be valid
        POST:returns the room that was added in the database. The id will be changed based on the rule specified in BaseEntity
         */
        log.trace("addRoom - method entered room={}",room);
        Room roomResult = roomRepository.save(room);
        log.trace("addRoom - method finished newRoom={}",roomResult);
        return roomResult;
    }

    @Override
    public Room updateRoom(@Valid Room room) {
        /*
        DESCR:updates a room. Similar with update from LocationServiceImpl
        PARAM:room - Room
        PRE:room has to be valid
        POST:returns the room saved in the database
         */
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
        /*
        DESCR:returns all the rooms from a location
        PARAM:location - Location
        PRE:location != null
        POST:returns a list of all the rooms from {location}
         */
        log.trace("getRooms - method entered l={}",location);
        List<Room> result =  roomRepository.findAllByLocation(location);
        log.trace("getRooms - method finished r={}",result);
        return result;
    }

    @Override
    public boolean deleteRoom(Long room) {
        /*
        DESCR:deletes a room form the database
        PARAM:room - Long (the room id)
        PRE:room > 0
        POST:returns true if it was deleted successfully
                     false if a room with this id dies not exists
         */
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
        /*
        DESCR:deletes all teh rooms from the given location
        PARAM:location - Location
        PRE:location != null
        POST:-
         */
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
        /*
        DESCR:checks if an user has access to a room
        PARAM:id - Long (the user's id), roomID - Long
        PRE:id > 0, roomID > 0
        POST:returns true if the user has access
                     false oterwise
         */
        log.trace("checkAccessRoom - method entered u={} r={}",id,roomID);
        AtomicBoolean r = new AtomicBoolean(false);
        roomRepository.findById(roomID).ifPresent(room->{
            r.set(room.getLocation().getUser().getId().equals(id));
        });
        log.trace("deleteRoomsWithLocation - method finished r={}",r.get());
        return r.get();
    }
}
