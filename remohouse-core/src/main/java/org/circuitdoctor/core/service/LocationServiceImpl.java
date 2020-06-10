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
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {
    private static final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);
    @Autowired
    private Repository<Location,Long> locationRepository;
    @Autowired
    private Repository<User,Long> userRepository;
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
}
