package org.circuitdoctor.web.converter;

import org.circuitdoctor.core.model.ActionLogGSM;
import org.circuitdoctor.core.model.GSMController;
import org.circuitdoctor.core.repository.GSMControllerRepository;
import org.circuitdoctor.core.repository.UserRepository;
import org.circuitdoctor.web.dto.ActionLogGSMDto;
import org.circuitdoctor.web.dto.GSMControllerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActionLogGSMConverter extends BaseConverter<ActionLogGSM, ActionLogGSMDto> {
    @Autowired
    private GSMControllerRepository gsmControllerRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public ActionLogGSM convertDtoToModel(ActionLogGSMDto dto) {
        ActionLogGSM actionLogGSM= ActionLogGSM.builder()
                .operationType(dto.getOperationType())
                .dateTime(dto.getDateTime())
                .user(userRepository.findById(dto.getUserID()).get())
                .gsmController(gsmControllerRepository.findById(dto.getGsmControllerID()).get())
                .build();
        actionLogGSM.setId(dto.getId());
        return actionLogGSM;
    }

    @Override
    public ActionLogGSMDto convertModelToDto(ActionLogGSM actionLogGSM) {
        ActionLogGSMDto dto= ActionLogGSMDto.builder()
                .id(actionLogGSM.getId())
                .operationType(actionLogGSM.getOperationType())
                .dateTime(actionLogGSM.getDateTime())
                .gsmControllerID(actionLogGSM.getGsmController().getId())
                .userID(actionLogGSM.getUser().getId())
                .build();
        return dto;
    }
}
