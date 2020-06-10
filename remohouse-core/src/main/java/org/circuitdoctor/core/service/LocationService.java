package org.circuitdoctor.core.service;
import org.circuitdoctor.core.model.Location;
import java.util.List;
public interface LocationService {
    List<Location> getAllLocations();
    Location addLocation(Location location, Long userID);
}
