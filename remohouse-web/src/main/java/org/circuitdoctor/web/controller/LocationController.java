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
import java.util.List;
import java.util.Set;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class LocationController {
    private static final Logger log = LoggerFactory.getLogger(LocationController.class);
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationConverter locationConverter;

    @RequestMapping(value = "location/addLocation/{userID}",method = RequestMethod.POST)
    String addLocation(@RequestBody @Valid LocationDto locationDto,@PathVariable Long userID, BindingResult bindingResult){
        /*
        DESCR:adds a new location
        PARAM:locationDto  - LocationDto  : must be valid. If request is used, this is given in the body of the request
              userID       - Long         : If request is used, this is given in the path of the request
              bindingResult- BindingResult:If there are any error validations, this is where they will be stored.
        PRE:None of them null. LocationDto must be valid. UserID>0
        POST: 405 if error validations - when request used
              "validation errors" if any validation errors occured
              "null" if there is no user with the given id
              "different user ids" if the user does not have access to the location
              the id of the saved location if success
         */
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
        /*
        DESCR: deletes a location
        PARAM: userID     - Long : If request is used, this is given in the path of the request
               locationID - Long : If request is used, this is given in the path of the request
        PRE:userID > 0, locationID>0
        POST: returns true if success
                      false if the user has no access to te location or if the delete failed
         */
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
        /*
        DESCR:updates a location
        PARAM:userID      - Long         : If request is used, this is given in the path of the request
              locationDto - LocationDto  : If request is used, this is sent in the body of the request
              errors      - BindingResult: here will be stored all the validation erros
        PRE:None null, userID>0, locationDto is valid
        POST: return true if success
                     false if user has no access to the location or update failed
         */
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
    List<LocationDto> getLocations(@PathVariable Long userID){
        /*
        DESCR: gets all the locations of a user
        PARAM:userID - Long : If request is used, this is given in the path of the request
        PRE:userID:0
        POST: returns the set of locationDto of the guven user
         */
        log.trace("getLocations - method entered userID={}",userID);
        List<Location> locations = locationService.getAllLocations(userID);
        log.trace("getLocations - method finished l={}",locations);
        return locationConverter.convertModelsToDtos(locations);
    }

}
