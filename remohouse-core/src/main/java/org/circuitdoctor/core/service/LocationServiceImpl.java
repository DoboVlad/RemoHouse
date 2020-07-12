package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.repository.Repository;
import org.circuitdoctor.core.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {
    private static final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);
    @Autowired
    private Repository<Location,Long> locationRepository;
    @Autowired
    private RoomService roomService;
    @Override
    public List<Location> getAllLocations(Long userID) {
        /*
        DESCR: returns a set of Location = the locations of the user with user ID {userID}
        PARAM : userID : long
        PRE : userID > 0
        POST: -
         */
        log.trace("getAllLocations - method entered id={}",userID);
        List<Location> result = locationRepository.findAll().stream()
                .filter(location -> location.getUser().getId().equals(userID))
                .sorted(Comparator.comparing(Location::getName))
                .collect(Collectors.toList());
        log.trace("getAllLocations - method finished result={}",result);
        return result;
    }

    @Override
    public Location addLocation(@Valid Location location) {
        /*
        DESCR: adds a new location in the database
        PARAM: location - Location
        PRE: location has to be a valid Location
        POST: returns saved location in database. The id will be changed based on the rule specified in BaseEntity
         */
        log.trace("addLocation - method entered location={}",location);
        Location locationResult = locationRepository.save(location);
        log.trace("addLocation - method finished l={}",locationResult);
        return locationResult;
    }

    @Override
    public boolean checkAccessLocation(Long userID, Long locationID) {
        /*
        DESCR: verifies if the user with user id {userID} has access to the location with id {locationID}
        PARAM : userID : long, locationID : long
        PRE: userID > 0 and locationID > 0
        POST: returns true if the user has access
                      false otherwise
         */
        log.trace("checkAccessLocation - method entered uid={} lid={}",userID,locationID);
        Optional<Location> locationOpt = locationRepository.findById(locationID);
        AtomicReference<Boolean> result = new AtomicReference<>(false);
        locationOpt.ifPresent(location->{
            if(location.getUser().getId()==userID)
                result.set(true);
        });
        log.trace("checkAccessLocation - method finished r={}",result.get());
        return result.get();
    }

    @Override
    public boolean deleteLocation(Long locationID) {
        /*
        DESCR: deletes location with id {locationID} from the database
        PARAM: locationID : long
        PRE: locationID > 0
        POST: returns true if the location was successfully deleted
                      false if a location with this id does not exist
         */
        log.trace("deleteLocation - method entered l={}",locationID);
        AtomicBoolean result = new AtomicBoolean(false);
        locationRepository.findById(locationID).ifPresent(location->{
            result.set(true);
            roomService.deleteRoomsWithLocation(location);
            locationRepository.delete(location);
        });
        log.trace("deleteLocation - method finished {}",result.get());
        return result.get();
    }

    @Override
    public boolean updateLocation(Location location) {
        /*
        DESCR: updates a location
        PARAM: location - Location
        PRE: {location} has to have the id of an existing location.
        POST: returns true if the locations was updated successfully
                      false otherwise
         OBS: the function will update the fields of the location found at {Location}.id with the fields of {location}
         */
        log.trace("updateLocation - method entered l={}",location);
        Location r = locationRepository.save(location);
        log.trace("updateLocation - method finished {}",r!=null);
        return r!=null;
    }
}
