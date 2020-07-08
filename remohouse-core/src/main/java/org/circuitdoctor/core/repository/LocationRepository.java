package org.circuitdoctor.core.repository;

import org.circuitdoctor.core.model.Location;
import org.springframework.stereotype.Component;

@Component
public interface LocationRepository extends Repository<Location,Long> {
}
