package org.circuitdoctor.web.controller;

import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.repository.LocationRepository;
import org.circuitdoctor.core.service.LocationService;
import org.circuitdoctor.web.converter.LocationConverter;
import org.circuitdoctor.web.dto.LocationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class LocationController {
    private static final Logger log = LoggerFactory.getLogger(LocationController.class);
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationConverter locationConverter;

    @RequestMapping(value = "location/addLocation/{userID}",method = RequestMethod.PUT)
    LocationDto addLocation(@RequestBody @Valid LocationDto locationDto, @PathVariable Long userID, Errors errors){
        log.trace("addLocation - method entered location={} userid={}",locationDto,userID);
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error->log.error("error - {}",error.toString()));
            log.trace("addLocation - validation error occurred");
            return null;
        }
        Location result = locationService.addLocation(locationConverter.convertDtoToModel(locationDto),userID);
        log.trace("addLocation - method finished l={}",result);
        return locationConverter.convertModelToDto(result);
    }
}
