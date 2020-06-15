package org.circuitdoctor.web.controller;

import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.service.LocationService;
import org.circuitdoctor.web.converter.LocationConverter;
import org.circuitdoctor.web.dto.LocationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Set;

@RestController
public class LocationController {
    private static final Logger log = LoggerFactory.getLogger(LocationController.class);
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationConverter locationConverter;

    @RequestMapping(value = "location/addLocation/{userID}",method = RequestMethod.POST)
    String addLocation(@RequestBody @Valid LocationDto locationDto,@PathVariable Long userID, BindingResult bindingResult){
        //receives a location already created
        log.trace("addLocation - method entered location={}",locationDto);
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(error->log.error("error - {}",error.toString()));
            log.trace("addLocation - validation error occurred");
            return "validation error";
        }
        if(userID.equals(locationDto.getUserID())) {
            Location result = locationService.addLocation(locationConverter.convertDtoToModel(locationDto));
            log.trace("addLocation - method finished l={}", result);
            if (result != null)
                return String.valueOf(result.getId());
            return "null";
        }
        log.trace("addLocation - userID in path and userID in location are different");
        return "different user ids";
    }

    @RequestMapping(value = "location/getLocations/{userID}",method = RequestMethod.GET)
    Set<LocationDto> getLocations(@PathVariable Long userID){
        log.trace("getLocations - method entered userID={}",userID);
        Set<Location> locations = locationService.getAllLocations(userID);
        log.trace("getLocations - method finished l={}",locations);
        return locationConverter.convertModelsToDtos(locations);
    }

}
