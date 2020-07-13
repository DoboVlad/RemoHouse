package org.circuitdoctor.core.repository;

import org.circuitdoctor.core.model.*;
import org.springframework.stereotype.Component;
import java.util.List;
@Component
public interface ActionLogGSMRepository extends  Repository<ActionLogGSM,Long>{
    List<ActionLogGSM> findAllByUser(User user);
    List<ActionLogGSM> findAllByGsmController(GSMController gsmController);

}
