package org.circuitdoctor.core.service;
import org.circuitdoctor.core.model.Location;
import java.util.List;
import java.util.Set;

public interface LocationService {
    Set<Location> getAllLocations(Long userID);
    Location addLocation(Location location);
}
