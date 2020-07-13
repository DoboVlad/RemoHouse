package org.circuitdoctor.web.controller;
import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.model.Room;
import org.circuitdoctor.core.repository.LocationRepository;
import org.circuitdoctor.core.service.LocationService;
import org.circuitdoctor.core.service.RoomService;
import org.circuitdoctor.web.converter.RoomConverter;
import org.circuitdoctor.web.dto.RoomDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class RoomController {
    private static final Logger log = LoggerFactory.getLogger(RoomController.class);
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomConverter roomConverter;
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationRepository locationRepository;

    @RequestMapping(value = "room/addRoom/{userID}",method = RequestMethod.PUT)
    public String addRoom(@RequestBody @Valid RoomDto roomDto,@PathVariable Long userID, BindingResult errors){
        /*
        DESCR:adds a new Room
        PARAM:roomDto  - RoomDto  : must be valid. If request is used, this is given in the body of the request
              userID       - Long         : If request is used, this is given in the path of the request
              bindingResult- BindingResult:If there are any error validations, this is where they will be stored.
        PRE:None of them null. RoomDto must be valid. UserID>0
        POST: 405 if error validations - when request used
              "validation errors" if any validation errors occured
              "null" if there is no user with the given id
              "different user ids" if the user does not have access to the room
              the id of the saved room if success
         */
        log.trace("addRoom - method entered roomdto={}",roomDto);
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error->log.error("error - {}",error.toString()));
            log.trace("addRoom - validation error");
            return "validation errors";
        }

        boolean userHasAccessToThisLocation = locationService.checkAccessLocation(userID,roomDto.getLocationID());
        if(userHasAccessToThisLocation) {
            Room r = roomService.addRoom(roomConverter.convertDtoToModel(roomDto));
            log.trace("addRoom - method finished r={}", r);
            return String.valueOf(r.getId());
        }
        log.warn("addRoom - {} has no access",userID);
        return "user has no access";
    }

    @RequestMapping(value = "room/updateRoom/{id}",method = RequestMethod.PUT)
    public String updateRoom(@RequestBody @Valid RoomDto roomDto,@PathVariable Long id, BindingResult errors){
        /*
        DESCR:updates a room
        PARAM:userID      - Long         : If request is used, this is given in the path of the request
              roomDto - RoomDto  : If request is used, this is sent in the body of the request
              errors      - BindingResult: here will be stored all the validation erros
        PRE:None null, userID>0, roomDto is valid
        POST: return true if success
                     false if user has no access to the room or update failed
         */
        log.trace("updateRoom - method entered roomdto={}",roomDto);
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error->log.error("error - {}",error.toString()));
            log.trace("updateRoom - validation error");
            return "validation errors";
        }
        if(locationService.checkAccessLocation(id,roomDto.getLocationID())) {
            Room r = roomService.updateRoom(roomConverter.convertDtoToModel(roomDto));
            log.trace("updateRoom - method finished r={}", r);
            return String.valueOf(r.getId());
        }
        log.warn("updateRoom - {} has no access",id);
        return "user has no access";
    }




    
    @RequestMapping(value = "room/deleteRoom/{id}/{roomID}",method = RequestMethod.DELETE)
    public String deleteRoom(@PathVariable Long roomID,@PathVariable Long id){
        /*
        DESCR: deletes a room
        PARAM: userID     - Long : If request is used, this is given in the path of the request
               roomID - Long : If request is used, this is given in the path of the request
        PRE:userID > 0, roomID>0
        POST: returns success message if success
                      return error message if the user has no access to the room or if the delete failed
         */
        log.trace("deleteRoom(controller) - method entered roomdto={}",roomID);


        if(roomService.checkAccessRoom(id,roomID)) {
            boolean result = roomService.deleteRoom(roomID);
            log.trace("deleteRoom(controller) - method finished r={}", result);
            if(result){
                return "room deleted";
            }
        }
        log.warn("deleteRoom(controller)- {} has no access",id);
        return "user has no access";
    }


    @RequestMapping(value = "room/getRooms/{userID}/{locationID}",method = RequestMethod.GET)
    public List<RoomDto> getRooms(@PathVariable Long userID, @PathVariable Long locationID){
        /*
        DESCR: gets all the rooms of a user from a location
        PARAM:userID - Long : If request is used, this is given in the path of the request
                locationID - Long : If request is used, this is given in the path of the request
        PRE:userID>0 locationID>0
        POST: returns the set of roomDto of the given user and location
         */
        log.trace("getRooms - method entered uid={} lid={}",userID,locationID);
        Optional<Location> locationOptional = locationRepository.findById(locationID);
        log.trace(locationOptional.toString());
        AtomicBoolean locationBool = new AtomicBoolean(false);
        AtomicReference<List<Room>> result = new AtomicReference<>();
        locationOptional.ifPresent(location -> {
            locationBool.set(true);
            if(userID.equals(location.getUser().getId()))
                result.set(roomService.getRooms(location));
            else
                log.trace("getRooms - user does not have access to this location");
        });
        if(!locationBool.get()){
            log.trace("getRoom - locationID invalid");
            return null;
        }

        return roomConverter.convertModelsToDtos(result.get()) ;
    }
}
