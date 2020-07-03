package org.circuitdoctor.core.repository;

import org.circuitdoctor.core.model.*;

import java.util.List;

public interface ActionLogGSMRepository extends  Repository<ActionLogGSM,Long>{
    List<ActionLogGSM> findAllByUser(User user);
    List<ActionLogGSM> findAllByGsmController(GSMController gsmController);
}
