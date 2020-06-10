package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class LocationServiceImpl implements LocationService {
    private static final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);
    @Autowired
    private Repository<Location,Long> locationRepository;
    @Autowired
    private Repository<User,Long> userRepository;
    @Override
    public List<Location> getAllLocations() {
        log.trace("getAllLocations - method entered");
        List<Location> result = locationRepository.findAll();
        log.trace("getAllLocations - method finished result={}",result);
        return result;
    }

    @Override
    public Location addLocation(@Valid Location location, Long userID) {
        log.trace("addLocation - method entered location={} userID={}",location,userID);
        Optional<User> userOpt = userRepository.findById(userID);
        AtomicReference<Location> locationResult = new AtomicReference<>();
        userOpt.ifPresent(user->{
            location.setUser(user);
            locationResult.set(locationRepository.save(location));
        });
        log.trace("addLocation - method finished l={}",locationResult.get());
        return locationResult.get();
    }
}
