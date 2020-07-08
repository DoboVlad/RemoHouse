package org.circuitdoctor.core.repository;

import org.circuitdoctor.core.model.Location;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface LocationRepository extends Repository<Location,Long> {
    Optional<Location> findById(Long id);
}
