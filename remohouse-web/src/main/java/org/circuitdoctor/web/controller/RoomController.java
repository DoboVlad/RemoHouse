package org.circuitdoctor.web.controller;
import org.circuitdoctor.core.model.Room;
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

@RestController
public class RoomController {
    private static final Logger log = LoggerFactory.getLogger(RoomController.class);
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomConverter roomConverter;
    @Autowired
    private LocationService locationService;

    @RequestMapping(value = "room/addRoom/{userID}",method = RequestMethod.PUT)
    public String addRoom(@RequestBody @Valid RoomDto roomDto,@PathVariable Long userID, BindingResult errors){
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
}
