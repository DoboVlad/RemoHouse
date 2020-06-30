package org.circuitdoctor.web.controller;

import org.apache.commons.dbcp2.DelegatingResultSet;
import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.service.LocationService;
import org.circuitdoctor.web.converter.LocationConverter;
import org.circuitdoctor.web.dto.LocationDto;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;
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

    @RequestMapping(value = "location/delete/{userID}/{locationID}",method = RequestMethod.DELETE)
    boolean deleteLocation(@PathVariable Long userID, @PathVariable Long locationID){
        log.trace("deleteLocation - method entered");
        if(!locationService.checkAccessLocation(userID,locationID)) {
            log.error("deleteLocation - user has no access to this room");
            return false;
        }
        boolean r = locationService.deleteLocation(locationID);
        log.trace("deleteLocation - method finished r={}",r);
        return r;
    }

    @RequestMapping(value = "location/update/{userID}",method = RequestMethod.PUT)
    boolean updateLocation(@PathVariable Long userID, @RequestBody @Valid LocationDto location, BindingResult errors){
        log.trace("updateLocation - method entered location={}",location);
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error->log.error("error - {}",error.toString()));
            log.trace("updateLocation - validation error occurred");
            return false;
        }
        if(!userID.equals(location.getUserID())) {
            log.trace("addLocation - userID in path and userID in location are different");
            return false;
        }
        boolean r = locationService.updateLocation(locationConverter.convertDtoToModel(location));
        log.trace("updateLocation - method finished r={}",r);
        return r;
    }

    @RequestMapping(value = "location/getLocations/{userID}",method = RequestMethod.GET)
    Set<LocationDto> getLocations(@PathVariable Long userID){
        log.trace("getLocations - method entered userID={}",userID);
        Set<Location> locations = locationService.getAllLocations(userID);
        log.trace("getLocations - method finished l={}",locations);
        return locationConverter.convertModelsToDtos(locations);
    }

}
