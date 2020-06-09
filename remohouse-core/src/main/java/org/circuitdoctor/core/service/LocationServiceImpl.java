package org.circuitdoctor.core.service;

import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    private static final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    @Autowired
    private LocationRepository locationRepository;
    @Override
    public List<Location> getAllLocations() {
        log.trace("getAllLocations - method entered");
        List<Location> result = locationRepository.findAll();
        log.trace("getAllLocations - method finished result={}",result);
        return result;
    }
}
