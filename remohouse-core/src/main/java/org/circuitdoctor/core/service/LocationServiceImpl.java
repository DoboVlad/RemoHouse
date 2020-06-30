package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.repository.Repository;
import org.circuitdoctor.core.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;
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
    public Set<Location> getAllLocations(Long userID) {
        log.trace("getAllLocations - method entered id={}",userID);
        Set<Location> result = locationRepository.findAll().stream()
                .filter(location -> location.getUser().getId().equals(userID))
                .collect(Collectors.toSet());
        log.trace("getAllLocations - method finished result={}",result);
        return result;
    }

    @Override
    public Location addLocation(@Valid Location location) {
        log.trace("addLocation - method entered location={}",location);
        Location locationResult = locationRepository.save(location);
        log.trace("addLocation - method finished l={}",locationResult);
        return locationResult;
    }

    @Override
    public boolean checkAccessLocation(Long userID, Long locationID) {
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
        log.trace("updateLocation - method entered l={}",location);
        Location r = locationRepository.save(location);
        log.trace("updateLocation - method finished {}",r!=null);
        return r!=null;
    }
}
